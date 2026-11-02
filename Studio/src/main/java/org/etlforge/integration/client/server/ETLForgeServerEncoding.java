package org.etlforge.integration.client.server;

import com.google.common.base.Charsets;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.zip.DataFormatException;
import org.etlforge.util.zip.ZipUtils;

public final class ETLForgeServerEncoding {
   private static final Charset SERVER_ENCODING = Charsets.UTF_8;

   public static String base64DecodeAndInflateString(String var0) throws DataFormatException {
      try {
         return ZipUtils.base64DecodeAndInflateString(var0, SERVER_ENCODING);
      } catch (UnsupportedEncodingException var2) {
         throw new RuntimeException(var2);
      }
   }

   public static String deflateAndBase64EncodeString(String var0) throws IOException {
      return ZipUtils.deflateAndBase64EncodeString(var0, SERVER_ENCODING);
   }

   private ETLForgeServerEncoding() {
   }
}
