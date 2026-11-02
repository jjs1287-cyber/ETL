package org.etlforge.integration.server.log;

import java.io.File;
import java.util.Date;
import org.etlforge.util.file.ModifiedDateFilter;

public class LogDirectory {
   private final File root;

   public LogDirectory(File var1) {
      this.root = var1;
   }

   public final File[] listFilesFromModifiedDate(Date var1, Date var2) {
      return this.root.exists() && this.root.isDirectory() ? this.root.listFiles(new ModifiedDateFilter(var1, var2)) : new File[0];
   }
}
