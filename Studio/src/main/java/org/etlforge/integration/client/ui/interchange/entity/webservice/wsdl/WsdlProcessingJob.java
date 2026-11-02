package org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl;

import org.etlforge.application.ui.job.JobMessageListener;
import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;

public interface WsdlProcessingJob extends Runnable {
   void setCallback(WsdlProcessingCallback var1);

   boolean isDone();

   WsdlFile getWsdlFile();

   WebServiceInfo getParsedInfo();

   void addJobMessageListener(JobMessageListener var1);
}
