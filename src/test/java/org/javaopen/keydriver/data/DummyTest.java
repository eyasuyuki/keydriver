package org.javaopen.keydriver.data;

public class DummyTest {
    public Test getDummy(Keyword keyword) {
        switch(keyword) {
            case OPEN:
                return getOpen();
            case CLICK:
                return getClick();
            case SELECT:
                return getSelect();
            case INPUT:
                return getInput();
            case CLEAR:
                return getClear();
            case ACCEPT:
                return getAccept();
            case DISMISS:
                return getDismiss();
            case CAPTURE:
                return getCapture();
            case ASSERT:
                return getAssert();
            case EXECUTE:
                return getExecute();
            case UPLOAD:
                return getUpload();
            default: // _DIRECTIVE
                return getDirective();
        }
    }

    private Test getDirective() {
        return null;
    }

    private Test getOpen() {
        return null;
    }

    private Test getClick() {
        return null;
    }

    private Test getSelect() {
        return null;
    }

    private Test getInput() {
        return null;
    }

    private Test getClear() {
        return null;
    }

    private Test getAccept() {
        return null;
    }

    private Test getDismiss() {
        return null;
    }

    private Test getCapture() {
        return null;
    }

    private Test getAssert() {
        return null;
    }

    private Test getExecute() {
        return null;
    }

    private Test getUpload() {
        return null;
    }
}
