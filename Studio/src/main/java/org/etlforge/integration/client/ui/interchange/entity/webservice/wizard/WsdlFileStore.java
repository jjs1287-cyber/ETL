package org.etlforge.integration.client.ui.interchange.entity.webservice.wizard;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.filestore.FileCollisionResolver;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.filestore.FileStoreException;
import org.etlforge.application.filestore.FileStoreFile;

final class WsdlFileStore {
   public static final String ROOT = "";
   private final FileStore fileStore;

   public WsdlFileStore(FileStore var1) {
      this.fileStore = Preconditions.checkNotNull(var1, "fs");
   }

   public void add(String var1, String var2, FileCollisionResolver var3) throws IOException, FileStoreException {
      if (!StringUtils.isEmpty(var1)) {
         File var4 = new File(var2);
         if (var4.canRead()) {
            String var5 = "" + var1;
            this.fileStore.addFile(var4, var5, var3);
         } else {
            throw new IOException("Could not read the file " + var2);
         }
      }
   }

   public FileStoreFile getRoot() throws FileStoreException {
      return this.fileStore.getFile("");
   }
}
