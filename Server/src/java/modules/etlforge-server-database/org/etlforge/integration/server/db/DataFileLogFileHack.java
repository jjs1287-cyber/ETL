package org.etlforge.integration.server.db;

public final class DataFileLogFileHack {
   public final String dataFile;
   public final String logFile;

   public DataFileLogFileHack(String var1) {
      String[] var2 = var1.split("\\|", 2);
      this.dataFile = var2[0];
      this.logFile = var2.length > 1 ? var2[1] : null;
   }
}
