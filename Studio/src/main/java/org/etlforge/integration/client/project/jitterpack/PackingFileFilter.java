package org.etlforge.integration.client.project.jitterpack;

import java.io.File;

public interface PackingFileFilter {
   PackingFileFilter DEFAULT = new PackingFileFilter() {
      @Override
      public PackingFileFilter.Result evaluateDirectory(File var1) {
         return PackingFileFilter.INCLUDE;
      }

      @Override
      public PackingFileFilter.Result evaluateFile(File var1) {
         return PackingFileFilter.INCLUDE;
      }
   };
   PackingFileFilter.Result EXCLUDE = PackingFileFilter.Result.EXCLUDE;
   PackingFileFilter.Result INCLUDE = PackingFileFilter.Result.INCLUDE;
   PackingFileFilter.Result INCLUDE_SELECTIVELY = PackingFileFilter.Result.INCLUDE_SELECTIVELY;

   PackingFileFilter.Result evaluateDirectory(File var1);

   PackingFileFilter.Result evaluateFile(File var1);

   enum Result {
      EXCLUDE,
      INCLUDE,
      INCLUDE_SELECTIVELY;
   }
}
