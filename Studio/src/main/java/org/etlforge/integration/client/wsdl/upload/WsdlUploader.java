package org.etlforge.integration.client.wsdl.upload;

import com.google.common.collect.ImmutableList;
import java.io.File;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.integration.client.wsdl.serverapi.WsdlProvider;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.upload.FileUploadJob;
import org.etlforge.integration.filetransfer.client.upload.FileUploader;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;
import org.etlforge.system.callback.CallbackResult;
import org.etlforge.system.callback.CallbackResultWrapper;

public final class WsdlUploader {
   private final WsdlProvider serverInterface;
   private final FileStore fileStore;

   public WsdlUploader(WsdlProvider var1, FileStore var2) {
      this.serverInterface = var1;
      this.fileStore = var2;
   }

   public void upload(File var1, CallbackResult var2) {
      var2 = new CallbackResultWrapper(var2);
      FileUploader var3 = this.createWorker(var1, var2);

      try {
         var3.start();
      } catch (InterruptedException var5) {
         Thread.currentThread().interrupt();
         var5.printStackTrace();
         var2.failed(var5);
      }
   }

   private FileUploader createWorker(File var1, CallbackResult var2) {
      WsdlUploadClient var3 = new WsdlUploadClient(this.fileStore, var2);
      return new FileUploader(this.serverInterface, var3, var1.getAbsolutePath()) {
         @Override
         protected FileUploadJob createUploadJob(FileUploadApi var1, ServerFile var2x, ImmutableList<File> var3x) {
            return new FileUploadJob("Uploading WSDL file(s) to the server", this, var2x, var3x);
         }
      };
   }
}
