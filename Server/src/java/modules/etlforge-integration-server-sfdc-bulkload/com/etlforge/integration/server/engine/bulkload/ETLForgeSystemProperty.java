package com.etlforge.integration.server.engine.bulkload;

import com.google.common.base.Charsets;
import java.nio.charset.Charset;
import org.apache.commons.lang.StringUtils;

public final class ETLForgeSystemProperty {
   public static Charset getCharset(String var0) {
      return getCharsetFromName(var0, Charset.defaultCharset());
   }

   private static Charset getCharsetFromName(String var0, Charset var1) {
      if (StringUtils.isNotEmpty(var0)) {
         try {
            return Charset.forName(var0);
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

      return var1;
   }

   public static Charset getInternalCharset(String var0) {
      return getCharsetFromName(var0, Charsets.UTF_8);
   }

   private ETLForgeSystemProperty() {
   }
}
