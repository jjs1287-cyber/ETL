package org.etlforge.integration.jitterpack.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.etlforge.io.KongaIoUtils;

final class EncryptionChecker {
   public static boolean isPossibleEncryptedJitterPack(File var0) throws IOException {
      FileInputStream var1 = new FileInputStream(var0);

      try {
         byte[] var2 = new byte[4];
         int var3 = var1.read(var2);
         return var3 == var2.length && ByteMarkers.isEncrypted(var2);
      } finally {
         KongaIoUtils.close(var1);
      }
   }

   private EncryptionChecker() {
   }
}
