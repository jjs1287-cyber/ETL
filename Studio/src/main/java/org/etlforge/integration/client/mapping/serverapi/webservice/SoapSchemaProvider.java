package org.etlforge.integration.client.mapping.serverapi.webservice;

import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;

public interface SoapSchemaProvider extends ServerCall {
   void generateXsds(WebServiceOperationInfo var1, String var2, String var3, boolean var4, GenerateSoapSchemasCallback var5);
}
