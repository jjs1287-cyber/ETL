package org.etlforge.integration.client.project.jitterpack;

import java.io.File;
import java.io.FileFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.io.FileUtils;

public final class JitterPackConstants {
   public static final String SINGULAR = "Jitterpak";
   public static final String PLURAL = "Jitterpaks";
   public static final String FILE_EXTENSION = ".jpk";
   public static final FileFilter FILE_FILTER = new FileFilter() {
      @Override
      public boolean accept(File var1) {
         if (var1.isDirectory()) {
            return true;
         }

         String var2 = FileUtils.getExtension(var1, true);
         return ".jpk".equalsIgnoreCase(var2);
      }
   };

   private JitterPackConstants() {
   }

   public static String getSuggestedJitterpakName(IntegrationProject var0) {
      return var0.getName() + ".jpk";
   }
}
