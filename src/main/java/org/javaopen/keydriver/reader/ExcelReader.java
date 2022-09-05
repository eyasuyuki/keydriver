package org.javaopen.keydriver.reader;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.javaopen.keydriver.data.Record;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.driver.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.javaopen.keydriver.driver.Context.KEYS;

public class ExcelReader implements Reader {
    private static final Logger logger = Logger.getLogger(ExcelReader.class.getName());
    private static final int MAX_SHEETS = 1000;

    @Override
    public List<Section> read(Context context, String path) throws IOException {
        List<Section> sections = new ArrayList<>();
        final FileInputStream in = new FileInputStream(new File(path));
        final XSSFWorkbook workbook = new XSSFWorkbook(in);
        final Iterable<Sheet> sheetIter = () -> workbook.sheetIterator();
        for (Sheet s: sheetIter) {
            Section section = new Section(s.getSheetName());
            setRecords(context, section, s);
            sections.add(section);
        }
        return sections;
    }

    private void setRecords(Context context, Section section, Sheet sheet) {
        final Iterable<Row> iter = () -> sheet.iterator();
        String[] keys = null;
        for (Row r: iter) {
            if (keys == null) {
                keys = new String[KEYS.size()];
                for (int i=0; i<keys.length; i++) {
                    Cell c = r.getCell(i);
                    keys[i] = getCellString(c);
                    if (StringUtils.isEmpty(keys[i])) {
                        keys[i] = KEYS.get(i);
                    }
                }
            } else {
                Map<String, String> cols = new HashMap<>();
                for (int i = 0; i < keys.length; i++) {
                        Cell c = r.getCell(i);
                        cols.put(keys[i], getCellString(c));
                }
                section.getRecords().add(new Record(context, cols));
            }
        }
    }



    private String getCellString(Cell cell) {
        if (cell == null) {
            return "";
        }
        CellType type = cell.getCellType();
        if (type == CellType._NONE) {
            return "";
        } else if (type == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (type == CellType.BLANK) {
            return "";
        } else if (type == CellType.ERROR) {
            return "";
        } else if (type == CellType.BOOLEAN) {
            boolean value = cell.getBooleanCellValue();
            return Boolean.toString(value);
        } else if (type == CellType.FORMULA) {
            return cell.getCellFormula();
        } else if (type == CellType.NUMERIC) {
            double value = cell.getNumericCellValue();
            return Integer.toString((int)value);
        } else {
            return "";
        }
    }
}
