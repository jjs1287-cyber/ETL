package org.etlforge.integration.client.ui.structure.editor.db;

import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.database.info.DatabaseObject;

public interface TransformationDatabaseInfo {
   DatabaseStructure getStructure();

   DatabaseObject[] getDatabaseObjects();
}
