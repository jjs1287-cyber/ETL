package org.etlforge.integration.activity.serverapi.files;

import java.util.List;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.common.server.file.OperationFile;

public interface ListAvailableFilesCallback extends ServerCallback {
   void gotFileList(ServerInfo var1, List<OperationFile> var2);
}
