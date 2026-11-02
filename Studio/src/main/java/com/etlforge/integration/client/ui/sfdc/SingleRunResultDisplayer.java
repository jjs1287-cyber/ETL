package com.etlforge.integration.client.ui.sfdc;

import com.google.common.base.Optional;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Window;
import javax.swing.JComponent;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.activity.client.files.StatusFileDownloaderFactory;
import org.etlforge.integration.activity.client.files.StatusHistoryApi;
import org.etlforge.integration.activity.client.log.LogStatus;
import org.etlforge.integration.activity.ui.files.StatusHistoryController;
import org.etlforge.integration.activity.ui.files.StatusHistoryModel;
import org.etlforge.integration.activity.ui.files.StatusHistoryView;
import org.etlforge.integration.activity.ui.monitor.OperationMonitorListener;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.util.string.StringLimiter;

public final class SingleRunResultDisplayer implements OperationMonitorListener {
   private final StatusHistoryApi statusApi;
   private volatile Optional<StatusFileDownloaderFactory> statusFileDownloaderFactory = Optional.absent();

   public SingleRunResultDisplayer(StatusHistoryApi var1) {
      this.statusApi = var1;
   }

   public void setStatusFileDownloaderFactory(StatusFileDownloaderFactory var1) {
      this.statusFileDownloaderFactory = Optional.of(var1);
   }

   @Override
   public void operationCompleted(String var1, long var2, Optional<LogStatus> var4) {
      if (this.isOfInterest(var4)) {
         this.openStatusView(var1, var2);
      }
   }

   private boolean isOfInterest(Optional<LogStatus> var1) {
      return var1.isPresent() && ((LogStatus)var1.get()).isSuccess();
   }

   private void openStatusView(final String var1, long var2) {
      final StatusHistoryModel var4 = new StatusHistoryModel(var2);
      final StatusHistoryController var5 = new StatusHistoryController(var4, this.statusApi);
      if (this.statusFileDownloaderFactory.isPresent()) {
         var5.enableRemoteFileSupport(this.statusFileDownloaderFactory.get());
      }

      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            StatusHistoryView var1x = new StatusHistoryView(var4, var5);
            Optional var2x = Optional.absent();
            WizardStyleDialog var3 = SingleRunResultDisplayer.this.createDialog(var1, var2x, var1x);
            var5.downloadHistory();
            var3.setVisible(true);
         }
      });
   }

   private WizardStyleDialog createDialog(String var1, Optional<Component> var2, StatusHistoryView var3) {
      JComponent var4 = WizardStyleDialog.wrapUi(var3);
      Window var5 = ApplicationUi.getWindowAncestor(var2);
      String var6 = "Result: " + StringLimiter.limit(var1, 50);
      WizardStyleDialog var7 = new WizardStyleDialog(var5, var6, var4, KongaDialog.CLOSE_ONLY, true);
      var7.manageLocation(SingleRunResultDisplayer.class);
      var7.setCloseButtonAsDefault();
      var3.setWaitService(var7);
      return var7;
   }
}
