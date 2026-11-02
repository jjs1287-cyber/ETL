package org.etlforge.integration.client.mapping.serverapi.webservice;

import org.etlforge.integration.client.mapping.webservice.WebServiceCallStructures;
import org.etlforge.integration.client.server.ServerCallback;

public interface GetWebServiceJtrCallback extends ServerCallback {
   void jtrGenerated(WebServiceCallStructures var1);
}
