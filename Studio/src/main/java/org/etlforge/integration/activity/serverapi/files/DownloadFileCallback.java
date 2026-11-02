package org.etlforge.integration.activity.serverapi.files;

import java.io.File;
import org.etlforge.integration.client.server.ServerCallback;

public interface DownloadFileCallback extends ServerCallback {
   void gotFile(File var1);
}
