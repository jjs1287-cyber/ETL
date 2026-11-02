package com.etlforge.integration.client.ui.sfdc.wizard;

import com.google.common.io.Resources;
import java.net.URL;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.filestore.FileCollisionResolver;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.filestore.FileStoreFile;

final class PartnerWsdlToFileStoreCopier {
   private final FileStore fileStore;

   public PartnerWsdlToFileStoreCopier(FileStore var1) {
      this.fileStore = var1;
   }

   public void copyWsdl() {
      FileStoreFile var1 = this.fileStore.getFile("etlforge.salesforce.partner.29.wsdl");
      if (var1 == null) {
         this.copyWsdlImpl();
      }
   }

   private void copyWsdlImpl() {
      try {
         byte[] var1 = this.readWsdl();
         if (var1 != null) {
            this.fileStore.addFile("etlforge.salesforce.partner.29.wsdl", var1, FileCollisionResolver.REPLACE_EXISTING);
         }
      } catch (Exception var2) {
         ErrorLog.attention(this.getClass(), "Failed to copy the partner WSDL to the project's file store.", var2);
      }
   }

   private byte[] readWsdl() throws Exception {
      URL var1 = Resources.getResource("etlforge.salesforce.partner.29.wsdl");
      return Resources.toByteArray(var1);
   }
}
