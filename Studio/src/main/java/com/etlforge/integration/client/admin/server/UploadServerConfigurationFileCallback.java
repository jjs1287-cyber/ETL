package com.etlforge.integration.client.admin.server;

import org.etlforge.integration.client.server.ServerCallback;

public interface UploadServerConfigurationFileCallback extends ServerCallback {
   void fileWasUploaded();
}
