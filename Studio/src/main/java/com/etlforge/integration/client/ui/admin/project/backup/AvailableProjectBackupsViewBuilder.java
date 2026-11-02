package com.etlforge.integration.client.ui.admin.project.backup;

import javax.swing.Action;
import org.etlforge.integration.common.server.project.backup.ProjectBackupDescriptor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.wait.WaitService;

public class AvailableProjectBackupsViewBuilder {
   private final IntegrationProject project;
   private WaitService waitService = WaitService.NONE;

   public AvailableProjectBackupsViewBuilder(IntegrationProject var1) {
      this.project = var1;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   public AvailableProjectBackupsView createView() {
      AvailableProjectBackupsView var1 = new AvailableProjectBackupsView(this.project);
      Action var2 = this.createRefreshAction(this.project, var1);
      var1.setRefreshAction(var2);
      DownloadBackupAction var3 = new DownloadBackupAction(this.project);
      var1.setDownloadSelectedBackupAction(var3);
      return var1;
   }

   private Action createRefreshAction(IntegrationProject var1, final AvailableProjectBackupsView var2) {
      RefreshBackupListAction var3 = new RefreshBackupListAction(var1);
      var3.setWaitService(this.waitService);
      var3.setResultHandler(new Receiver<ProjectBackupDescriptor[]>() {
         public void handle(ProjectBackupDescriptor[] var1) {
            var2.setBackups(var1);
         }
      });
      return var3;
   }
}
