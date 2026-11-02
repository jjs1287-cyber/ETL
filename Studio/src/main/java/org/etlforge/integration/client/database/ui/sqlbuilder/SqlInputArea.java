package org.etlforge.integration.client.database.ui.sqlbuilder;

import java.util.List;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.ui.UiProvider;

public interface SqlInputArea extends UiProvider {
   void setNameGenerator(NameGenerator var1);

   void insertTable(DatabaseObject var1);

   void insertColumns(List<DatabaseColumn> var1);
}
