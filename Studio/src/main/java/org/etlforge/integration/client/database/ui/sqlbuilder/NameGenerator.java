package org.etlforge.integration.client.database.ui.sqlbuilder;

import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;

public interface NameGenerator {
   String getTableName(DatabaseObject var1);

   String getColumnName(DatabaseColumn var1);
}
