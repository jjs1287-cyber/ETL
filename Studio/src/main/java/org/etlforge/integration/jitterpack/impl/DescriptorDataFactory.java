package org.etlforge.integration.jitterpack.impl;

import de.schlichtherle.io.ArchiveDetector;
import java.io.File;
import java.io.IOException;
import org.etlforge.integration.client.project.jitterpack.IllegalDataFormatException;

abstract class DescriptorDataFactory {
   public abstract DescriptorData create(File var1) throws IOException, IllegalDataFormatException;

   public static DescriptorDataFactory newInstance(File var0, char[] var1, String var2) throws IllegalDataFormatException {
      if (var1 == null) {
         ArchiveDetector var3 = TrueZipUtils.getArchiveDetector(null);
         de.schlichtherle.io.File var4 = new de.schlichtherle.io.File(var0, var3);
         File[] var5 = var4.listFiles();
         if (var5 != null) {
            int var6 = var5.length;
            if (var6 == 1) {
               return new V110_DescriptorDataFactory(var2, null);
            }

            if (var6 >= 1) {
               return new V100_DescriptorDataFactory(var2);
            }
         }

         throw new IllegalDataFormatException();
      } else {
         return new V110_DescriptorDataFactory(var2, var1);
      }
   }
}
