package org.etlforge.integration.client.database.ui.sqlbuilder;

import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.retriever.TableSearchFilter;

public interface DbObjectsViewFilter {
   boolean acceptsTable(DatabaseObject var1);

   boolean acceptsColumn(DatabaseColumn var1);

   TableSearchFilter asInfoProviderFilter();
}
