package org.javaopen.keydriver.report;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.javaopen.keydriver.driver.Context;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExcelWriter implements Writer {
    public static final String COMMA_FORMAT = "#,##0";

    private String timestampFormat;
    private String outputDirectory;
    private String outputExtension;
    private String outputPrefix;
    private String summarySheetName;
    private String errorSheetPrefix;
    private String testFileNameLabel;
    private String expectingTestCountLabel;
    private String expectingFailureCountLabel;
    private String executedTestCountLabel;
    private String successTestCountLabel;
    private String failedTestCountLabel;
    private String notExecutedTestCount;
    private String startTimeLabel;
    private String durationLabel;
    private String archLabel;
    private String processorCountLabel;
    private String loadAverageLabel;
    private String maxMemoryLabel;
    private String freeMemoryLabel;
    private String totalMemoryLabel;
    private String usableDiskLabel;
    private String freeDiskLabel;
    private String totalDiskLabel;

    @Override
    public void write(Context context, Report report, Usage usage) {
        init(context);
        String baseName = FilenameUtils.getBaseName(context.getInputFileName());

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(summarySheetName);

        Row[] r = new Row[20];
        Cell[] a = new Cell[20];
        Cell[] b = new Cell[20];
        for (int i=0; i<r.length; i++) {
            r[i] = sheet.createRow(i);
            a[i] = r[i].createCell(0);
            b[i] = r[i].createCell(1);
        }

        // set labels
        a[0].setCellValue(testFileNameLabel);
        a[1].setCellValue(expectingTestCountLabel);
        a[2].setCellValue(expectingFailureCountLabel);
        a[3].setCellValue(executedTestCountLabel);
        a[4].setCellValue(successTestCountLabel);
        a[5].setCellValue(failedTestCountLabel);
        a[6].setCellValue(notExecutedTestCount);

        a[8].setCellValue(startTimeLabel);
        a[9].setCellValue(durationLabel);

        a[11].setCellValue(archLabel);
        a[12].setCellValue(processorCountLabel);
        a[13].setCellValue(loadAverageLabel);
        a[14].setCellValue(maxMemoryLabel);
        a[15].setCellValue(freeMemoryLabel);
        a[16].setCellValue(totalMemoryLabel);
        a[17].setCellValue(usableDiskLabel);
        a[18].setCellValue(freeDiskLabel);
        a[19].setCellValue(totalDiskLabel);

        // set values
        b[0].setCellValue(context.getInputFileName());
        b[1].setCellValue(report.getExpectingTestCount());
        b[2].setCellValue(report.getExpectingFailureCount());
        b[3].setCellValue(report.getExecutedTestCount());
        b[4].setCellValue(report.getSucceedTestCount());
        b[5].setCellValue(report.getFailedTestCount());
        b[6].setCellValue(report.getNotExecutedTestCount());

        CreationHelper helper = workbook.getCreationHelper();
        if (report.getStartTime() != null) {
            CellStyle cellStyle = workbook.createCellStyle();
            short style = helper.createDataFormat().getFormat(timestampFormat);
            cellStyle.setDataFormat(style);
            b[8].setCellStyle(cellStyle);
            double startTime = DateUtil.getExcelDate(report.getStartTime().toLocalDateTime());
            b[8].setCellValue(startTime);
        }
        if (report.getDuration() != null) {
            double duration = report.getDuration().toMillis() / 1000.0;
            b[9].setCellValue(duration);
        }

        CellStyle commaStyle = workbook.createCellStyle();
        short comma = helper.createDataFormat().getFormat(COMMA_FORMAT);
        commaStyle.setDataFormat(comma);
        b[11].setCellValue(usage.getArch());
        b[12].setCellValue(usage.getProcessorCount());
        b[13].setCellValue(usage.getLoadAverage());
        b[14].setCellStyle(commaStyle);
        b[14].setCellValue(usage.getMaxMemory());
        b[15].setCellStyle(commaStyle);
        b[15].setCellValue(usage.getFreeMemory());
        b[16].setCellStyle(commaStyle);
        b[16].setCellValue(usage.getTotalMemory());
        b[17].setCellStyle(commaStyle);
        b[17].setCellValue(usage.getUsableDisk());
        b[18].setCellStyle(commaStyle);
        b[18].setCellValue(usage.getFreeMemory());
        b[19].setCellStyle(commaStyle);
        b[19].setCellValue(usage.getTotalDisk());

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

        // chart
        chart(sheet, report, usage);

        // TODO error sheet

        Path path = Paths.get(outputDirectory, outputPrefix+baseName+"."+outputExtension);
        try (FileOutputStream out = new FileOutputStream(path.toFile())) {
            workbook.write(out);;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);//TODO
        } catch (IOException e) {
            throw new RuntimeException(e);//TODO
        } finally {
            //workbook.close();
        }
    }

    private void init(Context context) {
        timestampFormat =               context.getConfig().getString(Context.TIMESTAMP_FORMAT_KEY);
        outputDirectory =               context.getConfig().getString(Context.OUTPUT_DIRECTORY_KEY);
        outputExtension =               context.getConfig().getString(Context.OUTPUT_EXTENSION_KEY);
        outputPrefix =                  context.getConfig().getString(Context.OUTPUT_PREFIX_KEY);
        summarySheetName =              context.getConfig().getString(Context.SUMMARY_SHEET_NAME_KEY);
        errorSheetPrefix =              context.getConfig().getString(Context.ERROR_SHEET_PREFIX_KEY);
        testFileNameLabel =             context.getConfig().getString(Context.TEST_FILE_NAME_LABEL_KEY);
        expectingTestCountLabel =       context.getConfig().getString(Context.EXPECTING_TEST_COUNT_LABEL_KEY);
        expectingFailureCountLabel =    context.getConfig().getString(Context.EXPECTING_FAILURE_COUNT_LABEL_KEY);
        executedTestCountLabel =        context.getConfig().getString(Context.EXECUTED_TEST_COUNT_LABEL_KEY);
        successTestCountLabel =         context.getConfig().getString(Context.SUCCESS_TEST_COUNT_LABEL_KEY);
        failedTestCountLabel =          context.getConfig().getString(Context.FAILED_TEST_COUNT_LABEL_KEY);
        notExecutedTestCount =          context.getConfig().getString(Context.NOT_EXECUTED_TEST_COUNT_KEY);
        startTimeLabel =                context.getConfig().getString(Context.START_TIME_LABEL_KEY);
        durationLabel =                 context.getConfig().getString(Context.DURATION_LABEL_KEY);
        archLabel =                     context.getConfig().getString(Context.ARCH_LABEL_KEY);
        processorCountLabel =           context.getConfig().getString(Context.PROCESSOR_COUNT_LABEL_KEY);
        loadAverageLabel =              context.getConfig().getString(Context.LOAD_AVERAGE_LABEL_KEY);
        maxMemoryLabel =                context.getConfig().getString(Context.MAX_MEMORY_LABEL_KEY);
        freeMemoryLabel =               context.getConfig().getString(Context.FREE_MEMORY_LABEL_KEY);
        totalMemoryLabel =              context.getConfig().getString(Context.TOTAL_MEMORY_LABEL_KEY);
        usableDiskLabel =               context.getConfig().getString(Context.USABLE_DISK_LABEL_KEY);
        freeDiskLabel =                 context.getConfig().getString(Context.FREE_DISK_LABEL_KEY);
        totalDiskLabel =                context.getConfig().getString(Context.TOTAL_DISK_LABEL_KEY);
    }

    private void chart(XSSFSheet sheet, Report report, Usage usage) {
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 2, 0, 7, 7);

        XSSFChart chart = drawing.createChart(anchor);

        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);

        XDDFDataSource<String> labels = XDDFDataSourcesFactory.fromStringCellRange(sheet,
                new CellRangeAddress(4, 6, 0, 0));

        XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
                new CellRangeAddress(4, 6, 1, 1));

        XDDFChartData data = chart.createData(ChartTypes.PIE, null, null);

        data.setVaryColors(true);
        data.addSeries(labels, values);

        // Add data labels
        if (!chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).isSetDLbls()) {
            chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).addNewDLbls();
        }
        chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).getDLbls().addNewShowVal().setVal(true);
        chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).getDLbls().addNewShowSerName().setVal(false);
        chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).getDLbls().addNewShowCatName().setVal(false);
        chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).getDLbls().addNewShowPercent().setVal(false);
        chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).getDLbls().addNewShowLegendKey().setVal(false);

        chart.plot(data);
    }
}
