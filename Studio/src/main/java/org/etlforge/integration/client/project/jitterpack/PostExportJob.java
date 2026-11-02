package org.etlforge.integration.client.project.jitterpack;

public interface PostExportJob {
   void doJob();

   PostExportJob.Condition getCondition();

   enum Condition {
      ALWAYS,
      ON_SUCCESS,
      ON_FAILURE;

      public boolean fulfills(PostExportJob.Condition var1) {
         return this == ALWAYS ? true : this == var1;
      }
   }
}
