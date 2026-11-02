package org.etlforge.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetConverter {
   Object convert(ResultSet var1) throws SQLException;
}
