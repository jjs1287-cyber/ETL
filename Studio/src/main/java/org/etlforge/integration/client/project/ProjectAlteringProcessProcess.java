package org.etlforge.integration.client.project;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;

public abstract class ProjectAlteringProcessProcess<T> {
   private final IntegrationProject project;

   public ProjectAlteringProcessProcess(IntegrationProject var1) {
      this.project = var1;
   }

   public final T run() {
      IntegrationProjectLock.acquire();

      try {
         Object var1 = this.doWork();
         this.saveProject();
         return (T)var1;
      } finally {
         IntegrationProjectLock.release();
      }
   }

   protected abstract T doWork();

   private void saveProject() {
      try {
         ManagedProject var1 = this.project.getExtensionObject(ManagedProject.class);
         var1.saveToDisk();
      } catch (InterchangeSavingException var2) {
         ErrorLog.attention(this.getClass(), "An error occurred when saving the project: " + var2.getMessage(), var2);
      }
   }
}
