package org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl;

import java.net.MalformedURLException;
import org.etlforge.integration.client.wsdl.WsdlParserException;
import org.etlforge.integration.client.wsdl.WsdlUtils;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;

final class ProcessRemoteWsdlJob extends AbstractWsdlProcessingJob {
   private final String wsdlUrl;

   public ProcessRemoteWsdlJob(String var1) {
      this.wsdlUrl = var1;
   }

   @Override
   public void run() {
      try {
         this.setNewMessage(PackageResources.ProcessRemoteFileJob.STATUS_PARSING);
         this.setParsedInfo(WsdlUtils.parseRemoteWsdl(this.wsdlUrl));
      } catch (WsdlParserException var2) {
         this.signalJobFailed(var2);
         return;
      } catch (MalformedURLException var3) {
         this.signalJobFailed(var3);
         return;
      }

      WsdlFile var1 = new WsdlFile();
      var1.putString("wsdl_id", this.wsdlUrl);
      this.setWsdlFile(var1);
      this.setNewMessage(PackageResources.ProcessRemoteFileJob.STATUS_UPLOAD);
      WsdlUtils.uploadRemoteWsdl(this.wsdlUrl, new UploadRemoteWsdlCallbackImpl(this, var1));
   }
}
