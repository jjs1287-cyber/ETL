package org.etlforge.integration.client.project.jitterpack;

import java.io.File;

public final class JitterPack {
   private final File file;
   private final JitterPackDescriptor descriptor;

   public JitterPack(File var1, JitterPackDescriptor var2) {
      this.file = var1;
      this.descriptor = var2;
   }

   public File getFile() {
      return this.file;
   }

   public JitterPackDescriptor getDescriptor() {
      return this.descriptor;
   }
}
