package org.etlforge.integration.debug.client.file;

import java.io.Serializable;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.util.id.KongaGuid;

public interface DataFilesInfo extends Serializable {
   KongaGuid getGuid();

   OperationId getOperationId();

   TransformationId getTransformationId();

   ServerInfo getServerInfo();

   int getNumberOfOriginalFiles(DataFileType var1);

   DataFileInfo getFileInfo(DataFileType var1, int var2);

   boolean contains(DataFileIdentifier var1);

   boolean containsFileOfType(DataFileType var1);
}
