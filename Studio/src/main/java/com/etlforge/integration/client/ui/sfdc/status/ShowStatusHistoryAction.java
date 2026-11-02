package com.etlforge.integration.client.ui.sfdc.status;

import com.google.common.base.Optional;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.activity.client.files.StatusFileDownloaderFactory;
import org.etlforge.integration.activity.client.files.StatusHistoryApi;
import org.etlforge.integration.activity.ui.files.StatusHistoryController;
import org.etlforge.integration.activity.ui.files.StatusHistoryModel;
import org.etlforge.integration.activity.ui.files.StatusHistoryView;
import org.etlforge.integration.activity.ui.resources.ActivityIcons;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.util.string.StringLimiter;

public final class ShowStatusHistoryAction extends AbstractSingleActiveObjectAction<SalesforceWizardEntity> {
   private final StatusHistoryApi api;
   private Optional<StatusFileDownloaderFactory> statusFileDownloaderFactory = Optional.absent();

   public ShowStatusHistoryAction(ActiveObjectService var1, StatusHistoryApi var2) {
      super("Sfdc.StatusHistory", var1, "Result History", ActivityIcons.HISTORY_16, SalesforceWizardEntity.class);
      this.putValue("KongaMediumIconKey", ActivityIcons.HISTORY_24);
      this.putValue("SwingLargeIconKey", ActivityIcons.HISTORY_32);
      this.api = var2;
   }

   public void setStatusFileDownloaderFactory(StatusFileDownloaderFactory var1) {
      this.statusFileDownloaderFactory = Optional.of(var1);
   }

   protected void runOnObject(SalesforceWizardEntity var1) {
      StatusHistoryModel var2 = this.createModel(var1);
      StatusHistoryController var3 = new StatusHistoryController(var2, this.api);
      if (this.statusFileDownloaderFactory.isPresent()) {
         var3.enableRemoteFileSupport(this.statusFileDownloaderFactory.get());
      }

      StatusHistoryView var4 = new StatusHistoryView(var2, var3);
      WizardStyleDialog var5 = this.createDialog(var1, var4);
      var3.downloadHistory();
      var5.setVisible(true);
   }

   private StatusHistoryModel createModel(SalesforceWizardEntity var1) {
      return new StatusHistoryModel(var1.getImplementingOperationId());
   }

   private WizardStyleDialog createDialog(SalesforceWizardEntity var1, StatusHistoryView var2) {
      JComponent var3 = WizardStyleDialog.wrapUi(var2);
      JFrame var4 = ApplicationUi.getAppWinFrame();
      String var6 = "Result History: " + StringLimiter.limit(var1.getName(), 50);
      WizardStyleDialog var7 = new WizardStyleDialog(var4, var6, var3, KongaDialog.CLOSE_ONLY, true) {
         @Override
         protected String getCaptionTitle() {
            return "Result History";
         }
      };
      var7.manageLocation(StatusHistoryView.class);
      var7.setCloseButtonAsDefault();
      var2.setWaitService(var7);
      return var7;
   }
}
