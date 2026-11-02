package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import java.io.File;
import java.util.concurrent.Callable;
import org.etlforge.integration.debug.client.file.DataFileIdentifier;

final class DataFileOpener implements Callable<File> {
   private final TransformationFileStore dataFileStore;
   private final DataFileIdentifier fileId;

   public DataFileOpener(TransformationFileStore var1, DataFileIdentifier var2) {
      this.dataFileStore = var1;
      this.fileId = var2;
   }

   public File call() throws Exception {
      TransformationDataFile var1 = this.dataFileStore.getFile(this.fileId);
      return var1.open();
   }
}
