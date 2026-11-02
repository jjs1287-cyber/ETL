package org.etlforge.integration.client.connector.serverapi;

import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;

@ParametersAreNonnullByDefault
public interface ConnectorCallInfoProvider extends ServerCall, FileUploadApi {
   void getObjectList(ConnectorEndpoint var1, List<ConnectorObject> var2, ListObjectsCallback var3);

   void getSingleSchemas(ConnectorEndpoint var1, List<ConnectorObject> var2, GetSchemasCallback var3);

   void getMultipleSchemas(ConnectorEndpoint var1, List<ConnectorObject> var2, boolean var3, GetSchemasCallback var4);
}
