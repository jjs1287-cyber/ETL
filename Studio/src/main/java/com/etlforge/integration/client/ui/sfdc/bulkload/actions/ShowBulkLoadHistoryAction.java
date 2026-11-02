package com.etlforge.integration.client.ui.sfdc.bulkload.actions;

import com.google.common.base.Optional;
import com.etlforge.integration.client.bulkload.serverapi.BulkLoadInfoProvider;
import com.etlforge.integration.client.ui.sfdc.bulkload.history.BulkLoadHistoryController;
import com.etlforge.integration.client.ui.sfdc.bulkload.history.BulkLoadHistoryDialog;
import com.etlforge.integration.client.ui.sfdc.bulkload.history.BulkLoadHistoryModel;
import com.etlforge.integration.client.ui.sfdc.bulkload.history.BulkLoadHistoryView;
import java.awt.EventQueue;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.activity.client.files.StatusFileDownloaderFactory;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.ui.util.UiUtils;

public final class ShowBulkLoadHistoryAction extends AbstractSingleActiveObjectAction<SalesforceBulkLoad> {
   public static final String ID = "action.ic.BulkLoadHistory";
   private final BulkLoadInfoProvider backend;
   private Optional<StatusFileDownloaderFactory> statusFileDownloaderFactory = Optional.absent();

   public ShowBulkLoadHistoryAction(ActiveObjectService var1, BulkLoadInfoProvider var2) {
      super(var1, "action.ic.BulkLoadHistory", BulkLoadActionDictionary.DICTIONARY, SalesforceBulkLoad.class);
      this.backend = var2;
   }

   public void setStatusFileDownloaderFactory(StatusFileDownloaderFactory var1) {
      this.statusFileDownloaderFactory = Optional.of(var1);
   }

   protected void runOnObject(final SalesforceBulkLoad var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ShowBulkLoadHistoryAction.this.openHistory(var1);
         }
      });
   }

   private void openHistory(SalesforceBulkLoad var1) {
      BulkLoadHistoryModel var2 = new BulkLoadHistoryModel(var1);
      BulkLoadHistoryController var3 = new BulkLoadHistoryController(var2, this.backend);
      if (this.statusFileDownloaderFactory.isPresent()) {
         var3.enableDownloadOfRemoteFiles(this.statusFileDownloaderFactory.get());
      }

      BulkLoadHistoryView var4 = new BulkLoadHistoryView(var2, var3);
      BulkLoadHistoryDialog var5 = new BulkLoadHistoryDialog(var4, var3);
      var5.open(UiUtils.getActiveWindow(), false);
   }
}
