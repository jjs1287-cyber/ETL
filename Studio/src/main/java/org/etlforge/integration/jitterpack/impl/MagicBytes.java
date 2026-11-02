package org.etlforge.integration.jitterpack.impl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

                  
final class MagicBytes {
   private static final byte[] MAGIC = new byte[]{-2, -78, -34, -57};

   static int length() {
      return MAGIC.length;
   }

   static void writeToStream(DataOutputStream var0) throws IOException {
      var0.write(MAGIC);
   }

   static boolean areMagic(byte[] var0) {
      return Arrays.equals(var0, MAGIC);
   }
}
