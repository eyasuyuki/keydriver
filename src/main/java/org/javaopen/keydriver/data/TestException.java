package org.javaopen.keydriver.data;

public class TestException extends RuntimeException {
    private TestCase testCase;

    public TestException(TestCase testCase) {
        setTestCase(testCase);
    }

    public TestException(String message, TestCase testCase) {
        super(message);
        setTestCase(testCase);
    }

    public TestException(Throwable cause, TestCase testCase) {
        super(cause);
        setTestCase(testCase);
    }

    public TestException(String message, Throwable cause, TestCase testCase) {
        super(message, cause);
        setTestCase(testCase);
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }
}
