package com.etlforge.integration.client.ui.sfdc.wizard.v4;

import com.google.common.base.Function;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.filestore.FileStoreFile;

public final class LocalWsdlLookup implements Function<String, InputStream> {
   private final FileStore files;

   public LocalWsdlLookup(FileStore var1) {
      this.files = var1;
   }

   public InputStream apply(String var1) {
      try {
         FileStoreFile var2 = this.files.getFile(var1);
         return new FileInputStream(var2.getPhysicalRepresentation());
      } catch (FileNotFoundException var3) {
         throw new RuntimeException(var3);
      }
   }
}
