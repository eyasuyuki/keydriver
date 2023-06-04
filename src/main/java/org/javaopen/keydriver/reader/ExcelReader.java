package org.javaopen.keydriver.reader;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.data.TestCase;
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
import static org.javaopen.keydriver.driver.Context.SECTION_KEY;

public class ExcelReader implements Reader {
    private static final Logger logger = Logger.getLogger(ExcelReader.class.getName());
    private static final int MAX_SHEETS = 1000;

    private XSSFWorkbook workbook;
    @Override
    public List<Section> read(Context context, String path) throws IOException {
        List<Section> sections = new ArrayList<>();
        final FileInputStream in = new FileInputStream(new File(path));
        workbook = new XSSFWorkbook(in);
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
                cols.put(SECTION_KEY, section.getName());
                for (int i = 0; i < keys.length; i++) {
                        Cell c = r.getCell(i);
                        cols.put(keys[i], getCellString(c));
                }
                section.getTestCaseList().add(new TestCase(context, cols));
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
            boolean isDate = false;
            try {
                isDate = DateUtil.isCellDateFormatted(cell);
            } catch (Throwable t) {
                //logger.severe(t.getLocalizedMessage());
            }
            CreationHelper helper = workbook.getCreationHelper();
            FormulaEvaluator evaluator = helper.createFormulaEvaluator();
            CellValue value = evaluator.evaluate(cell);
            String result = null;
            if (value.getCellType().equals(CellType.STRING)) {
                result = value.getStringValue();
            } else if (value.getCellType().equals(CellType.NUMERIC)) {
                if (isDate) {
                    DataFormatter formatter = new DataFormatter();
                    result = formatter.formatCellValue(cell, evaluator);
                } else {
                    result = Double.toString(value.getNumberValue());
                }
            } else if (value.getCellType().equals(CellType.BOOLEAN)) {
                result = Boolean.toString(value.getBooleanValue());
            }
            return result;
        } else if (type == CellType.NUMERIC) {
            final boolean isDate = DateUtil.isCellDateFormatted(cell);
            if (isDate) {
                DataFormatter formatter = new DataFormatter();
                return formatter.formatCellValue(cell);//TEST
            } else {
                double value = cell.getNumericCellValue();
                return Integer.toString((int) value);
            }
        } else {
            return "";
        }
    }
}
