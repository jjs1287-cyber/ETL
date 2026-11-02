package org.etlforge.util.file;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.FileFilter;
import java.util.Date;

public abstract class AbstractDateBasedFileFilter implements FileFilter {
   private final long startDate;
   private final long endDate;
   private boolean acceptFolders;

   public AbstractDateBasedFileFilter(Date var1, Date var2) {
      Preconditions.checkArgument(var1 == null || var2 == null || var1.before(var2), "startDate must be before endDate");
      this.startDate = var1 != null ? var1.getTime() : -1L;
      this.endDate = var2 != null ? var2.getTime() : -1L;
   }

   public final void setAcceptFolders(boolean var1) {
      this.acceptFolders = var1;
   }

   @Override
   public final boolean accept(File var1) {
      if (var1.isFile()) {
         long var2 = this.getTimestamp(var1);
         return this.startDate >= 0L && this.startDate > var2 ? false : this.endDate < 0L || this.endDate >= var2;
      } else {
         return this.acceptFolders;
      }
   }

   protected abstract long getTimestamp(File var1);
}
