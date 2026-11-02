package org.etlforge.application.appdir;

import java.io.File;
import java.io.IOException;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.io.KongaIoUtils;

public final class ReadMeGenerator {
   public static final String NAME = "README.txt";

   public static void generate(File var0) {
      if (var0 != null) {
         File var1 = new File(var0, "README.txt");
         if (!var1.exists()) {
            create(var1);
         }
      }
   }

   private static void create(File var0) {
      try {
         String var1 = "This folder contains application data for the "
            + ApplicationConfiguration.getProductName()
            + ". Modifying the files and subfolders in this folder may lead to unexpected results when running the "
            + ApplicationConfiguration.getProductName()
            + ".";
         KongaIoUtils.createAndWrite(var0, var1);
         var0.setReadOnly();
      } catch (IOException var2) {
      }
   }

   private ReadMeGenerator() {
      throw new UnsupportedOperationException();
   }
}
