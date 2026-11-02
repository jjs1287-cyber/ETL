package com.etlforge.integration.client.admin.server;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerCallbackAdapter;

public interface GetServerFileListCallback extends ServerCallback {
   void fileListRetrieved(ServerFileLocator[] var1);

   abstract class Adapter extends ServerCallbackAdapter implements GetServerFileListCallback {
      @Override
      public void fileListRetrieved(ServerFileLocator[] var1) {
      }
   }
}
