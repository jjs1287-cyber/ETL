package org.etlforge.application.update;

public interface ExitWhileDownloadingHandler {
   ExitWhileDownloadingHandler CANCEL_DOWNLOAD = new ExitWhileDownloadingHandler() {
      @Override
      public boolean isExitAllowed() {
         return true;
      }
   };

   boolean isExitAllowed();
}
