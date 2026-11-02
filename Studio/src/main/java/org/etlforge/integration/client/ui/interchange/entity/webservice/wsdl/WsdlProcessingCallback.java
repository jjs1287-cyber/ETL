package org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl;

import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;

public interface WsdlProcessingCallback {
   void processCompletedSuccessfully(WsdlFile var1, WebServiceInfo var2);

   void processingFailed(Throwable var1);

   void processingCancelled();
}
