package org.etlforge.integration.client.project.export;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.jitterpack.PostExportJob;
import org.etlforge.integration.client.project.jitterpack.PostExportManager;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.lang.exception.ExceptionUtils;

public final class ProjectPostExportManager implements PostExportManager {
   private final ManagedProject project;
   private final boolean keepPasswords;
   private final List<PostExportJob> jobs;

   public ProjectPostExportManager(ManagedProject var1, boolean var2) {
      Preconditions.checkNotNull(var1, "project");
      this.project = var1;
      this.keepPasswords = var2;
      this.jobs = new ArrayList<>();
   }

   @Override
   public boolean blankOutPasswords() {
      return !this.keepPasswords;
   }

   @Override
   public void addJob(PostExportJob var1) {
      Preconditions.checkNotNull(var1, "job");
      this.jobs.add(var1);
   }

   @Override
   public void runJobs(PostExportJob.Condition var1) {
      this.runJobsImpl(var1);
      this.saveProject();
   }

   private void runJobsImpl(PostExportJob.Condition var1) {
      for (Iterator var2 = this.jobs.iterator(); var2.hasNext(); var2.remove()) {
         PostExportJob var3 = (PostExportJob)var2.next();
         if (var3.getCondition().fulfills(var1)) {
            try {
               var3.doJob();
            } catch (Exception var5) {
               this.handleJobError(var5);
            }
         }
      }
   }

   private void handleJobError(Exception var1) {
      var1.printStackTrace();
      String var2 = "Failed to run post-export job: ";
      var2 = var2 + ExceptionUtils.getStackTrace(var1);
      ErrorLog.log(ProjectPostExportManager.class, var2, var1);
   }

   private void saveProject() {
      IntegrationProjectLock.acquire();

      try {
         this.project.saveToDisk();
      } catch (InterchangeSavingException var5) {
         this.handleSaveError(var5);
      } finally {
         IntegrationProjectLock.release();
      }
   }

   private void handleSaveError(InterchangeSavingException var1) {
      ErrorLog.attention(ProjectPostExportManager.class, "Post-export processing failed.", var1);
   }
}
