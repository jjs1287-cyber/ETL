package org.etlforge.integration.server.db.lookup;

import java.sql.SQLException;

public interface DbExecute {
   DbExecuteResult execute(String var1, int var2) throws SQLException;

   void setAutoCommit(boolean var1) throws SQLException;
}
