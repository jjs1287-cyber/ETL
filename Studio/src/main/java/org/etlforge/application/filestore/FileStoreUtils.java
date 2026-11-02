package org.etlforge.application.filestore;

import java.io.File;

final class FileStoreUtils {
   private FileStoreUtils() {
      throw new UnsupportedOperationException();
   }

   static String formatPath(String var0) {
      return new File(var0).getPath();
   }
}
