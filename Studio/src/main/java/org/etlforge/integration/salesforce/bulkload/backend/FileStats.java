package org.etlforge.integration.salesforce.bulkload.backend;

import org.etlforge.system.ArgCheck;

public final class FileStats {
   public final long fileSize;
   public final long startPos;

   public FileStats(long var1, long var3) {
      this.fileSize = ArgCheck.checkNotNegative(var1);
      this.startPos = ArgCheck.checkNotNegative(var3);
   }
}
