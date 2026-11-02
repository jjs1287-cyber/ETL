package com.etlforge.integration.client.admin.project;

public interface ProjectDownloadTracker {
   void downloadingProjectDefinitions();

   void downloadingRequiredFiles();

   class Adapter implements ProjectDownloadTracker {
      @Override
      public void downloadingProjectDefinitions() {
      }

      @Override
      public void downloadingRequiredFiles() {
      }
   }
}
