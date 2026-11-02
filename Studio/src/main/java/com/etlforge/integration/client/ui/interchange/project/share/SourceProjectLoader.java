package com.etlforge.integration.client.ui.interchange.project.share;

import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectLoadingCancelledException;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.ui.wait.NullWaitService;
import org.etlforge.ui.wait.WaitLock;

public final class SourceProjectLoader {
   private final ShareProjectDataModel model;
   private String titleWhenDone;

   public SourceProjectLoader(ShareProjectDataModel var1) {
      this.model = var1;
   }

   public void setTitleWhenDone(String var1) {
      this.titleWhenDone = var1;
   }

   public void load(ProjectLocation var1, WaitLock var2) {
      Application.getWorker().submitForParallel(new SourceProjectLoader.Worker(var1, var2));
   }

   private final class Worker implements ApplicationWorker.SafeRunnable {
      private final ProjectLocation loc;
      private final WaitLock waitLock;

      public Worker(ProjectLocation var2, WaitLock var3) {
         this.loc = var2;
         this.waitLock = var3 != null ? var3 : new NullWaitService().startWait();
      }

      @Override
      public void run() {
         IntegrationProjectLock.acquire();

         try {
            ProjectManager var1 = SourceProjectLoader.this.model.getProjectManager();
            ManagedProject var2 = var1.inspect(this.loc.getProjectName(), this.loc.getLocation());
            SourceProjectLoader.this.model.setSourceProject(var2);
            if (SourceProjectLoader.this.titleWhenDone != null) {
               SourceProjectLoader.this.model.setSourceProjectTitle(SourceProjectLoader.this.titleWhenDone);
            }
         } catch (ProjectLoadingCancelledException var7) {
         } catch (Exception var8) {
            ErrorLog.attention(ProjectItemImporter.class, "An error occurred when opening the project.", var8);
         } finally {
            IntegrationProjectLock.release();
            this.waitLock.release();
         }
      }
   }
}
