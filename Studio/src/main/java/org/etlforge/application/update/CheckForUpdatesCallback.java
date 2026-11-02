package org.etlforge.application.update;

public interface CheckForUpdatesCallback {
   void isUpToDate();

   void laterVersionFound(LatestVersion var1);

   void requestCanceled();

   void resultNotAvailable(UpdateServiceException var1);
}
