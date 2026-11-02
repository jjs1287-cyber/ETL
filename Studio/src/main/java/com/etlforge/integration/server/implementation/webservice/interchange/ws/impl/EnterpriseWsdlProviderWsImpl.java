package com.etlforge.integration.server.implementation.webservice.interchange.ws.impl;

import com.etlforge.integration.client.ui.sfdc.InterchangeKnownWsdlsStore;
import com.etlforge.integration.salesforce.wsdl.KnownWsdlsStore;
import java.io.File;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.wsdl.serverapi.DeleteWsdlCallback;
import org.etlforge.integration.client.wsdl.serverapi.GetWsdlFilesCallback;
import org.etlforge.integration.client.wsdl.serverapi.WsdlProvider;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;
import org.etlforge.integration.filetransfer.client.ServerFileUploadResult;
import org.etlforge.integration.filetransfer.serverapi.UploadServerFileCallback;
import org.etlforge.integration.server.implementation.webservice.interchange.webservice.impl.WebServiceInfoProviderWsImpl;

public final class EnterpriseWsdlProviderWsImpl implements WsdlProvider {
   private final WsdlProvider impl;

   public EnterpriseWsdlProviderWsImpl(CppWebServiceConfiguration var1) {
      this.impl = new WebServiceInfoProviderWsImpl(var1);
   }

   @Override
   public void uploadFile(File var1, ServerFileFolder var2, String var3, UploadServerFileCallback var4) {
      EnterpriseWsdlProviderWsImpl.LocalFileUploadCallback var5 = new EnterpriseWsdlProviderWsImpl.LocalFileUploadCallback(var1, var4);
      this.impl.uploadFile(var1, var2, var3, var5);
   }

   @Override
   public void getWsdlFiles(GetWsdlFilesCallback var1) {
      this.impl.getWsdlFiles(var1);
   }

   @Override
   public void uploadRemoteWsdl(String var1, UploadServerFileCallback var2) {
      this.impl.uploadRemoteWsdl(var1, var2);
   }

   @Override
   public void deleteWsdl(String var1, boolean var2, DeleteWsdlCallback var3) {
      this.impl.deleteWsdl(var1, var2, var3);
   }

   private static class LocalFileUploadCallback implements UploadServerFileCallback {
      private final String locator;
      private final File localFile;
      private final UploadServerFileCallback externalCallback;

      public LocalFileUploadCallback(File var1, UploadServerFileCallback var2) {
         this.localFile = var1;
         this.locator = var1.getName();
         this.externalCallback = var2;
      }

      @Override
      public void fileWasUploaded(ServerFileUploadResult var1) {
         try {
            this.updateSfdcWsdlCache();
         } finally {
            this.externalCallback.fileWasUploaded(var1);
         }
      }

      private void updateSfdcWsdlCache() {
         KnownWsdlsStore var1 = InterchangeKnownWsdlsStore.getStore();
         var1.replaceLocalWsdl(this.locator, this.localFile);
      }

      @Override
      public void caught(Throwable var1) {
         this.externalCallback.caught(var1);
      }

      @Override
      public void cancelled() {
         this.externalCallback.cancelled();
      }
   }
}
