package org.etlforge.integration.jitterpack.impl;

import java.util.Arrays;

final class ByteMarkers {
   private static final byte[] ENCRYPTED = new byte[]{82, 65, 69, 83};

   static boolean isEncrypted(byte[] var0) {
      return Arrays.equals(var0, ENCRYPTED);
   }

   private ByteMarkers() {
   }
}
