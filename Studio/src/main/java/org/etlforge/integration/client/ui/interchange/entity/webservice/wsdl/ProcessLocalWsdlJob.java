package org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl;

import java.io.File;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.wsdl.WsdlParserException;
import org.etlforge.integration.client.wsdl.WsdlUtils;
import org.etlforge.integration.client.wsdl.serverapi.WsdlProvider;
import org.etlforge.integration.client.wsdl.upload.WsdlUploader;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.system.callback.CallbackResult;

final class ProcessLocalWsdlJob extends AbstractWsdlProcessingJob {
   private final FileStore fileStore;
   private final String filePath;

   public ProcessLocalWsdlJob(FileStore var1, String var2) {
      this.fileStore = var1;
      this.filePath = var2;
   }

   @Override
   public void run() {
      File var1 = new File(this.filePath);

      try {
         this.setNewMessage(PackageResources.ProcessLocalFileJob.STATUS_PARSING);
         this.setParsedInfo(WsdlUtils.parseLocalWsdl(var1));
      } catch (WsdlParserException var3) {
         this.signalJobFailed(var3);
         return;
      }

      WsdlFile var2 = new WsdlFile();
      var2.putString("wsdl_id", var1.getName());
      this.setWsdlFile(var2);
      this.setNewMessage(PackageResources.ProcessLocalFileJob.STATUS_UPLOAD);
      this.uploadWsdlFile(var1);
   }

   private void uploadWsdlFile(File var1) {
      IntegrationServer var2 = IntegrationServer.getInstance();
      WsdlProvider var3 = var2.getServerCall(WsdlProvider.class);
      WsdlUploader var4 = new WsdlUploader(var3, this.fileStore);
      var4.upload(var1, new CallbackResult() {
         @Override
         public void succeeded(Object var1) {
            ProcessLocalWsdlJob.this.signalJobSucceeded();
         }

         @Override
         public void failed(Object var1) {
            ProcessLocalWsdlJob.this.signalJobFailed((Throwable)var1);
         }

         @Override
         public void cancelled(Object var1) {
            ProcessLocalWsdlJob.this.signalJobCancelled();
         }
      });
   }
}
