package org.etlforge.util.file;

import java.io.File;
import java.util.Date;

public class ModifiedDateFilter extends AbstractDateBasedFileFilter {
   public ModifiedDateFilter(Date var1, Date var2) {
      super(var1, var2);
   }

   @Override
   protected long getTimestamp(File var1) {
      return var1.lastModified();
   }
}
