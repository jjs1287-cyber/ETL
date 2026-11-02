package org.etlforge.util.file;

import java.io.File;
import java.util.Comparator;

public enum FileComparators implements Comparator<File> {
   FOLDERS_FIRST {
      public int compare(File var1, File var2) {
         boolean var3 = var1.isDirectory();
         boolean var4 = var2.isDirectory();
         if (var3 && !var4) {
            return -1;
         } else {
            return var4 && !var3 ? 1 : var1.compareTo(var2);
         }
      }
   };

   FileComparators() {
   }
}
