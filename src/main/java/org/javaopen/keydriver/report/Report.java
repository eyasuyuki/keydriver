package org.javaopen.keydriver.report;

public interface Report {
    int getExpectingTestCount();//予定テスト数
    int getExecutedTestCount();//実行されたテスト数
    int getSucceedTestCount();//成功テスト数
    int getFailedTestCount();//失敗テスト数
    int getExpectingFailureCount();//予定失敗数
    int getUncompletedTestCount();//未完了テスト数
    String getResourceInformation();//マシン利用状況、ディスク容量等
    long getExpectingTime(); //予定時間
    long getDuration();//実行時間
}
