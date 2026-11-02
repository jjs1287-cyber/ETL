package org.etlforge.integration.server.engine.connector.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;

public class ContentExtractorByte {
   private static final String STARTTGASTRING = "<![CDATA[";
   private static final String ENDTAGSTRING = "]]>";
   private static final int BLOCKSIZE = 9180;
   private static final Charset UTF8CHARSET = Charset.forName("UTF-8");
   private static final byte[] STARTTAG = "<![CDATA[".getBytes(UTF8CHARSET);
   private static final byte[] ENDTAG = "]]>".getBytes(UTF8CHARSET);

   private static int locatePosition(byte[] var0, byte[] var1, long var2) {
      if (var2 > var1.length) {
         var2 = var1.length;
      }

      for (int var5 = 0; var5 < var2 - var0.length; var5++) {
         if (var0[0] == var1[var5]) {
            boolean var4 = true;

            for (int var6 = 1; var6 < var0.length; var6++) {
               if (var0[var6] != var1[var5 + var6]) {
                  var4 = false;
                  break;
               }
            }

            if (var4) {
               return var5;
            }
         }
      }

      return -1;
   }

   private static long locatePosition(byte[] var0, int var1, boolean var2, File var3) {
      byte[] var4 = new byte[var1];
      BufferedInputStream var5 = null;

      try {
         var5 = new BufferedInputStream(new FileInputStream(var3));
         long var6 = var2 ? var3.length() - var1 : 0L;
         skipInputStream(var6, var5);
         int var8 = readInputStream(var4, var5);
         int var9 = locatePosition(var0, var4, var8);
         if (var9 < -1) {
            return -1L;
         } else {
            return var6 > 0L ? var6 + var9 : var9;
         }
      } catch (FileNotFoundException var16) {
         throw new ConnectorServerRuntimeException(var16);
      } catch (IOException var17) {
         throw new ConnectorServerRuntimeException(var17);
      } finally {
         ConnectorExecutionSystem.close(var5);
      }
   }

   public static void extract(String var0, String var1) {
      extract(STARTTAG, ENDTAG, 9180, var0, var1);
   }

   private static void extract(byte[] var0, byte[] var1, int var2, String var3, String var4) {
      File var5 = new File(var3);
      long var6 = locatePosition(var0, var2, false, var5);
      long var8 = locatePosition(var1, var2, true, var5);
      if (var6 >= 0L) {
         var6 += var0.length;
      }

      long var10 = var8 - var6;
      extract(var6, var10, var3, var4);
   }

   private static void extract(long var0, long var2, String var4, String var5) {
      BufferedInputStream var6 = null;

      try {
         var6 = new BufferedInputStream(new FileInputStream(new File(var4)));
         extract(var0, var2, var6, var5);
      } catch (FileNotFoundException var12) {
         throw new ConnectorServerRuntimeException(var12);
      } catch (IOException var13) {
         throw new ConnectorServerRuntimeException(var13);
      } finally {
         ConnectorExecutionSystem.close(var6);
      }
   }

   private static int readInputStream(byte[] var0, BufferedInputStream var1) throws IOException {
      int var2 = var1.read(var0);
      if (var2 != -1 && var2 != var0.length) {
         for (int var3 = var0.length - var2; var3 > 0; var3 = var0.length - var2) {
            int var4 = var1.read(var0, var2, var3);
            if (var4 <= 0) {
               break;
            }

            var2 += var4;
         }

         return var2;
      } else {
         return var2;
      }
   }

   private static void skipInputStream(long var0, BufferedInputStream var2) throws IOException {
      if (var0 >= 0L) {
         while (var0 > 0L) {
            long var3 = var2.skip(var0);
            if (var3 <= 0L) {
               break;
            }

            var0 -= var3;
         }
      }
   }

   private static void extract(long var0, long var2, BufferedInputStream var4, String var5) throws IOException {
      BufferedOutputStream var6 = null;

      try {
         var6 = new BufferedOutputStream(new FileOutputStream(var5));
         if (var0 < 0L || var2 <= 0L) {
            return;
         }

         skipInputStream(var0, var4);

         for (long var7 = var0 + var2; var0 < var7; var0++) {
            int var9 = var4.read();
            if (var9 == -1) {
               break;
            }

            var6.write(var9);
         }
      } catch (FileNotFoundException var13) {
         var13.printStackTrace();
         throw new ConnectorServerRuntimeException(var13);
      } finally {
         ConnectorExecutionSystem.close(var6);
      }
   }

   public static void main(String[] var0) {
      String var1 = "Z:/etlforge/branches/v5/5.1.x/dev/ETLForgeConnectorProject/MSAX/Request&Response Samples/";
      String var2 = var1 + "ExecuteOperation_Response.xml";
      String var3 = var1 + "ExecuteOperation_Response_extractor.txt";
      extract(var2, var3);
   }
}
