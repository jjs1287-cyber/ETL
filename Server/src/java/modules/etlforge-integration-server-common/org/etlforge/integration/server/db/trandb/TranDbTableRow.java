package org.etlforge.integration.server.db.trandb;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface TranDbTableRow {
   void populate(ResultSet var1) throws SQLException;
}
