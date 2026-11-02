package org.etlforge.integration.jitterpack.impl;

import de.schlichtherle.io.ArchiveDetector;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.etlforge.integration.client.project.jitterpack.IllegalDataFormatException;
import org.etlforge.io.KongaIoUtils;

class V110_DescriptorDataFactory extends DescriptorDataFactory {
   private final String headerFileName;
   private final char[] password;

   public V110_DescriptorDataFactory(String var1, char[] var2) {
      this.headerFileName = var1;
      this.password = var2;
   }

   @Override
   public DescriptorData create(File var1) throws IOException, IllegalDataFormatException {
      boolean var2 = EncryptionChecker.isPossibleEncryptedJitterPack(var1);
      ArchiveDetector var3 = TrueZipUtils.getArchiveDetector(this.password);
      de.schlichtherle.io.File var4 = new de.schlichtherle.io.File(var1, var3);
      File[] var5 = var4.listFiles();
      if (var5 != null) {
         int var6 = var5.length;
         if (var6 == 1) {
            File var7 = var5[0];
            InputStream var8 = null;

            try {
               var8 = TrueZipUtils.getInputStream(var7, this.headerFileName, this.password);
               Properties var9 = new Properties();
               var9.load(var8);
               return new DescriptorData(var7.getName(), var9, var2);
            } finally {
               KongaIoUtils.close(var8);
            }
         }
      }

      throw new IllegalDataFormatException();
   }
}
