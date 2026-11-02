package org.etlforge.io;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

public final class FileUtils {
   public static final FileFilter DIRECTORIES = new FileFilter() {
      @Override
      public boolean accept(File var1) {
         return var1.isDirectory();
      }
   };
   public static final FileFilter ALL_FILES = new FileFilter() {
      @Override
      public boolean accept(File var1) {
         return var1.isFile();
      }
   };
   public static final FileFilter ACCEPT_ALL = new FileFilter() {
      @Override
      public boolean accept(File var1) {
         return true;
      }
   };
   private static final char[] ILLEGAL_CHARS = new char[]{'\\', '/', ':', '?', '*', '"', '<', '>', '|'};

   public static String getExtension(File var0) {
      return getExtension(var0, false);
   }

   public static String getExtension(File var0, boolean var1) {
      String var2 = StringUtils.substringAfterLast(var0.getName(), ".").toLowerCase();
      if (var2.isEmpty()) {
         return null;
      } else {
         return var1 ? "." + var2 : var2;
      }
   }

   public static String getExtension(String var0, boolean var1) {
      File var2 = new File(var0);
      return getExtension(var2, var1);
   }

   public static String getFileDir(File var0) {
      if (var0.isDirectory()) {
         return var0.getPath() + File.separator;
      } else {
         return var0.isFile() && var0.getParentFile() != null ? var0.getParent() + File.separator : null;
      }
   }

   public static String stripExtension(File var0) {
      return FilenameUtils.removeExtension(var0.getName());
   }

   public static int countFilesRecursively(File var0, FileFilter var1) {
      int var2 = 0;
      if (var0.isDirectory()) {
         for (File var6 : var0.listFiles(var1)) {
            if (var6.isDirectory()) {
               var2 += countFilesRecursively(var6, var1);
            } else {
               var2++;
            }
         }
      }

      return var2;
   }

   public static void deleteFilesRecursively(File var0) {
      for (File var4 : var0.listFiles()) {
         if (var4.isDirectory()) {
            deleteFilesRecursively(var4);
         }

         var4.delete();
      }
   }

   public static File getRootFolder(File var0) {
      File var1 = var0.getParentFile();
      if (var1 == null) {
         return var0;
      }

      for (File var2 = var1.getParentFile(); var2 != null; var2 = var2.getParentFile()) {
         var1 = var2;
      }

      return var1;
   }

   public static void cleanFolderStructure(File var0, boolean var1) throws IOException {
      if (var0.isDirectory()) {
         File[] var2 = var0.listFiles(FileFilters.DIRECTORIES_ONLY);
         if (var2 != null) {
            for (File var6 : var2) {
               cleanFolderStructure(var6, var1);
            }
         }

         if (var1) {
            File[] var8 = var0.listFiles(FileFilters.FILES_ONLY);
            if (var8 != null) {
               for (File var7 : var8) {
                  org.apache.commons.io.FileUtils.forceDelete(var7);
               }
            }
         } else {
            org.apache.commons.io.FileUtils.cleanDirectory(var0);
         }
      }
   }

   public static String base64EncodeFile(File var0) throws IOException {
      byte[] var1 = Base64.encodeBase64Chunked(Files.toByteArray(var0));
      return new String(var1, Charsets.UTF_8);
   }

   public static String getSafeFileName(String var0, char var1) {
      Preconditions.checkArgument(var1 != 0, "replacement cannot be 0");
      String var2 = var0;

      for (char var6 : ILLEGAL_CHARS) {
         Preconditions.checkArgument(var6 != var1, "The replacement character is not allowed");
         var2 = var2.replace(var6, var1);
      }

      return var2;
   }

   public static boolean isFolderEmpty(File var0) {
      if (!var0.isDirectory()) {
         throw new IllegalArgumentException(var0 + " is not a folder");
      }

      String[] var1 = var0.list();
      return var1 == null || var1.length == 0;
   }

   public static String joinPath(String... var0) {
      return join(var0).getAbsolutePath();
   }

   public static File join(String... var0) {
      File var1 = new File(var0[0]);
      return var0.length > 1 ? appendToPath(var1, Arrays.copyOfRange(var0, 1, var0.length)) : var1;
   }

   public static File appendToPath(File var0, String... var1) {
      File var2 = var0;

      for (String var6 : var1) {
         var2 = new File(var2, var6);
      }

      return var2;
   }

   public static String leftTrimPath(File var0, int var1) {
      String var2 = var0.getAbsolutePath();
      int var3 = var2.length();
      if (var2.length() <= var1) {
         return var2;
      }

      String var4 = "...";
      int var5 = var3 - var1 + var4.length();
      int var6 = var2.indexOf(File.separator, var5);
      if (var6 >= 0) {
         var5 = var6;
      }

      return var4 + var2.substring(var5);
   }

   public static String getNewPostfixFilename(String var0, String var1, String var2) {
      String var3 = getPostfixFilename(var0, var1, var2);
      Integer var4 = 0;

      for (File var5 = new File(var3); var5.exists(); var5 = new File(var3)) {
         var4 = var4 + 1;
         var3 = getPostfixFilename(var0, var1 + "_" + var4, var2);
      }

      return var3;
   }

   public static String getNewPostfixFilename(String var0, String var1) {
      return getNewPostfixFilename(var0, var1, null);
   }

   public static String getPostfixFilename(String var0, String var1) {
      return getPostfixFilename(var0, var1, null);
   }

   public static String getFilenameWithoutExtension(String var0) {
      String var1 = getExtension(var0, true);
      return Strings.isNullOrEmpty(var1) ? var0 : var0.substring(0, var0.length() - var1.length());
   }

   public static String getPostfixFilenameWithSpecifiedExtension(String var0, String var1, String var2) {
      String var3 = getFilenameWithoutExtension(var0) + var1;
      return Strings.isNullOrEmpty(var2) ? var3 : var3 + var2;
   }

   public static String getPostfixFilenameXml(String var0, String var1) {
      return getPostfixFilenameWithSpecifiedExtension(var0, var1, ".xml");
   }

   public static String getPostfixFilename(String var0, String var1, String var2) {
      String var3 = getExtension(var0, true);
      String var4;
      if (Strings.isNullOrEmpty(var3)) {
         if (Strings.isNullOrEmpty(var2)) {
            var4 = var0 + var1;
         } else {
            var4 = var0 + var1 + var2;
         }
      } else {
         var4 = var0.substring(0, var0.length() - var3.length()) + var1 + var3;
      }

      return var4;
   }

   public static boolean createFileDirIfNotExists(String var0, boolean var1) {
      File var2 = new File(var0);
      return createFileDirIfNotExists(var2, var1);
   }

   public static boolean createFileDirIfNotExists(File var0, boolean var1) {
      if (var0.getParentFile() == null) {
         return true;
      }

      if (var0.getParentFile().exists()) {
         if (var1 && var0.exists() && var0.isFile()) {
            var0.delete();
         }

         return true;
      } else {
         return var0.getParentFile().mkdirs();
      }
   }

   public static boolean moveFile(String var0, String var1, boolean var2) throws IOException {
      return moveFile(new File(var0), new File(var1), var2);
   }

   public static boolean moveFile(File var0, File var1, boolean var2) throws IOException {
      if (var1.exists()) {
         if (var0.exists()) {
            if (!var2) {
               return false;
            }

            if (!var0.delete()) {
               throw new IOException("File " + var0.getAbsolutePath() + " cannot be overwritten!");
            }
         }

         return var1.renameTo(var0);
      } else {
         return false;
      }
   }

   public static boolean appendFile(String var0, String var1) throws IOException {
      File var2 = new File(var1);
      if (!var2.exists()) {
         return false;
      }

      if (!createFileDirIfNotExists(var0, false)) {
         return false;
      }

      File var3 = new File(var0);
      Files.copy(var2, Files.newOutputStreamSupplier(var3, true));
      return true;
   }

   public static char[] illegalChars() {
      return (char[])ILLEGAL_CHARS.clone();
   }

   public static Iterable<String> listNames(File var0) {
      String[] var1 = var0.list();
      return var1 == null ? Collections.emptyList() : ImmutableList.copyOf(var1);
   }

   private FileUtils() {
   }
}
