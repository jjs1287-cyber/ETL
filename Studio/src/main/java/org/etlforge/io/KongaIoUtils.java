package org.etlforge.io;

import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.system.SystemUtils;

public final class KongaIoUtils {
   public static final String LINE_BREAK = SystemUtils.LINE_SEPARATOR;
   private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();

   public static boolean close(Closeable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (IOException var2) {
            var2.printStackTrace();
            return false;
         }
      }

      return true;
   }

   public static void write(File var0, String var1, Charset var2) throws IOException {
      Files.write(var1, var0, var2);
   }

   public static void write(File var0, String var1, String var2) throws IOException {
      write(var0, var1, Charset.forName(var2));
   }

   public static void createAndWrite(File var0, String var1) throws IOException {
      createAndWrite(var0, new String[]{var1 != null ? var1 : ""}, false);
   }

   public static void createAndWrite(File var0, String[] var1, boolean var2) throws IOException {
      createAndWrite(var0, var1, DEFAULT_CHARSET.name(), var2);
   }

   public static void createAndWrite(File var0, String[] var1, Charset var2, boolean var3) throws IOException {
      createAndWrite(var0, var1, var2.name(), var3);
   }

   public static void createAndWrite(File var0, String[] var1, String var2, boolean var3) throws IOException {
      BufferedWriter var4 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var0), var2));

      try {
         int var5 = var1.length;
         int var6 = 0;

         for (String var10 : var1) {
            if (var10 == null) {
               var10 = "";
            }

            var4.write(var10);
            if (var3 || ++var6 < var5) {
               var4.newLine();
            }
         }
      } finally {
         var4.close();
      }
   }

   public static String[] readLines(File var0) throws IOException {
      return readLines(var0, Charset.defaultCharset());
   }

   public static String[] readLines(File var0, String var1) throws IOException {
      return readLines(var0, Charset.forName(var1));
   }

   public static String[] readLines(File var0, Charset var1) throws IOException {
      List var2 = Files.readLines(var0, var1);
      return (String[]) var2.toArray(new String[var2.size()]);
   }

   public static String read(File var0) throws IOException {
      return read(var0, DEFAULT_CHARSET);
   }

   public static String read(File var0, String var1) throws IOException {
      return read(var0, Charset.forName(var1));
   }

   public static String read(File var0, Charset var1) throws IOException {
      return Files.toString(var0, var1);
   }

   public static String read(File var0, Charset var1, int var2) throws IOException {
      BufferedReader var3 = new BufferedReader(new InputStreamReader(new FileInputStream(var0), var1));

      try {
         char[] var4 = new char[var2];
         int var5 = var3.read(var4, 0, var2);
         return var5 > 0 ? new String(var4, 0, var5) : "";
      } finally {
         close(var3);
      }
   }

   public static String inputStreamToHexString(InputStream var0) throws IOException {
      return inputStreamToHexString(var0, 1024);
   }

   public static String inputStreamToHexString(InputStream var0, int var1) throws IOException {
      StringBuilder var2 = new StringBuilder();
      byte[] var3 = new byte[var1];
      int var4 = -1;

      while ((var4 = var0.read(var3)) >= 0) {
         var2.append(KongaStringUtils.toHexString(var3, 0, var4));
      }

      return var2.toString();
   }

   private KongaIoUtils() {
   }
}
