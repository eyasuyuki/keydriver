package org.javaopen.keydriver.data;

import org.apache.commons.lang.RandomStringUtils;
import org.javaopen.keydriver.driver.Context;

import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class DummyTestFactory {
    static {
        Locale.setDefault(Locale.US);
    }
    private static final Context context = Context.getContext(null, null, null);
    private static final Random random = new Random();
    public static TestCase getDummy(Keyword keyword) {
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

    private static TestCase getDirective() {
        String num = Integer.toString(random.nextInt(20)+1);
        String keyword = RandomStringUtils.randomAlphanumeric(random.nextInt(20)+1);
        String target = RandomStringUtils.randomAlphanumeric(random.nextInt(20)+1);
        String sheetName = RandomStringUtils.randomAlphanumeric(random.nextInt(20)+1);
        return new TestCase(context, Stream.of(new String[][] {
                {"No", num},
                {"Keyword", keyword},
                {"Target", target},
                {"Object", "sheet["+sheetName+"]"},
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1])));
    }

    private static TestCase getOpen()  {
        String num = Integer.toString(random.nextInt(20)+1);
        String url = RandomStringUtils.randomAlphanumeric(random.nextInt(20)+1);
        return new TestCase(context, Stream.of(new String[][] {
                { "No", num },
                { "Keyword", "open" },
                { "Target", "url[https://"+url+"]" },
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1])));
    }

    private static TestCase getClick() {
        String num = Integer.toString(random.nextInt(20)+1);
        String target = RandomStringUtils.randomAlphanumeric(random.nextInt(10)+1);
        String buttonName = RandomStringUtils.randomAlphanumeric(random.nextInt(20)+1);
        return new TestCase(context, Stream.of(new String[][] {
                { "No", num },
                { "Keyword", "click" },
                { "Target", target },
                { "Object", "id["+buttonName+"]" },
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1])));
    }

    private static TestCase getSelect() {
        String num = Integer.toString(random.nextInt(20)+1);
        String target = RandomStringUtils.randomAlphanumeric(random.nextInt(10)+1);
        String argument = RandomStringUtils.randomAlphanumeric(random.nextInt(10)+1);
        String inputName = RandomStringUtils.randomAlphanumeric(random.nextInt(20)+1);
        return new TestCase(context, Stream.of(new String[][] {
                {"No", num},
                {"Keyword", "select"},
                {"Target", target},
                {"Argument", argument},
                {"Object", "id["+inputName+"]"},
                {"Option", argument},
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1])));
    }

    private static TestCase getInput() {
        String num = Integer.toString(random.nextInt(20)+1);
        String target = RandomStringUtils.randomAlphanumeric(random.nextInt(10)+1);
        String argument = RandomStringUtils.randomAlphanumeric(random.nextInt(10)+1);
        String inputName = RandomStringUtils.randomAlphanumeric(random.nextInt(20)+1);
        return new TestCase(context, Stream.of(new String[][] {
                { "No", num },
                { "Keyword", "input" },
                { "Target", target },
                { "Argument", argument },
                { "Object", "name["+inputName+"]" },
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1])));
    }

    private static TestCase getClear() {
        String num = Integer.toString(random.nextInt(20)+1);
        String target = RandomStringUtils.randomAlphanumeric(random.nextInt(10)+1);
        String inputName = RandomStringUtils.randomAlphanumeric(random.nextInt(20)+1);
        return new TestCase(context, Stream.of(new String[][] {
                { "No", num },
                { "Keyword", "clear" },
                { "Target", target },
                { "Object", "id["+inputName+"]" },
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1])));
    }

    private static TestCase getAccept() {
        String num = Integer.toString(random.nextInt(20)+1);
        String comment = RandomStringUtils.randomAlphanumeric(random.nextInt(20)+1);
        return new TestCase(context, Stream.of(new String[][] {
                { "No", num },
                { "Keyword", "accept" },
                { "Comment", comment},
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1])));
    }

    private static TestCase getDismiss() {
        String num = Integer.toString(random.nextInt(20)+1);
        String comment = RandomStringUtils.randomAlphanumeric(random.nextInt(20)+1);
        return new TestCase(context, Stream.of(new String[][] {
                { "No", num },
                { "Keyword", "dismiss" },
                { "Comment", comment},
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1])));
    }

    private static TestCase getCapture() {
        String num = Integer.toString(random.nextInt(20)+1);
        String comment = RandomStringUtils.randomAlphanumeric(random.nextInt(20)+1);
        return new TestCase(context, Stream.of(new String[][] {
                { "No", num },
                { "Keyword", "capture" },
                { "Comment", comment},
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1])));
    }

    private static TestCase getAssert() {
        String num = Integer.toString(random.nextInt(20)+1);
        String target = RandomStringUtils.randomAlphanumeric(random.nextInt(20)+1);
        String argument = Boolean.toString(random.nextBoolean());
        String elementName = RandomStringUtils.randomAlphanumeric(random.nextInt(20)+1);
        String attribute = RandomStringUtils.randomAlphanumeric(random.nextInt(10)+1);
        return new TestCase(context, Stream.of(new String[][] {
                {"No", num},
                {"Keyword", "assert"},
                {"Target", target},
                {"Argument", "is["+argument+"]"},
                {"Object", "id["+elementName+"#"+attribute+"]"}
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1])));
    }

    private static TestCase getExecute() {
        String num = Integer.toString(random.nextInt(20)+1);
        String target = RandomStringUtils.randomAlphanumeric(random.nextInt(20)+1);
        String argument = RandomStringUtils.randomAlphanumeric(random.nextInt(60)+1);
        String sql = Integer.toString(random.nextInt(20)+1);
        String url = RandomStringUtils.randomAlphanumeric(random.nextInt(20)+1);
        return new TestCase(context, Stream.of(new String[][] {
                {"No", num},
                {"Keyword", "execute"},
                {"Target", target},
                {"Argument", argument},
                {"Object", "sql["+sql+"]"},
                {"Option", "url["+url+"]"}
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1])));
    }

    private static TestCase getUpload() {
        String num = Integer.toString(random.nextInt(20)+1);
        String target = RandomStringUtils.randomAlphanumeric(random.nextInt(20)+1);
        String argument = RandomStringUtils.randomAlphanumeric(random.nextInt(60)+1);
        String elementName = RandomStringUtils.randomAlphanumeric(random.nextInt(20)+1);
        String attribute = RandomStringUtils.randomAlphanumeric(random.nextInt(10)+1);
        return new TestCase(context, Stream.of(new String[][] {
                {"No", num},
                {"Keyword", "upload"},
                {"Target", target},
                {"Argument", argument},
                {"Object", "id["+elementName+"#"+attribute+"]"}
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1])));
    }

    @org.junit.Test
    public void testGetDirective() {
        TestCase directive = getDirective();
        assertThat(directive, is(not(nullValue())));
        assertThat(directive.getKeyword(), is(Keyword._DIRECTIVE));
        assertThat(directive.getObject(), is(not(nullValue())));
        assertThat(directive.getObject().getTag(), is(DataType.SHEET));
    }
}
