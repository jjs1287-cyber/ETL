package org.etlforge.integration.client.connector.mscrm.serverapi;

import javax.annotation.ParametersAreNonnullByDefault;
import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.data.connector.MSCrmEndpoint;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;

@ParametersAreNonnullByDefault
public interface MSCrmDesignInfoProvider extends ServerCall, FileUploadApi {
   void getObjectDescriptions(MSCrmEndpoint var1, GetObjectDescriptionsCallback var2);

   void getDetailedMetaData(MSCrmEndpoint var1, String var2, GetDetailedMetaDataCallback var3);

   void getSavedQueries(MSCrmEndpoint var1, String var2, GetSavedQueriesCallback var3);

   void getQueryResponseSchema(MSCrmEndpoint var1, String var2, GetResponseSchemaCallback var3);
}
