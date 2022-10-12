package org.javaopen.keydriver.report;

import com.spire.xls.Chart;
import com.spire.xls.ExcelChartType;
import com.spire.xls.ExcelVersion;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import com.spire.xls.charts.ChartSerie;
import org.apache.commons.io.FilenameUtils;
import org.javaopen.keydriver.driver.Context;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ExcelWriter implements Writer {
    public static final String OUTPUT_DIRECTORY_KEY =               "output_directory";
    public static final String OUTPUT_EXTENSION_KEY =               "output_extension";
    public static final String OUTPUT_PREFIX_KEY =                  "output_prefix";
    public static final String SUMMARY_SHEET_NAME_KEY =             "summary_sheet_name";
    public static final String ERROR_SHEET_PREFIX_KEY =             "error_sheet_prefix";
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

    private String outputDirectory;
    private String outputExtension;
    private String outputPrefix;
    private String summarySheetName;
    private String errorSheetPrefix;
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
        sheet.getCellRange("A1").setValue(expectingTestCountLabel);
        sheet.getCellRange("A2").setValue(expectingFailureCountLabel);
        sheet.getCellRange("A3").setValue(executedTestCountLabel);
        sheet.getCellRange("A4").setValue(successTestCountLabel);
        sheet.getCellRange("A5").setValue(failedTestCountLabel);
        sheet.getCellRange("A6").setValue(notExecutedTestCount);

        sheet.getCellRange("A8").setValue(startTimeLabel);
        sheet.getCellRange("A9").setValue(durationLabel);

        sheet.getCellRange("A11").setValue(archLabel);
        sheet.getCellRange("A12").setValue(processorCountLabel);
        sheet.getCellRange("A13").setValue(loadAverageLabel);
        sheet.getCellRange("A14").setValue(maxMemoryLabel);
        sheet.getCellRange("A15").setValue(freeMemoryLabel);
        sheet.getCellRange("A16").setValue(totalMemoryLabel);
        sheet.getCellRange("A17").setValue(usableDiskLabel);
        sheet.getCellRange("A18").setValue(freeDiskLabel);
        sheet.getCellRange("A19").setValue(totalDiskLabel);

        // set values
        sheet.getCellRange("B1").setNumberValue(report.getExpectingTestCount());
        sheet.getCellRange("B2").setNumberValue(report.getExpectingFailureCount());
        sheet.getCellRange("B3").setNumberValue(report.getExecutedTestCount());
        sheet.getCellRange("B4").setNumberValue(report.getSucceedTestCount());
        sheet.getCellRange("B5").setNumberValue(report.getFailedTestCount());
        sheet.getCellRange("B6").setNumberValue(report.getNotExecutedTestCount());

        if (report.getStartTime() != null) {
            sheet.getCellRange("B8").setValue(report.getStartTime().toString());//TODO format
        }
        if (report.getDuration() != null) {
            sheet.getCellRange("B9").setValue(report.getDuration().toString());//TODO format
        }

        sheet.getCellRange("B11").setValue(usage.getArch());
        sheet.getCellRange("B12").setNumberValue(usage.getProcessorCount());
        sheet.getCellRange("B13").setNumberValue(usage.getLoadAverage());
        sheet.getCellRange("B14").setNumberValue(usage.getMaxMemory());
        sheet.getCellRange("B15").setNumberValue(usage.getFreeMemory());
        sheet.getCellRange("B16").setNumberValue(usage.getTotalMemory());
        sheet.getCellRange("B17").setNumberValue(usage.getUsableDisk());
        sheet.getCellRange("B18").setNumberValue(usage.getFreeMemory());
        sheet.getCellRange("B19").setNumberValue(usage.getTotalDisk());

        sheet.getAllocatedRange().autoFitColumns();

        // chart
        chart(sheet, report, usage);

        // TODO error sheet

        Path path = Paths.get(outputDirectory, outputPrefix+baseName+"."+outputExtension);
        workbook.saveToFile(path.toFile().getAbsolutePath(), ExcelVersion.Version2007);
        workbook.dispose();
    }

    private void init(Context context) {
        outputDirectory =               context.getBundle().getString(OUTPUT_DIRECTORY_KEY);     
        outputExtension =               context.getBundle().getString(OUTPUT_EXTENSION_KEY);
        outputPrefix =                  context.getBundle().getString(OUTPUT_PREFIX_KEY);
        summarySheetName =              context.getBundle().getString(SUMMARY_SHEET_NAME_KEY);
        errorSheetPrefix =              context.getBundle().getString(ERROR_SHEET_PREFIX_KEY);
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
        chart.setTopRow(1);
        chart.setBottomRow(7);

        // TODO chart labels
        ChartSerie cs = chart.getSeries().get(0);
        cs.setCategoryLabels(sheet.getCellRange("A4:A6"));
        cs.setValues(sheet.getCellRange("B4:B6"));
        cs.getDataPoints().getDefaultDataPoint().getDataLabels().hasValue(true);
        chart.getPlotArea().getFill().setVisible(true);

    }
}
