package org.etlforge.integration.client.mapping.serverapi.webservice;

import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;

public interface WebServiceInfoProvider extends ServerCall {
   void getInputAndOutputJTR(WebServiceOperationInfo var1, GetWebServiceJtrCallback var2);
}
