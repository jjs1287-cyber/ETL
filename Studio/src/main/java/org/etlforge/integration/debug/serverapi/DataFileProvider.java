package org.etlforge.integration.debug.serverapi;

import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.debug.client.file.DataFileIdentifier;
import org.etlforge.util.id.KongaGuid;

public interface DataFileProvider extends ServerCall {
   void downloadDataFile(KongaGuid var1, DataFileIdentifier var2, DownloadDataFileCallback var3);
}
