package org.javaopen.keydriver.report;

import com.spire.xls.Chart;
import com.spire.xls.ExcelChartType;
import com.spire.xls.ExcelVersion;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import com.spire.xls.charts.ChartSerie;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.javaopen.keydriver.driver.Context;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalTime;

public class ExcelWriter implements Writer {
    public static final String COMMA_FORMAT = "#,##0";
    public static final String TIMESTAMP_FORMAT_KEY =               "timestamp_format";
    public static final String OUTPUT_DIRECTORY_KEY =               "output_directory";
    public static final String OUTPUT_EXTENSION_KEY =               "output_extension";
    public static final String OUTPUT_PREFIX_KEY =                  "output_prefix";
    public static final String SUMMARY_SHEET_NAME_KEY =             "summary_sheet_name";
    public static final String ERROR_SHEET_PREFIX_KEY =             "error_sheet_prefix";
    public static final String TEST_FILE_NAME_LABEL_KEY =           "test_file_name_label";
    public static final String EXPECTING_TEST_COUNT_LABEL_KEY =     "expecting_test_count_label";
    public static final String EXPECTING_FAILURE_COUNT_LABEL_KEY =  "expecting_failure_count_label";
    public static final String EXECUTED_TEST_COUNT_LABEL_KEY =      "executed_test_count_label";
    public static final String SUCCESS_TEST_COUNT_LABEL_KEY =       "success_test_count_label";
    public static final String FAILED_TEST_COUNT_LABEL_KEY =        "failed_test_count_label";
    public static final String NOT_EXECUTED_TEST_COUNT_KEY =        "not_executed_test_count";
    public static final String START_TIME_LABEL_KEY =               "start_time_label";
    public static final String DURATION_LABEL_KEY =                 "duration_label";
    public static final String ARCH_LABEL_KEY =                     "arch_label";
    public static final String PROCESSOR_COUNT_LABEL_KEY =          "processor_count_label";
    public static final String LOAD_AVERAGE_LABEL_KEY =             "load_average_label";
    public static final String MAX_MEMORY_LABEL_KEY =               "max_memory_label";
    public static final String FREE_MEMORY_LABEL_KEY =              "free_memory_label";
    public static final String TOTAL_MEMORY_LABEL_KEY =             "total_memory_label";
    public static final String USABLE_DISK_LABEL_KEY =              "usable_disk_label";
    public static final String FREE_DISK_LABEL_KEY =                "free_disk_label";
    public static final String TOTAL_DISK_LABEL_KEY =               "total_disk_label";

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

        Workbook workbook = new Workbook();
        Worksheet sheet = workbook.getWorksheets().get(0);
        sheet.setName(summarySheetName);

        // set labels
        sheet.getCellRange("A1").setValue(testFileNameLabel);
        sheet.getCellRange("A2").setValue(expectingTestCountLabel);
        sheet.getCellRange("A3").setValue(expectingFailureCountLabel);
        sheet.getCellRange("A4").setValue(executedTestCountLabel);
        sheet.getCellRange("A5").setValue(successTestCountLabel);
        sheet.getCellRange("A6").setValue(failedTestCountLabel);
        sheet.getCellRange("A7").setValue(notExecutedTestCount);

        sheet.getCellRange("A9").setValue(startTimeLabel);
        sheet.getCellRange("A10").setValue(durationLabel);

        sheet.getCellRange("A12").setValue(archLabel);
        sheet.getCellRange("A13").setValue(processorCountLabel);
        sheet.getCellRange("A14").setValue(loadAverageLabel);
        sheet.getCellRange("A15").setValue(maxMemoryLabel);
        sheet.getCellRange("A16").setValue(freeMemoryLabel);
        sheet.getCellRange("A17").setValue(totalMemoryLabel);
        sheet.getCellRange("A18").setValue(usableDiskLabel);
        sheet.getCellRange("A19").setValue(freeDiskLabel);
        sheet.getCellRange("A20").setValue(totalDiskLabel);

        // set values
        sheet.getCellRange("B1").setValue(context.getInputFileName());
        sheet.getCellRange("B2").setNumberValue(report.getExpectingTestCount());
        sheet.getCellRange("B3").setNumberValue(report.getExpectingFailureCount());
        sheet.getCellRange("B4").setNumberValue(report.getExecutedTestCount());
        sheet.getCellRange("B5").setNumberValue(report.getSucceedTestCount());
        sheet.getCellRange("B6").setNumberValue(report.getFailedTestCount());
        sheet.getCellRange("B7").setNumberValue(report.getNotExecutedTestCount());

        if (report.getStartTime() != null) {
            sheet.getCellRange("B9").setNumberFormat(timestampFormat);
            double startTime = DateUtil.getExcelDate(report.getStartTime().toLocalDateTime());
            sheet.getCellRange("B9").setNumberValue(startTime);
        }
        if (report.getDuration() != null) {
            double duration = (double)(report.getDuration().toMillis() / 1000.0);
            sheet.getCellRange("B10").setNumberValue(duration);
        }

        sheet.getCellRange("B12").setValue(usage.getArch());
        sheet.getCellRange("B13").setNumberValue(usage.getProcessorCount());
        sheet.getCellRange("B14").setNumberValue(usage.getLoadAverage());
        sheet.getCellRange("B15").setNumberFormat(COMMA_FORMAT);
        sheet.getCellRange("B15").setNumberValue(usage.getMaxMemory());
        sheet.getCellRange("B16").setNumberFormat(COMMA_FORMAT);
        sheet.getCellRange("B16").setNumberValue(usage.getFreeMemory());
        sheet.getCellRange("B17").setNumberFormat(COMMA_FORMAT);
        sheet.getCellRange("B17").setNumberValue(usage.getTotalMemory());
        sheet.getCellRange("B18").setNumberFormat(COMMA_FORMAT);
        sheet.getCellRange("B18").setNumberValue(usage.getUsableDisk());
        sheet.getCellRange("B19").setNumberFormat(COMMA_FORMAT);
        sheet.getCellRange("B19").setNumberValue(usage.getFreeMemory());
        sheet.getCellRange("B20").setNumberFormat(COMMA_FORMAT);
        sheet.getCellRange("B20").setNumberValue(usage.getTotalDisk());

        sheet.getAllocatedRange().autoFitColumns();

        // chart
        chart(sheet, report, usage);

        // TODO error sheet

        Path path = Paths.get(outputDirectory, outputPrefix+baseName+"."+outputExtension);
        workbook.saveToFile(path.toFile().getAbsolutePath(), ExcelVersion.Version2007);
        workbook.dispose();
    }

    private void init(Context context) {
        timestampFormat =               context.getBundle().getString(TIMESTAMP_FORMAT_KEY);
        outputDirectory =               context.getBundle().getString(OUTPUT_DIRECTORY_KEY);
        outputExtension =               context.getBundle().getString(OUTPUT_EXTENSION_KEY);
        outputPrefix =                  context.getBundle().getString(OUTPUT_PREFIX_KEY);
        summarySheetName =              context.getBundle().getString(SUMMARY_SHEET_NAME_KEY);
        errorSheetPrefix =              context.getBundle().getString(ERROR_SHEET_PREFIX_KEY);
        testFileNameLabel =             context.getBundle().getString(TEST_FILE_NAME_LABEL_KEY);
        expectingTestCountLabel =       context.getBundle().getString(EXPECTING_TEST_COUNT_LABEL_KEY);
        expectingFailureCountLabel =    context.getBundle().getString(EXPECTING_FAILURE_COUNT_LABEL_KEY);
        executedTestCountLabel =        context.getBundle().getString(EXECUTED_TEST_COUNT_LABEL_KEY);
        successTestCountLabel =         context.getBundle().getString(SUCCESS_TEST_COUNT_LABEL_KEY);
        failedTestCountLabel =          context.getBundle().getString(FAILED_TEST_COUNT_LABEL_KEY);
        notExecutedTestCount =          context.getBundle().getString(NOT_EXECUTED_TEST_COUNT_KEY);
        startTimeLabel =                context.getBundle().getString(START_TIME_LABEL_KEY);
        durationLabel =                 context.getBundle().getString(DURATION_LABEL_KEY);
        archLabel =                     context.getBundle().getString(ARCH_LABEL_KEY);
        processorCountLabel =           context.getBundle().getString(PROCESSOR_COUNT_LABEL_KEY);
        loadAverageLabel =              context.getBundle().getString(LOAD_AVERAGE_LABEL_KEY);
        maxMemoryLabel =                context.getBundle().getString(MAX_MEMORY_LABEL_KEY);
        freeMemoryLabel =               context.getBundle().getString(FREE_MEMORY_LABEL_KEY);
        totalMemoryLabel =              context.getBundle().getString(TOTAL_MEMORY_LABEL_KEY);
        usableDiskLabel =               context.getBundle().getString(USABLE_DISK_LABEL_KEY);
        freeDiskLabel =                 context.getBundle().getString(FREE_DISK_LABEL_KEY);
        totalDiskLabel =                context.getBundle().getString(TOTAL_DISK_LABEL_KEY);
    }

    private void chart(Worksheet sheet, Report report, Usage usage) {
        // pie chart
        Chart chart = sheet.getCharts().add(ExcelChartType.Pie);

        // chart data range
        chart.setDataRange(sheet.getCellRange("B4:B6"));

        // chart rectangle
        chart.setLeftColumn(3);
        chart.setRightColumn(8);
        chart.setTopRow(2);
        chart.setBottomRow(8);

        // TODO chart labels
        ChartSerie cs = chart.getSeries().get(0);
        cs.setCategoryLabels(sheet.getCellRange("A4:A6"));
        cs.setValues(sheet.getCellRange("B4:B6"));
        cs.getDataPoints().getDefaultDataPoint().getDataLabels().hasValue(true);
        chart.getPlotArea().getFill().setVisible(true);

    }
}
