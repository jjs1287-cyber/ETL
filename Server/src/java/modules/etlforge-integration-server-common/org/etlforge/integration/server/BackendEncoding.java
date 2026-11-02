package org.etlforge.integration.server;

import com.google.common.base.Charsets;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.integration.server.conf.ServerConfig;

public final class BackendEncoding {
   private static final Charset LINUX = Charset.forName("ISO-8859-1");
   public static final Charset CHARSET = getDefaultCharset();
   public static final Charset AXIS_CHARSET = getSystemCharset();

   private static final Charset getSystemCharset() {
      return Charsets.UTF_8;
   }

   private static Charset getDefaultCharset() {
      return Charsets.UTF_8;
   }

   public static Charset getCharsetForReading() {
      return CHARSET;
   }

   public static Charset getCharsetForWriting() {
      return CHARSET;
   }

   private static Charset getCharset(String var0) {
      Charset var1 = getCharsetFromServerConfig(var0);
      if (var1 == null) {
         var1 = getDefaultCharset();
      }

      return var1;
   }

   private static Charset getCharsetFromServerConfig(String var0) {
      try {
         String var1 = ServerConfig.getOption("JavaBackend", var0, null);
         if (var1 != null) {
            return Charset.forName(var1);
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      return null;
   }

   public static Reader openFileForReading(File var0) throws IOException {
      Charset var1 = getCharsetForReading();
      return new InputStreamReader(new FileInputStream(var0), var1);
   }

   public static Writer openFileForWriting(File var0) throws IOException {
      Charset var1 = getCharsetForWriting();
      return new OutputStreamWriter(new FileOutputStream(var0), var1);
   }
}
