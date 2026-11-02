package com.etlforge.integration.server.engine.bulkload;

public interface BulkLoadMonitor {
   BulkLoadMonitor NONE = new BulkLoadMonitor() {
      @Override
      public void preCreateJob() {
      }

      @Override
      public void postJobCreated(String var1) {
      }

      @Override
      public void preCloseJob() {
      }

      @Override
      public void postCloseJob() {
      }

      @Override
      public void done() {
      }
   };
   BulkLoadMonitor CONSOLE = new BulkLoadMonitor() {
      @Override
      public void preCreateJob() {
         System.out.println("About to create the salesforce job.");
      }

      @Override
      public void postJobCreated(String var1) {
         System.out.println("The salesforce job was created[jobId = " + var1 + "]");
      }

      @Override
      public void preCloseJob() {
         System.out.println("About to close the salesforce job.");
      }

      @Override
      public void postCloseJob() {
         System.out.println("The salesforce job was closed");
      }

      @Override
      public void done() {
         System.out.println("The bulk load process is done.");
      }
   };

   void preCreateJob() throws PersistorException;

   void postJobCreated(String var1) throws PersistorException;

   void preCloseJob() throws PersistorException;

   void postCloseJob() throws PersistorException;

   void done() throws PersistorException;
}
