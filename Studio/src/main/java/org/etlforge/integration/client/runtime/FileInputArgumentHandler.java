package org.etlforge.integration.client.runtime;

import java.io.File;

public interface FileInputArgumentHandler {
   FileInputArgumentHandler NONE = new FileInputArgumentHandler() {
      @Override
      public void setProjectToOpen(File var1) {
      }

      @Override
      public void setJitterpackToImport(File var1) {
      }

      @Override
      public void process() {
      }

      @Override
      public boolean isEmpty() {
         return true;
      }
   };

   void setJitterpackToImport(File var1);

   void setProjectToOpen(File var1);

   boolean isEmpty();

   void process();
}
