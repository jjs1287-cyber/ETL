package org.etlforge.integration.client.database.ui.sqlbuilder;

import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.SelectionSource;

public interface DbObjectsView extends UiProvider, SelectionSource {
   DbObjectsModel getModel();

   void setDownloadingTables(boolean var1);

   void setDownloadingColumns(DatabaseObject var1, boolean var2);

   DbObjectsViewSelection getSelection();
}
