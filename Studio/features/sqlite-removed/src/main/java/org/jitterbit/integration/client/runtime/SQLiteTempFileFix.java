package org.jitterbit.integration.client.runtime;

import java.io.File;
import java.io.FileFilter;
import org.jitterbit.application.appdir.ApplicationTempFolder;

final class SQLiteTempFileFix {
   public static void fix() {
      ApplicationTempFolder.deleteOnExit(new SQLiteTempFileFix.SQLiteLibFileFilter());
   }

   private static class SQLiteLibFileFilter implements FileFilter {
      private SQLiteLibFileFilter() {
      }

      @Override
      public boolean accept(File var1) {
         return var1.getName().toLowerCase().startsWith("libsqlitejdbc");
      }

      @Override
      public String toString() {
         return "SQLite JDBC Driver Lib Files";
      }
   }
}
