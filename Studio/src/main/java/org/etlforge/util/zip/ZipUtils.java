package org.etlforge.util.zip;

import com.google.common.base.Charsets;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.etlforge.io.KongaIoUtils;

public final class ZipUtils {
   private static final int BUFFER_SIZE = 1024;
   private static final boolean DEBUG = false;

   public static void unZip(File var0, File var1) throws KongaZipException {
      unZip(var0.getAbsolutePath(), var1.getAbsolutePath());
   }

   public static String getPlatformIndependentName(ZipEntry var0) {
      return var0.getName().replace('\\', '/');
   }

   public static void unZip(String var0, String var1) throws KongaZipException {
      FileInputStream var2 = null;
      ZipInputStream var3 = null;

      try {
         try {
            var2 = new FileInputStream(var0);
         } catch (FileNotFoundException var26) {
            throw new KongaZipException("Zip file (input) not found. " + var26.getMessage(), var26);
         }

         File var4 = new File(var1);
         if (var4.exists()) {
            if (!var4.isDirectory()) {
               throw new KongaZipException("The base directory \"" + var4.getAbsolutePath() + "\" is not a directory, it is a regular file.");
            }
         } else {
            var4.mkdirs();
         }

         if (!var4.canWrite()) {
            throw new KongaZipException("Cannot write to target directory \"" + var4.getAbsolutePath() + "\". ");
         }

         var1 = var4.getAbsolutePath();
         if (var1.charAt(var1.length() - 1) != File.separatorChar) {
            var1 = var1 + File.separatorChar;
         }

         Object var33 = null;
         var3 = new ZipInputStream(var2);
         byte[] var5 = new byte[1024];
         int var6 = 0;
         Object var7 = null;

         for (ZipEntry var35 = var3.getNextEntry(); var35 != null; var35 = var3.getNextEntry()) {
            if (var35.isDirectory()) {
               File var36 = new File(var1 + getPlatformIndependentName(var35));
               if (var36.exists()) {
                  if (!var36.isDirectory()) {
                     throw new KongaZipException(
                        "A file already exists in the location where a directory must be created. The offending file is \"" + var36.getAbsolutePath() + "\"."
                     );
                  }
               } else if (!var36.mkdir()) {
                  throw new KongaZipException("Could not create the folder " + var36.getAbsolutePath());
               }
            } else {
               File var8 = new File(var1 + getPlatformIndependentName(var35));
               File var9 = var8.getParentFile();
               if (!var9.exists() && !var9.mkdirs()) {
                  throw new KongaZipException("Could not create the folder " + var9.getAbsolutePath());
               }

               FileOutputStream var10 = new FileOutputStream(var8);

               try {
                  while ((var6 = var3.read(var5, 0, 1024)) > -1) {
                     var10.write(var5, 0, var6);
                  }
               } catch (FileNotFoundException var27) {
                  throw new KongaZipException("Could not find the path to write the unzipped file to. This is an internal bug. " + var27.getMessage(), var27);
               } catch (IOException var28) {
                  throw new KongaZipException("I/O Error while writing the unzipped file. " + var28.getMessage(), var28);
               } finally {
                  KongaIoUtils.close(var10);
               }
            }

            var3.closeEntry();
         }
      } catch (IOException var30) {
         throw new KongaZipException("Failed to get the next zipped entry. " + var30.getMessage(), var30);
      } finally {
         KongaIoUtils.close(var3);
         KongaIoUtils.close(var2);
      }
   }

   public static void zip(File var0, File var1) throws KongaZipException {
      zip(var0.getAbsolutePath(), var1.getAbsolutePath());
   }

   public static void zip(String var0, String var1) throws KongaZipException {
      ZipFileWriter var2 = new ZipFileWriter(var1);
      File var3 = new File(var0);
      if (!var3.exists()) {
         throw new KongaZipException("The source directory \"" + var0 + "\" does not exist.");
      }

      if (!var3.isDirectory()) {
         throw new KongaZipException("The source directory \"" + var0 + "\" is not a directory, it is a regular file.");
      }

      try {
         String var4 = var3.getParentFile().getCanonicalPath();
         File[] var5 = var3.listFiles();

         for (File var9 : var5) {
            addToZip(var9, var4, var2);
         }

         var2.closeZipFile();
      } catch (FileNotFoundException var10) {
         throw new KongaZipException("Could not zip the a file. " + var10.getMessage(), var10);
      } catch (IOException var11) {
         throw new KongaZipException("I/O exception while zipping. " + var11.getMessage(), var11);
      }
   }

   private static void addToZip(File var0, String var1, ZipFileWriter var2) throws IOException {
      if (var0.isDirectory()) {
         File[] var3 = var0.listFiles();

         for (File var7 : var3) {
            addToZip(var7, var1, var2);
         }
      } else {
         String var8 = var0.getCanonicalPath();
         if (var8.startsWith(var1)) {
            String var9 = var8.substring(var1.length() + 1);
            var2.add(var8, var9);
         }
      }
   }

   public static List<ZipEntry> getZipEntries(String var0, String var1) throws IOException {
      ArrayList var2 = new ArrayList();
      ZipFile var3 = new ZipFile(var0);

      try {
         Enumeration var4 = var3.entries();

         while (var4.hasMoreElements()) {
            ZipEntry var5 = (ZipEntry)var4.nextElement();
            if (var1 == null || var5.getName().endsWith(var1)) {
               var2.add(var5);
            }
         }
      } finally {
         try {
            var3.close();
         } catch (IOException var11) {
            var11.printStackTrace();
         }
      }

      return var2;
   }

   public static byte[] deflateFileContents(String var0) throws IOException {
      return deflateFileContents(new File(var0));
   }

   public static byte[] deflateFileContents(File var0) throws IOException {
      FileInputStream var1 = new FileInputStream(var0);
      return deflateInputStream(var1);
   }

   public static byte[] deflateInputStream(InputStream var0) throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      Deflater var2 = new Deflater(9);
      DeflaterOutputStream var3 = new DeflaterOutputStream(var1, var2);

      try {
         byte[] var4 = new byte[1024];
         int var5 = 0;

         while ((var5 = var0.read(var4, 0, 1024)) > -1) {
            var3.write(var4, 0, var5);
         }
      } finally {
         KongaIoUtils.close(var0);
         KongaIoUtils.close(var3);
         KongaIoUtils.close(var1);
      }

      return var1.toByteArray();
   }

   public static byte[] deflateReader(Reader var0) throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      IOUtils.copy(var0, var1);
      ByteArrayInputStream var2 = new ByteArrayInputStream(var1.toByteArray());
      return deflateInputStream(var2);
   }

   public static String deflateAndBase64EncodeFileContents(String var0) throws IOException {
      return deflateAndBase64EncodeFileContents(new File(var0));
   }

   public static String deflateAndBase64EncodeFileContents(File var0) throws IOException {
      return base64Encode(deflateFileContents(var0));
   }

   private static String base64Encode(byte[] var0) {
      byte[] var1 = Base64.encodeBase64Chunked(var0);
      return new String(var1, Charsets.US_ASCII);
   }

   private static byte[] base64Decode(String var0) {
      return Base64.decodeBase64(var0.getBytes(Charsets.US_ASCII));
   }

   public static String deflateAndBase64EncodeInputStream(InputStream var0) throws IOException {
      return base64Encode(deflateInputStream(var0));
   }

   public static String deflateAndBase64EncodeString(String var0) throws IOException {
      return deflateAndBase64EncodeString(var0, Charsets.UTF_8);
   }

   public static String deflateAndBase64EncodeString(String var0, Charset var1) throws IOException {
      return deflateAndBase64EncodeBytes(var0.getBytes(var1));
   }

   public static String deflateAndBase64EncodeBytes(byte[] var0) throws IOException {
      return base64Encode(deflateTypes(var0));
   }

   private static byte[] deflateTypes(byte[] var0) throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      DeflaterOutputStream var2 = new DeflaterOutputStream(var1, new Deflater(9));

      try {
         var2.write(var0);
      } finally {
         var2.close();
      }

      return var1.toByteArray();
   }

   public static String base64DecodeAndInflateString(String var0) throws DataFormatException {
      try {
         return base64DecodeAndInflateString(var0, Charsets.UTF_8);
      } catch (UnsupportedEncodingException var2) {
         throw new RuntimeException(var2);
      }
   }

   public static String base64DecodeAndInflateString(String var0, Charset var1) throws DataFormatException, UnsupportedEncodingException {
      byte[] var2 = base64DecodeAndInflateStringAsBytes(var0);
      return new String(var2, var1);
   }

   public static byte[] base64DecodeAndInflateStringAsBytes(String var0) throws DataFormatException {
      byte[] var1 = base64Decode(var0);
      return inflate(var1);
   }

   public static byte[] inflate(byte[] var0) throws DataFormatException {
      Inflater var1 = new Inflater();
      var1.setInput(var0);
      var1.finished();
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      byte[] var3 = new byte[1024];
      int var4 = 0;

      while ((var4 = var1.inflate(var3)) > 0) {
         var2.write(var3, 0, var4);
      }

      return var2.toByteArray();
   }

   public static void inflateToOutputStream(byte[] var0, OutputStream var1) throws IOException {
      InflaterOutputStream var2 = new InflaterOutputStream(var1);
      var2.write(var0);
   }

   public static boolean isLikelyZipped(File var0) {
      FileInputStream var1 = null;

      try {
         var1 = new FileInputStream(var0);
         return isLikelyZipped(var1);
      } catch (IOException var7) {
         var7.printStackTrace();
         return false;
      } finally {
         KongaIoUtils.close(var1);
      }
   }

   public static boolean isLikelyZipped(InputStream var0) {
      try {
         byte[] var1 = new byte[3];
         int var2 = var0.read(var1);
         return var2 == 3 && isLikelyZipped(var1);
      } catch (IOException var3) {
         var3.printStackTrace();
         return false;
      }
   }

   public static boolean isLikelyZipped(byte[] var0) {
      return var0.length <= 2 ? false : var0[0] == 120 && (var0[1] == -38 || var0[1] == -100);
   }

   public static void base64DecodeAndInflateToFile(String var0, File var1) throws IOException {
      byte[] var2 = base64Decode(var0);
      inflateToFile(var2, var1);
   }

   public static void inflateToFile(byte[] var0, File var1) throws IOException {
      BufferedOutputStream var2 = new BufferedOutputStream(new FileOutputStream(var1));

      try {
         inflateToOutputStream(var0, var2);
      } finally {
         var2.close();
      }
   }

   private ZipUtils() {
   }
}
