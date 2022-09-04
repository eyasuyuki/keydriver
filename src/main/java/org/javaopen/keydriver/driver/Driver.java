package org.javaopen.keydriver.driver;

import org.javaopen.keydriver.data.Matches;
import org.javaopen.keydriver.data.Param;
import org.javaopen.keydriver.data.Record;
import org.javaopen.keydriver.data.Section;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

public interface Driver {
    void perform(Context context, Section section, Record record);
    default boolean match(String value, Param param) throws NumberFormatException {
        if (param.getTag().equals(Matches.IS)) {
            return is(param.getValue()).matches(value);
        } else if (param.getTag().equals(Matches.IS_NOT)) {
            return not(param.getValue()).matches(value);
        } else if (param.getTag().equals(Matches.IS_NULL)) {
            return nullValue().matches(value);
        } else if (param.getTag().equals(Matches.IS_NOT_NULL)) {
            return notNullValue().matches(value);
        } else if (param.getTag().equals(Matches.GREATER_THAN)) {
            return greaterThan(Integer.parseInt(param.getValue())).matches(Integer.parseInt(value));
        } else if (param.getTag().equals(Matches.GREATER_THAN_EQUAL)) {
            return greaterThanOrEqualTo(Integer.parseInt(param.getValue())).matches(Integer.parseInt(value));
        } else if (param.getTag().equals(Matches.LESS_THAN)) {
            return lessThan(Integer.parseInt(param.getValue())).matches(Integer.parseInt(value));
        } else if (param.getTag().equals(Matches.LESS_THAN_EQUAL)) {
            return lessThanOrEqualTo(Integer.parseInt(param.getValue())).matches(Integer.parseInt(value));
        } else {
            throw new IllegalArgumentException(param.toString());
        }
    }

}
