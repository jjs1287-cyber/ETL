package org.jitterbit.integration.client.dl.migration;

import com.google.common.annotations.VisibleForTesting;
import java.io.File;
import java.io.FileFilter;
import org.jitterbit.string.RegExPredicate;

final class ProjectFolderFilter implements FileFilter {
   private final RegExPredicate nameFilter = new RegExPredicate("Data Loader(?: \\d+)?");

   @Override
   public boolean accept(File var1) {
      return var1.isDirectory() && this.isNameMatch(var1.getName());
   }

   @VisibleForTesting
   boolean isNameMatch(String var1) {
      return this.nameFilter.apply(var1);
   }
}
