package org.etlforge.integration.client.ui.structure.editor.db;

import org.etlforge.integration.database.info.DatabaseObject;

final class SelectedTablesPreparer {
   private final DatabaseTableSelectionPage page;
   private final TableSelectionModel model;

   public SelectedTablesPreparer(DatabaseTableSelectionPage var1, TableSelectionModel var2) {
      this.page = var1;
      this.model = var2;
   }

   public boolean prepare() {
      SelectedTables var1 = this.model.getUsageCount();
      if (var1.isEmpty()) {
         return false;
      }

      DatabaseObject[] var2 = this.downloadTableInfo(var1);
      if (var2 == null) {
         return false;
      }

      var1.replaceTableInstances(var2);
      this.model.setUsageCount(var1);
      return true;
   }

   private DatabaseObject[] downloadTableInfo(SelectedTables var1) {
      DatabaseObject[] var2 = var1.getTablesWithoutColumnInformation();
      if (var2.length == 0) {
         return var2;
      }

      DownloadColumnInfoJob var3 = new DownloadColumnInfoJob(this.model, var2);
      var3.submit(this.page.getWaitService(), 0);
      this.model.setCurrentServerInteraction(var3);
      DatabaseObject[] var4 = var3.getDownloadedTables();
      this.model.setCurrentServerInteraction(null);
      return var4;
   }
}
