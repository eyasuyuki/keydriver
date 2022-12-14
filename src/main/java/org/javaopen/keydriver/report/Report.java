package org.javaopen.keydriver.report;

import java.sql.Timestamp;
import java.time.Duration;

public interface Report {
    int getExpectingTestCount();//予定テスト数
    int getExecutedTestCount();//実行されたテスト数
    int getSucceedTestCount();//成功テスト数
    int getFailedTestCount();//失敗テスト数
    int getExpectingFailureCount();//予定失敗数
    int getNotExecutedTestCount();//未実行テスト数
    Timestamp getStartTime();// 開始時刻
    Duration getDuration();//実行時間
}
