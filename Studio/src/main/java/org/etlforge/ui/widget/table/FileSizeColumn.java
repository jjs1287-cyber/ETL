package org.etlforge.ui.widget.table;

import java.io.File;
import org.etlforge.util.StorageSizeUnit;

public final class FileSizeColumn {
   private final String display;

   public FileSizeColumn(File var1, StorageSizeUnit var2) {
      this(var1.length(), var2);
   }

   public FileSizeColumn(long var1, StorageSizeUnit var3) {
      this.display = var3.format(var1);
   }

   @Override
   public String toString() {
      return this.display;
   }
}
