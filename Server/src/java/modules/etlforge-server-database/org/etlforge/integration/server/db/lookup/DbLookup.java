package org.etlforge.integration.server.db.lookup;

import java.sql.SQLException;

public interface DbLookup {
   DbLookupResult lookup(String var1) throws SQLException;

   void setAutoCommit(boolean var1) throws SQLException;
}
