package org.etlforge.integration.jitterpack.impl;

import de.schlichtherle.crypto.io.raes.RaesParameters;
import de.schlichtherle.crypto.io.raes.Type0RaesParameters;
import de.schlichtherle.io.ArchiveDetector;
import de.schlichtherle.io.ArchiveException;
import de.schlichtherle.io.DefaultArchiveDetector;
import de.schlichtherle.io.FileInputStream;
import de.schlichtherle.io.archive.Archive;
import de.schlichtherle.io.archive.zip.Zip32Driver;
import de.schlichtherle.io.archive.zip.raes.SafeZip32RaesDriver;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import org.etlforge.integration.client.project.jitterpack.ExportParameters;
import org.etlforge.integration.client.project.jitterpack.PackingFileFilter;
import org.etlforge.io.FileUtils;

final class TrueZipUtils {
   private static final ArchiveDetector STANDARD = new DefaultArchiveDetector("jpk", new Zip32Driver());
   private static final FileFilter FILES_ONLY = new FileFilter() {
      @Override
      public boolean accept(File var1) {
         return var1.isFile();
      }
   };

   private TrueZipUtils() {
      throw new UnsupportedOperationException();
   }

   static void zip(File var0, File var1, ExportParameters var2) throws ArchiveException, IOException {
      if (var1.exists() && !var1.delete()) {
         throw new IOException("Failed to overwrite existing file.");
      }

      ArchiveDetector var3 = getArchiveDetector(var2.getPassword());
      File var4 = new File(var1, var0.getName());
      de.schlichtherle.io.File var5 = new de.schlichtherle.io.File(var4, var3);
      if (!var5.mkdirs()) {
         throw new IOException("Failed to create the file.");
      }

      PackingFileFilter var6 = var2.getFileFilter();

      for (File var10 : var0.listFiles(FileUtils.DIRECTORIES)) {
         zipFolderImpl(var5, var3, var10, var6);
      }

      for (File var14 : var0.listFiles(FILES_ONLY)) {
         archiveFile(var5, var14);
      }

      de.schlichtherle.io.File.update();
   }

   private static void zipFolderImpl(de.schlichtherle.io.File var0, ArchiveDetector var1, File var2, PackingFileFilter var3) throws IOException {
      PackingFileFilter.Result var4 = var3.evaluateDirectory(var2);
      if (var4 != PackingFileFilter.EXCLUDE) {
         de.schlichtherle.io.File var5 = new de.schlichtherle.io.File(var2.getCanonicalPath());
         de.schlichtherle.io.File var6 = new de.schlichtherle.io.File(var0, var2.getName(), var1);
         var6.mkdirs();
         if (var4 == PackingFileFilter.INCLUDE) {
            var6.archiveCopyAllFrom(var5);
         } else if (var4 == PackingFileFilter.INCLUDE_SELECTIVELY) {
            var6.archiveCopyFrom(var5);

            for (File var10 : var2.listFiles(FileUtils.DIRECTORIES)) {
               zipFolderImpl(var6, var1, var10, var3);
            }

            for (File var14 : var2.listFiles(FILES_ONLY)) {
               if (var3.evaluateFile(var14) == PackingFileFilter.INCLUDE) {
                  archiveFile(var6, var14);
               }
            }
         }
      }
   }

   private static boolean archiveFile(de.schlichtherle.io.File var0, File var1) throws IOException {
      de.schlichtherle.io.File var2 = new de.schlichtherle.io.File(var0, var1.getName());
      return var2.archiveCopyFrom(new de.schlichtherle.io.File(var1.getCanonicalPath()));
   }

   static void unzip(File var0, File var1, char[] var2) throws ArchiveException, InvalidPasswordException {
      ArchiveDetector var3 = getArchiveDetector(var2);
      de.schlichtherle.io.File var4 = new de.schlichtherle.io.File(var0, var3);
      var4.archiveCopyAllTo(var1);
      de.schlichtherle.io.File.update();
   }

   static InputStream getInputStream(File var0, String var1, char[] var2) throws IOException, InvalidPasswordException {
      ArchiveDetector var3 = getArchiveDetector(var2);
      de.schlichtherle.io.File var4 = new de.schlichtherle.io.File(var0, var1, var3);
      return new FileInputStream(var4);
   }

   static InputStream getInputStream(File var0, char[] var1) throws IOException, InvalidPasswordException {
      ArchiveDetector var2 = getArchiveDetector(var1);
      de.schlichtherle.io.File var3 = new de.schlichtherle.io.File(var0, var2);
      return new FileInputStream(var3);
   }

   static ArchiveDetector getArchiveDetector(char[] var0) {
      return var0 == null ? STANDARD : new DefaultArchiveDetector("jpk", new TrueZipUtils.SafeZipRaesDriverWithPassword(var0));
   }

   static {
      de.schlichtherle.io.File.setDefaultArchiveDetector(ArchiveDetector.ALL);
   }

   private static class SafeZipRaesDriverWithPassword extends SafeZip32RaesDriver {
      private final char[] password;

      public SafeZipRaesDriverWithPassword(char[] var1) {
         this.password = var1;
      }

      @Override
      public RaesParameters getRaesParameters(Archive var1) {
         return new Type0RaesParameters() {
            @Override
            public char[] getCreatePasswd() {
               return (char[])SafeZipRaesDriverWithPassword.this.password.clone();
            }

            @Override
            public int getKeyStrength() {
               return 2;
            }

            @Override
            public char[] getOpenPasswd() {
               return (char[])SafeZipRaesDriverWithPassword.this.password.clone();
            }

            @Override
            public void invalidOpenPasswd() {
               throw new InvalidPasswordException();
            }

            @Override
            public void setKeyStrength(int var1) {
            }
         };
      }
   }
}
