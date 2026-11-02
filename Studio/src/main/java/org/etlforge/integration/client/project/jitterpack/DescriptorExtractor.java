package org.etlforge.integration.client.project.jitterpack;

import java.io.File;
import org.etlforge.util.version.UnsupportedVersionException;

public class DescriptorExtractor implements Runnable {
   private final Importer importer;
   private final File file;
   private final ImportParameters params;
   private final ExtractDescriptorCallback callback;

   public DescriptorExtractor(File var1, ImportParameters var2, Importer var3, ExtractDescriptorCallback var4) {
      this.file = var1;
      this.params = var2;
      this.importer = var3;
      this.callback = var4;
   }

   @Override
   public void run() {
      try {
         JitterPackDescriptor var1 = this.extract();
         this.callback.descriptorExtracted(var1);
      } catch (Exception var2) {
         this.callback.failed(null, var2);
      }
   }

   public JitterPackDescriptor extract() throws JitterPackException, UnsupportedVersionException {
      return this.importer.extractDescriptor(this.file, this.params);
   }
}
