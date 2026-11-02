package org.etlforge.integration.client.ui.structure.editor.db;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.ui.wait.WaitService;

class RefreshColumnInformationAction extends AbstractAction {
   private final TableSelectionModel model;
   private final WaitService waitService;

   public RefreshColumnInformationAction(TableSelectionModel var1, WaitService var2) {
      this.model = var1;
      this.waitService = var2;
      ActionDecorators.REFRESH.decorate(this);
      this.putValue("Name", "Refresh Column Information");
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      RefreshColumnInformationAction.ColumnInfoDownloadWorker var2 = new RefreshColumnInformationAction.ColumnInfoDownloadWorker(this.model, this.waitService);
      var2.execute();
   }

   private static class ColumnInfoDownloadWorker extends KongaSwingWorker<DatabaseObject[], Void> {
      private final TableSelectionModel model;
      private final WaitService waitService;
      private volatile SelectedTables selected;

      public ColumnInfoDownloadWorker(TableSelectionModel var1, WaitService var2) {
         this.model = var1;
         this.waitService = var2;
      }

      protected DatabaseObject[] doInBackground() throws Exception {
         this.selected = this.model.getUsageCount();
         DownloadColumnInfoJob var1 = new DownloadColumnInfoJob(this.model, this.selected.getIncludedTables());
         var1.submit(this.waitService, 0);
         this.model.setCurrentServerInteraction(var1);
         return var1.getDownloadedTables();
      }

      protected void onSuccess(DatabaseObject[] var1) {
         if (var1 != null) {
            this.selected.replaceTableInstances(var1);
            this.model.setUsageCount(this.selected);
         }
      }
   }
}
