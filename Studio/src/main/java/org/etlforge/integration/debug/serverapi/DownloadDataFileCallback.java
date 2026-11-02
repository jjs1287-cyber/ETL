package org.etlforge.integration.debug.serverapi;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.debug.client.file.DataFile;
import org.etlforge.integration.debug.client.file.DataFileIdentifier;

public interface DownloadDataFileCallback extends ServerCallback {
   void fileWasDownloaded(DataFileIdentifier var1, DataFile var2);
}
