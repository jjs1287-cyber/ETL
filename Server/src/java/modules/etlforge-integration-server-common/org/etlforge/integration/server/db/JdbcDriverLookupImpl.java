package org.etlforge.integration.server.db;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.etlforge.integration.server.ServerFolders;
import org.etlforge.io.FileUtils;

class JdbcDriverLookupImpl implements JdbcDriverLookup {
   private static final FileFilter JAR_FILE_FILTER = new SuffixFileFilter(".jar");

   @Override
   public List<File> getJarFiles() {
      ArrayList var1 = new ArrayList();
      this.getJarFilesImpl(ServerFolders.JDBC_INSTALL_DIR, var1);
      return var1;
   }

   @Override
   public boolean isKnownDriver(String var1) {
      return "postgresql".equalsIgnoreCase(var1);
   }

   @Override
   public String getDriverClassName(String var1) throws JdbcDriverInfoException {
      if ("postgresql".equalsIgnoreCase(var1)) {
         return "org.postgresql.Driver";
      } else {
         throw new JdbcDriverInfoException("Unknown JDBC driver: " + var1);
      }
   }

   private void getJarFilesImpl(File var1, List<File> var2) {
      for (File var6 : var1.listFiles(JAR_FILE_FILTER)) {
         var2.add(var6);
      }

      for (File var10 : var1.listFiles(FileUtils.DIRECTORIES)) {
         this.getJarFilesImpl(var10, var2);
      }
   }
}
