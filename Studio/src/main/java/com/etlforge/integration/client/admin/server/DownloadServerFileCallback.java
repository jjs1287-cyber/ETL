package com.etlforge.integration.client.admin.server;

import java.io.File;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerCallbackAdapter;

public interface DownloadServerFileCallback extends ServerCallback {
   void fileWasDownloaded(File var1);

   abstract class Adapter extends ServerCallbackAdapter implements DownloadServerFileCallback {
      @Override
      public void fileWasDownloaded(File var1) {
      }
   }
}
