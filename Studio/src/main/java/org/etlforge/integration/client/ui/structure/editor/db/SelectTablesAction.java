package org.etlforge.integration.client.ui.structure.editor.db;

import java.util.List;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;

final class SelectTablesAction extends AbstractSingleTypeSelectionAction<DatabaseObject> {
   private final TableSelectionModel model;

   public SelectTablesAction(TableSelectionModel var1) {
      super(DatabaseObject.class, null, UiResourceBundle.getIcon("RightArrow"));
      this.model = var1;
   }

   @Override
   protected void run(List<DatabaseObject> var1) {
      this.model.includeTables(var1);
   }

   protected boolean isEnabledFor(DatabaseObject var1) {
      return !this.model.isTableIncluded(var1);
   }
}
