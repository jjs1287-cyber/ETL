package org.etlforge.integration.client.ui.admin.component.project;

import java.awt.EventQueue;
import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.integration.client.project.info.ProjectInfoCache;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleView;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.wait.WaitLock;

final class GetProjectListWorkflow {
   private final ProjectConsoleView console;
   private final ProjectInfoCache cache;
   private final Action downloadProjectListAction;

   public GetProjectListWorkflow(ProjectConsoleView var1, ProjectInfoCache var2, Action var3) {
      this.console = var1;
      this.cache = var2;
      this.downloadProjectListAction = var3;
   }

   public void getProjectList(boolean var1) {
      GetProjectListWorkflow.Worker var2 = new GetProjectListWorkflow.Worker(var1);
      var2.execute();
   }

   private class Worker extends KongaSwingWorker<List<IntegrationProject>, Void> {
      private final boolean checkCacheFirst;
      private volatile WaitLock waitLock;

      public Worker(boolean var2) {
         this.checkCacheFirst = var2;
      }

      protected List<IntegrationProject> doInBackground() throws Exception {
         this.waitLock = GetProjectListWorkflow.this.console.getWaitService().startWait();
         return this.checkCacheFirst ? GetProjectListWorkflow.this.cache.getProjects() : null;
      }

      protected void onSuccess(List<IntegrationProject> var1) {
         if (var1 != null) {
            GetProjectListWorkflow.this.console.getModel().setProjects(var1);
            GetProjectListWorkflow.this.console.getModel().setInfoIsCached(true);
         } else {
            GetProjectListWorkflow.this.downloadProjectListAction.actionPerformed(null);
         }
      }

      @Override
      protected void finished() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Worker.this.waitLock.release();
            }
         });
      }
   }
}
