package org.etlforge.integration.server.engine.connector;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import org.etlforge.integration.data.CC;
import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.integration.server.engine.connector.util.XmlUtils;
import org.etlforge.integration.server.exception.ServerConfigurationException;

public class ConnectorExecutionSystem {
   public static final Charset ConnectorDefaultCharset_UTF8 = Charsets.UTF_8;
   private static final char UTF8BOM = '\ufeff';
   private static final int xmlHeaderCheckLength = 200;

   public static final File getDataInterchangeDir() {
      try {
         return ServerConfig.getDataInterchangeDir();
      } catch (ServerConfigurationException var1) {
         throw new ConnectorServerRuntimeException(var1);
      }
   }

   public static String descryptPassword(String var0) {
      return Strings.isNullOrEmpty(var0) ? var0 : CC.reveal(var0);
   }

   public static boolean isUTF8(Charset var0) {
      return 0 == var0.compareTo(Charsets.UTF_8);
   }

   public static String readFile(String var0, Charset var1) {
      return readFile(new File(var0), var1);
   }

   public static String readFile(File var0, Charset var1) {
      try {
         String var2 = Files.toString(var0, var1);
         boolean var3 = var2.length() > 0 && '\ufeff' == var2.charAt(0) && isUTF8(var1);
         return var3 ? var2.substring(1) : var2;
      } catch (IOException var4) {
         throw new ConnectorServerRuntimeException(var4);
      }
   }

   public static String readFileUTF8(String var0) {
      return readFile(var0, Charsets.UTF_8);
   }

   public static String readFileUTF8(File var0) {
      return readFile(var0, Charsets.UTF_8);
   }

   public static String readFileChunk(String var0, int var1, Charset var2) {
      BufferedReader var3 = null;

      try {
         var3 = new BufferedReader(new InputStreamReader(new FileInputStream(var0), var2));
         char[] var4 = new char[var1];
         int var5 = var3.read(var4);
         boolean var6 = var5 > 0 && '\ufeff' == var4[0] && isUTF8(var2);
         return var6 ? new String(var4, 1, var5) : new String(var4, 0, var5);
      } catch (IOException var11) {
         throw new ConnectorServerRuntimeException(var11);
      } finally {
         close(var3);
      }
   }

   public static String readFileUTF8Chunk(String var0, int var1) {
      return readFileChunk(var0, var1, Charsets.UTF_8);
   }

   public static void writeFile(String var0, String var1, Charset var2) {
      writeFile(new File(var0), var1, var2);
   }

   public static void createFolder(File var0) {
      File var1 = new File(var0, "dummy.txt");
      createParentDirs(var1);
   }

   public static void createParentDirs(File var0) {
      try {
         File var1 = var0.getParentFile();
         if (var1 != null && !var1.exists()) {
            Files.createParentDirs(var0);
         }
      } catch (IOException var2) {
         throw new ConnectorServerRuntimeException(var2);
      }
   }

   public static void writeFile(File var0, String var1, Charset var2) {
      try {
         Files.createParentDirs(var0);
         Files.write(var1, var0, var2);
      } catch (IOException var4) {
         throw new ConnectorServerRuntimeException(var4);
      }
   }

   public static void writeFileUTF8(File var0, String var1) {
      writeFile(var0, var1, Charsets.UTF_8);
   }

   public static void writeFileUTF8(String var0, String var1) {
      writeFile(var0, var1, Charsets.UTF_8);
   }

   public static void copyFileUtf8Xml(String var0, Charset var1, String var2) {
      BufferedReader var3 = null;
      BufferedWriter var4 = null;

      try {
         var3 = Files.newReader(new File(var0), var1);
         var4 = Files.newWriter(new File(var2), Charsets.UTF_8);
         copyFileUtf8Xml(var3, var4);
      } catch (FileNotFoundException var10) {
         throw new ConnectorServerRuntimeException(var10);
      } catch (IOException var11) {
         throw new ConnectorServerRuntimeException(var11);
      } finally {
         close(var3);
         close(var4);
      }
   }

   private static boolean isEof(int var0) {
      if (var0 == 0) {
         throw new ConnectorServerRuntimeException("System Fatal Error: can not read any chars!");
      } else {
         return var0 < 0;
      }
   }

   public static void copyFileUtf8Xml(BufferedReader var0, BufferedWriter var1) throws IOException {
      char[] var2 = new char[8096];
      int var4 = var0.read(var2);
      if (!isEof(var4)) {
         String var5 = new String(var2, 0, var4 < 200 ? var4 : 200);
         int var3 = var5.indexOf("<?xml");
         if (var3 >= 0) {
            var3 = var5.indexOf("?>", var3);
         }

         boolean var6 = var3 > 0;
         if (var6) {
            var3 += 2;
         } else {
            var3 = 0;
         }

         if (var6) {
            var1.write(XmlUtils.xmlUtf8Header);
         }

         int var7 = var4 - var3;
         if (var7 > 0) {
            var1.write(var2, var3, var7);
         }

         while (true) {
            var4 = var0.read(var2);
            if (isEof(var4)) {
               return;
            }

            var1.write(var2, 0, var4);
         }
      }
   }

   public static void close(Closeable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (IOException var2) {
            var2.printStackTrace();
         }
      }
   }
}
