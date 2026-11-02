package org.etlforge.integration.client.ui.structure.editor.xsd;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.common.io.OutputSupplier;
import java.io.File;
import java.net.Authenticator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.etlforge.io.Folder;
import org.etlforge.io.IOSuppliers;
import org.etlforge.ui.util.SimpleUiAuthenticator;

public final class RemoteSampleFileFactory implements SampleFileFactory {
   private final FileType fileType;
   private final Folder outputFolder;
   private final String url;

   public RemoteSampleFileFactory(FileType var1, String var2, Folder var3) {
      this.fileType = var1;
      this.url = var2;
      this.outputFolder = var3;
   }

   @Override
   public SampleFile createSampleFile() throws Exception {
      File var1 = this.downloadXml();
      LocalSampleFileFactory var2 = new LocalSampleFileFactory(this.fileType, var1, this.outputFolder);
      SampleFile var3 = var2.createSampleFile();
      var3.useRootAsNameOfXsd();
      return var3;
   }

   private File downloadXml() throws Exception {
      File var1 = this.outputFolder.getFile(UUID.randomUUID().toString());
      int var2 = (int)TimeUnit.MINUTES.toMillis(5L);
      InputSupplier var3 = IOSuppliers.newInputStreamSupplier(this.url, var2);
      OutputSupplier var4 = Files.newOutputStreamSupplier(var1);

      try {
         Authenticator.setDefault(new SimpleUiAuthenticator());
         ByteStreams.copy(var3, var4);
      } finally {
         this.blankOutAuthenticator();
      }

      return var1;
   }

   private void blankOutAuthenticator() {
      Authenticator.setDefault(null);
   }
}
