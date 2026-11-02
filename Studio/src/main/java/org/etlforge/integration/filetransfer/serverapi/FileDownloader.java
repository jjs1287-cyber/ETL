package org.etlforge.integration.filetransfer.serverapi;

import org.etlforge.integration.client.server.ServerCall;

public interface FileDownloader extends ServerCall {
   void downloadFile(String var1, FileDownloadCallback var2);
}
