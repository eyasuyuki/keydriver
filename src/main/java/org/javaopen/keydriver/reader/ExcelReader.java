package org.javaopen.keydriver.reader;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.javaopen.keydriver.data.Record;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ExcelReader implements Reader {
    private static final Logger logger = Logger.getLogger(ExcelReader.class.getName());

    @Override
    public List<Record> read(String path) throws IOException {
        final FileInputStream in = new FileInputStream(new File(path));
        final XSSFWorkbook workbook = new XSSFWorkbook(in);
        final XSSFSheet sheet0 = workbook.getSheetAt(0);// TODO multi sheets
        final Iterable<Row> iter = () -> sheet0.iterator();
        String[] keys = new String[Record.KEYS.size()];
        final List<Record> rows = new ArrayList<>();
        for (Row r: iter) {
            if (keys == null) {
                for (int i=0; i<keys.length; i++) {
                    Cell c = r.cellIterator().next();
                    keys[i] = getString(c);
                    if (StringUtils.isEmpty(keys[i])) {
                        keys[i] = Record.KEYS.get(i);
                    }
                }
            } else {
                Map<String, String> cols = new HashMap<>();
                for (int i = 0; i < keys.length; i++) {
                    Cell c = r.cellIterator().next();
                    cols.put(keys[i], getString(c));
                }
                rows.add(new Record(cols));
            }
        }
        return rows;
    }

    private String getString(Cell cell) {
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
