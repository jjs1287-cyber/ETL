package org.etlforge.integration.client.ui.jitterpack.table;

import java.io.File;

final class JitterPackLocationTableItem {
   private final File location;

   public JitterPackLocationTableItem(File var1) {
      this.location = var1;
   }

   @Override
   public String toString() {
      return this.location.getAbsolutePath();
   }
}
