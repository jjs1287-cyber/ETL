package org.etlforge.integration.server.backend.webservice.jdbc;

import com.google.common.base.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.server.BackendEncoding;

final class JdbcEngineUtils {
   static final String BASE64_MARKER = "b64:";

   public static String getSqlStatement(WsDbLookupParams var0) {
      String var1 = var0.getSql();
      String var2 = var1;
      if (var1.startsWith("b64:")) {
         var2 = base64DecodeSqlStatement(var1);
      } else if (BackendEncoding.AXIS_CHARSET != BackendEncoding.CHARSET) {
         var2 = axisEncodingToJavaString(var1);
      }

      return var2;
   }

   private static String base64DecodeSqlStatement(String var0) {
      String var1 = var0.substring("b64:".length());
      if (StringUtils.isBlank(var1)) {
         throw new IllegalArgumentException("Invalid use of the base64-encoding marker b64:: The base-64 encoded data is missing.");
      }

      Exception var2 = null;

      try {
         byte[] var3 = var1.getBytes();
         if (Base64.isArrayByteBase64(var3)) {
            Base64 var4 = new Base64();
            byte[] var5 = var4.decode(var3);
            return new String(var5, BackendEncoding.CHARSET);
         }
      } catch (Exception var6) {
         var2 = var6;
      }

      String var7 = String.format("Invalid use of the base64-encoding marker %s: The string \"%s\" could not be decoded.", "b64:", var0);
      throw new RuntimeException(var7, var2);
   }

   public static String getResult(String var0) {
      return base64EncodeResult(var0);
   }

   private static String axisEncodingToJavaString(String var0) {
      byte[] var1 = var0.getBytes(BackendEncoding.AXIS_CHARSET);
      return new String(var1, BackendEncoding.CHARSET);
   }

   private static String base64EncodeResult(String var0) {
      if (var0 == null) {
         return null;
      }

      if (var0.isEmpty()) {
         return "";
      }

      byte[] var1 = Base64.encodeBase64Chunked(var0.getBytes(BackendEncoding.CHARSET));
      StringBuilder var2 = new StringBuilder("b64:");
      var2.append(new String(var1, Charsets.US_ASCII));
      return var2.toString();
   }

   private JdbcEngineUtils() {
   }
}
