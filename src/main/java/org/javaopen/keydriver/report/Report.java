package org.javaopen.keydriver.report;

import java.sql.Timestamp;

public interface Report {
    int getExpectingTestCount();//予定テスト数
    int getExecutedTestCount();//実行されたテスト数
    int getSucceedTestCount();//成功テスト数
    int getFailedTestCount();//失敗テスト数
    int getExpectingFailureCount();//予定失敗数
    int getUncompletedTestCount();//未完了テスト数
    Timestamp getStartTime();// 開始時刻
    long getExpectingTime(); //予定時間
    long getDuration();//実行時間
}
