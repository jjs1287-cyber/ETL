package com.etlforge.integration.client.sfdc.data;

import com.google.common.base.Function;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.filestore.FileStoreFile;
import org.etlforge.integration.client.project.ManagedProject;

public final class LocalWsdlFileReader implements Function<String, InputStream> {
   private final FileStore fileStore;

   public LocalWsdlFileReader(ManagedProject var1) {
      this(var1.getFileStore());
   }

   public LocalWsdlFileReader(FileStore var1) {
      this.fileStore = var1;
   }

   public InputStream apply(String var1) {
      try {
         FileStoreFile var2 = this.fileStore.getFile(var1);
         return new FileInputStream(var2.getPhysicalRepresentation());
      } catch (FileNotFoundException var3) {
         throw new RuntimeException(var3);
      }
   }
}
