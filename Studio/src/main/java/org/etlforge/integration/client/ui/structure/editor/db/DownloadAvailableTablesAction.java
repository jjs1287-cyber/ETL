package org.etlforge.integration.client.ui.structure.editor.db;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.Application;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseObjectContainer;
import org.etlforge.integration.database.info.retriever.TableSearchFilter;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.wait.WaitService;

final class DownloadAvailableTablesAction extends AbstractAction {
   private final TableSelectionModel model;
   private final WaitService waitService;

   public DownloadAvailableTablesAction(TableSelectionModel var1, WaitService var2) {
      super(Strings.get("Db.DownloadTables.Name"));
      this.model = var1;
      this.waitService = var2;
      this.putValue("ShortDescription", Strings.get("Db.DownloadTables.ToolTip"));
      this.putValue("AcceleratorKey", KeyUtils.REFRESH);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      if (this.isReadyForDownload()) {
         this.submitDownloadJob();
      }
   }

   private boolean isReadyForDownload() {
      TableSearchFilter var1 = this.model.getSearchFilter();
      if (var1.getObjectTypes().isEmpty()) {
         Alert.error("You must select at least one of Table and View.", "Invalid Filter Parameters");
         return false;
      } else {
         return true;
      }
   }

   private void submitDownloadJob() {
      Application.getWorker().submitForParallel(new ApplicationWorker.SafeRunnable() {
         @Override
         public void run() {
            DownloadAvailableObjectsJob var1 = new DownloadAvailableObjectsJob(DownloadAvailableTablesAction.this.model);
            var1.setFilter(DownloadAvailableTablesAction.this.model.getSearchFilter());
            var1.submit(DownloadAvailableTablesAction.this.waitService, 0);
            DownloadAvailableTablesAction.this.model.setCurrentServerInteraction(var1);
            DatabaseObjectContainer[] var2;
            if (var1.isSuccess()) {
               var2 = var1.getDownloadedObjects();
               BeginEndQuote var3 = var1.getBeginEndQuote();
               DownloadAvailableTablesAction.this.model.getDbLocation().setDatabaseBeginEndQuote(var3);
            } else {
               var2 = new DatabaseObjectContainer[0];
            }

            DownloadAvailableTablesAction.this.model.setAvailableObjects(var2);
            DownloadAvailableTablesAction.this.model.setCurrentServerInteraction(null);
         }
      });
   }
}
