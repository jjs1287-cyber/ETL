package org.etlforge.io;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.etlforge.lang.Arrays2;
import org.etlforge.system.callback.Receiver;

public final class Folder {
   public static final Function<File, Folder> FILE_TO_FOLDER = new Function<File, Folder>() {
      public Folder apply(File var1) {
         return new Folder(var1);
      }
   };
   private final File path;

   public static Folder parentOf(File var0) {
      File var1 = var0.getParentFile();
      return var1 != null ? new Folder(var1) : null;
   }

   public Folder(String var1, String var2) {
      this(new File(var1, var2));
   }

   public Folder(File var1, String var2) {
      this(new File(var1, var2));
   }

   public Folder(Folder var1, String... var2) {
      this(var1.asJavaFile(), var2);
   }

   public Folder(File var1, String... var2) {
      File var3 = var1;

      for (String var7 : var2) {
         var3 = new File(var3, var7);
      }

      this.path = var3;
   }

   public Folder(String var1) {
      this(new File(var1));
   }

   public Folder(File var1) {
      this.path = Preconditions.checkNotNull(var1, "path");
   }

   public File asJavaFile() {
      return this.path;
   }

   public String getName() {
      return this.path.getName();
   }

   public String getAbsolutePath() {
      return this.path.getAbsolutePath();
   }

   public String getCanonicalPath() throws IOException {
      return this.path.getCanonicalPath();
   }

   public boolean exists() {
      return this.path.exists();
   }

   public boolean createOnDisk() {
      return this.path.exists() && this.path.isDirectory() ? true : this.path.mkdirs();
   }

   public Folder parent() {
      return parentOf(this.path);
   }

   public boolean contains(File var1) {
      File var2 = var1.getParentFile();
      return var2.equals(this.path);
   }

   public boolean contains(String var1) {
      return this.contains(this.getFile(var1));
   }

   public Folder subFolder(String var1) {
      return new Folder(this.path, var1);
   }

   public File getFile(String var1) {
      return new File(this.path, var1);
   }

   public File[] listFiles() {
      File[] var1 = this.path.listFiles();
      return var1 != null ? var1 : new File[0];
   }

   public File[] listFiles(FileFilter var1) {
      File[] var2 = this.path.listFiles(var1);
      return var2 != null ? var2 : new File[0];
   }

   public File[] listFilesRecursively(FileFilter var1) {
      ArrayList var2 = Lists.newArrayList();
      collectFilesRecursively(this, var1, var2);
      return (File[]) var2.toArray(new File[var2.size()]);
   }

   private static void collectFilesRecursively(Folder var0, FileFilter var1, List<File> var2) {
      for (File var6 : var0.listFiles()) {
         if (var1.accept(var6)) {
            var2.add(var6);
         }

         if (var6.isDirectory()) {
            collectFilesRecursively(new Folder(var6), var1, var2);
         }
      }
   }

   public File[] listNonFolders() {
      return this.listFiles(FileUtils.ALL_FILES);
   }

   public Folder[] listSubFolders() {
      File[] var1 = this.path.listFiles(FileUtils.DIRECTORIES);
      return var1 == null ? new Folder[0] : Arrays2.transform(var1, Folder.class, FILE_TO_FOLDER);
   }

   public boolean isEmpty() {
      return this.listFiles(FileUtils.ACCEPT_ALL).length == 0;
   }

   public boolean delete() {
      if (!this.path.exists()) {
         return true;
      }

      try {
         org.apache.commons.io.FileUtils.deleteDirectory(this.path);
         return true;
      } catch (IOException var2) {
         return false;
      }
   }

   public Set<File> deleteFiles(FileFilter var1) {
      final HashSet var2 = Sets.newHashSet();
      MatchingFilesProcessor var3 = new MatchingFilesProcessor(var1, new Receiver<File>() {
         public void handle(File var1) {
            boolean var2x = var1.delete();
            if (!var2x) {
               var2.add(var1);
            }
         }
      });
      var3.process(this);
      return var2;
   }

   public void copyTo(Folder var1) throws IOException {
      boolean var2 = var1.createOnDisk();
      if (!var2) {
         throw new IOException("Could not create the destination folder " + var1);
      }

      org.apache.commons.io.FileUtils.copyDirectory(this.path, var1.path);
   }

   public File copyFileToThisFolder(File var1) throws IOException {
      org.apache.commons.io.FileUtils.copyFileToDirectory(var1, this.path);
      File var2 = this.getFile(var1.getName());
      if (var2.exists()) {
         return var2;
      } else {
         throw new IOException("Failed to copy the file.");
      }
   }

   public void writeFileInUTF8(String var1, String var2) throws IOException {
      this.writeFile(var1, var2, Charsets.UTF_8);
   }

   public void writeFile(String var1, String var2, Charset var3) throws IOException {
      File var4 = new File(this.path, var1);
      Files.write(var2, var4, var3);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof Folder ? this.path.equals(((Folder)var1).path) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.path.hashCode();
   }

   @Override
   public String toString() {
      return this.path.toString();
   }

   public static File[] listFiles(File var0, FileFilter var1) {
      return new Folder(var0).listFiles(var1);
   }

   public Set<String> getNamesOfFilesAndFolders() {
      HashSet var1 = Sets.newHashSet();

      for (File var5 : this.listFiles()) {
         var1.add(var5.getName());
      }

      return var1;
   }

   public Folder.Stats getStats() {
      return new Folder.Stats(this);
   }

   public static final class Stats {
      private int numberOfFiles;
      private int numberOfFolders;
      private long totalSizeInBytes;

      public Stats(Folder var1) {
         this.processDirectory(var1.path);
      }

      private void processDirectory(File var1) {
         for (File var5 : new Folder(var1).listFiles()) {
            if (var5.isDirectory()) {
               this.numberOfFolders++;
               this.processDirectory(var5);
            } else {
               this.numberOfFiles++;
               this.totalSizeInBytes = this.totalSizeInBytes + var5.length();
            }
         }
      }

      public int getNumberOfFiles() {
         return this.numberOfFiles;
      }

      public int getNumberOfFolders() {
         return this.numberOfFolders;
      }

      public long getTotalSizeInBytes() {
         return this.totalSizeInBytes;
      }

      public boolean isEmpty() {
         return this.numberOfFiles == 0 && this.numberOfFolders == 0;
      }

      @Override
      public String toString() {
         return String.format("%d file(s), %d folder(s), total size: %d b", this.numberOfFiles, this.numberOfFolders, this.totalSizeInBytes);
      }
   }
}
