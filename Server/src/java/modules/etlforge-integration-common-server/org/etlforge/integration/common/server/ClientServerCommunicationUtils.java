package org.etlforge.integration.common.server;

import com.google.common.base.Charsets;
import org.apache.commons.codec.binary.Base64;

public final class ClientServerCommunicationUtils {
   public static String encodeString(String var0) {
      if (var0 == null) {
         return null;
      }

      byte[] var1 = var0.getBytes(Charsets.UTF_8);
      return new String(Base64.encodeBase64Chunked(var1));
   }

   public static String decodeString(String var0) {
      if (var0 == null) {
         return null;
      }

      byte[] var1 = Base64.decodeBase64(var0.getBytes());
      return new String(var1, Charsets.UTF_8);
   }
}
