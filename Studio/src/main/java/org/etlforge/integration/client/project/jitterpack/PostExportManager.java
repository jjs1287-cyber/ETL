package org.etlforge.integration.client.project.jitterpack;

public interface PostExportManager {
   boolean blankOutPasswords();

   void addJob(PostExportJob var1);

   void runJobs(PostExportJob.Condition var1);
}
