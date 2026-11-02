package com.etlforge.integration.client.ui.admin.project.backup;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.job.JobStatusAdapter;
import org.etlforge.application.ui.job.JobStatusChangedEvent;
import org.etlforge.integration.common.server.project.backup.ProjectBackupDescriptor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.wait.WaitService;

final class RefreshBackupListAction extends AbstractAction {
   private final IntegrationProject project;
   private Receiver<ProjectBackupDescriptor[]> resultHandler;
   private WaitService waitService = WaitService.NONE;

   public RefreshBackupListAction(IntegrationProject var1) {
      this.project = var1;
      this.resultHandler = NoReceiver.create();
      ActionDecorators.REFRESH.decorate(this);
   }

   public void setResultHandler(Receiver<ProjectBackupDescriptor[]> var1) {
      this.resultHandler = var1;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            RefreshBackupListAction.this.startJob();
         }
      });
   }

   private void startJob() {
      this.setEnabled(false);
      DownloadBackupListJob var1 = new DownloadBackupListJob(this.project);
      var1.setResultReceiver(this.resultHandler);
      var1.addJobStatusListener(new JobStatusAdapter() {
         @Override
         public void jobEnded(JobStatusChangedEvent var1) {
            ActionUtils.setEnabledSafely(RefreshBackupListAction.this, true);
         }
      });
      var1.submit(this.waitService, 0);
   }
}
