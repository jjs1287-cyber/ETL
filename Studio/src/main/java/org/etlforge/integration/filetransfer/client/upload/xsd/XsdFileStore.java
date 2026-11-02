package org.etlforge.integration.filetransfer.client.upload.xsd;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
import org.etlforge.application.filestore.FileCollisionResolver;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.filestore.FileStoreException;

public final class XsdFileStore {
   public static final String ROOT = "";
   private final FileStore fileStore;

   public XsdFileStore(FileStore var1) {
      Preconditions.checkNotNull(var1, "fs");
      this.fileStore = var1;
   }

   public void addFile(String var1, String var2, String var3, FileCollisionResolver var4) throws IOException, FileStoreException {
      File var5 = new File(var1);
      if (var5.canRead()) {
         String var6 = "" + var2;
         this.fileStore.addFile(var5, var6, var4);
         if (var3 != null && var3.length() > 0) {
            this.fileStore.addDependency(var3, var2);
         }
      } else {
         throw new IOException("Could not read the file " + var1);
      }
   }

   public void addDependency(String var1, String var2) {
      this.fileStore.addDependency(var1, var2);
   }

   public FileStore getBackingFileStore() {
      return this.fileStore;
   }
}
