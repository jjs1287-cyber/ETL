package com.etlforge.integration.client.ui.admin.scheduler;

import com.etlforge.integration.client.admin.project.ProjectAdministration;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.Date;
import javax.swing.AbstractAction;
import org.etlforge.ui.property.AbstractBinding;

public final class ScheduledRunsController {
   private final ScheduledRunsModel model;
   private final ProjectAdministration serverApi;
   private final ScheduledRunsController.DownloadAction downloadAction;

   public ScheduledRunsController(ProjectAdministration var1, ScheduledRunsModel var2, ScheduledRunsView var3) {
      this.serverApi = var1;
      this.model = var2;
      this.downloadAction = new ScheduledRunsController.DownloadAction();
      var3.setDownloadHandler(this.downloadAction);
      this.bindDownloadAction();
      this.bindSelectedDay();
   }

   private void bindDownloadAction() {
      ScheduledRunsController.DownloadActionBinding var1 = new ScheduledRunsController.DownloadActionBinding();
      var1.syncUi();
   }

   private void bindSelectedDay() {
      new ScheduledRunsController.SelectedDayBinding();
   }

   private void downloadSchedules() {
      DownloadScheduledRunsJob var1 = new DownloadScheduledRunsJob(this.model, this.serverApi);
      var1.submit();
   }

   private class DownloadAction extends AbstractAction {
      public DownloadAction() {
         super(EnterpriseAdminResources.getString("ScheduledRuns.Download.Name"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ScheduledRunsController.this.downloadSchedules();
            }
         });
      }
   }

   private class DownloadActionBinding extends AbstractBinding<Boolean> {
      public DownloadActionBinding() {
         super(ScheduledRunsController.this.model, "isDownloading");
      }

      protected void updateUiWithValue(Boolean var1) {
         boolean var2 = !var1 && ScheduledRunsController.this.model.getDay() != null;
         ScheduledRunsController.this.downloadAction.setEnabled(var2);
      }
   }

   private class SelectedDayBinding extends AbstractBinding<Date> {
      public SelectedDayBinding() {
         super(ScheduledRunsController.this.model, "day");
      }

      protected void updateUiWithValue(Date var1) {
         if (!ScheduledRunsController.this.model.isCached(var1)) {
            ScheduledRunsController.this.downloadSchedules();
         }
      }
   }
}
