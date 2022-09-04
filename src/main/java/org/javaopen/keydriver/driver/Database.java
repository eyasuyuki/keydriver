package org.javaopen.keydriver.driver;

import org.apache.commons.lang.StringUtils;
import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.Record;
import org.javaopen.keydriver.data.Section;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class Database implements Driver {
    private static final Logger logger = Logger.getLogger(Database.class.getName());
    @Override
    public void perform(Context context, Section section, Record record) {
        try (Connection conn = DriverManager.getConnection(record.getOption().getValue())) {
            Statement st = conn.createStatement();
            String sql = record.getObject().getValue();
            if (StringUtils.isEmpty(sql)) {
                sql = record.getArgument().getValue();
            }
            if (record.getKeyword().equals(Keyword.ASSERT)) {
                ResultSet res = st.executeQuery(sql);
                String value = res.getString(0);
                if (!match(value, record.getArgument())) {
                    logger.severe("Section: "+section.getName()+", Test: "+record.getComment()+" failed: expected: "+record.getArgument().getValue()+", but got: "+value);
                }
            } else if (record.getKeyword().equals(Keyword.EXECUTE)) {
                st.execute(sql);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
