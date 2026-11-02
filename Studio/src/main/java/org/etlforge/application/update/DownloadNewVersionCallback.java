package org.etlforge.application.update;

public interface DownloadNewVersionCallback {
   void progressUpdate(int var1, String var2);

   void downloadComplete();

   void downloadCanceled();

   void downloadFailed(Exception var1);
}
