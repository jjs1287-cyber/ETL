package org.etlforge.integration.filetransfer.serverapi;

import java.util.List;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;

public interface FileDownloadCallback extends ServerCallback {
   void fileWasDownloaded(ServerInfo var1, String var2, String var3, List<? extends FileDownloadDependency> var4);
}
