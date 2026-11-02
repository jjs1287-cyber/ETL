package org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl;

import org.etlforge.integration.client.server.AbstractServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.wsdl.cache.WsdlListCache;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.integration.filetransfer.client.ServerFileUploadResult;
import org.etlforge.integration.filetransfer.serverapi.UploadServerFileCallback;

final class UploadRemoteWsdlCallbackImpl extends AbstractServerCallback implements UploadServerFileCallback {
   private final ProcessRemoteWsdlJob job;
   private final WsdlFile wsdlFile;

   public UploadRemoteWsdlCallbackImpl(ProcessRemoteWsdlJob var1, WsdlFile var2) {
      this.job = var1;
      this.wsdlFile = var2;
   }

   @Override
   public void fileWasUploaded(ServerFileUploadResult var1) {
      ServerInfo var2 = var1.getServer();
      WsdlListCache.update(var2.getGuid(), this.wsdlFile);
      this.job.signalJobSucceeded();
   }

   @Override
   public void cancelled() {
      this.job.signalJobCancelled();
   }

   @Override
   public void caught(Throwable var1) {
      this.job.signalJobFailed(var1);
   }
}
