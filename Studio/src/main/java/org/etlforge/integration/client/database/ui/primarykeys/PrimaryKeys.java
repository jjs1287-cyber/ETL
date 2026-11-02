package org.etlforge.integration.client.database.ui.primarykeys;

import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;

public interface PrimaryKeys {
   DatabaseObject getDatabaseObject();

   DatabaseColumn[] getPrimaryKeys();
}
