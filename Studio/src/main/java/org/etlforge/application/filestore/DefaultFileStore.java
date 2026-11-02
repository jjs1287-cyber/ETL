package org.etlforge.application.filestore;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.etlforge.application.errorlog.ErrorLog;

public final class DefaultFileStore implements FileStore {
   private static final String DEPENDENCIES_FILE_NAME = ".dependencies";
   private static final String FILE_ROOT = "root";
   private final DefaultFile rootFile;
   private final String rootPath;
   private FileCollisionResolver collisionResolver;
   private final HashFunction md5 = Hashing.md5();
   private final DependencyManager dependencyManager;

   public DefaultFileStore(File var1) {
      Preconditions.checkNotNull(var1, "location");
      File var2 = new File(var1, "root");
      this.ensureRootExists(var2);
      this.rootFile = new DefaultFile(null, var2.getAbsolutePath());
      this.rootPath = this.getRootPath(var2);
      this.dependencyManager = this.loadDependencies(var1);
      this.buildStore(this.rootFile);
   }

   private String getRootPath(File var1) {
      String var2 = var1.getAbsolutePath();
      if (!var2.endsWith(File.separator)) {
         var2 = var2 + File.separator;
      }

      return var2;
   }

   private void ensureRootExists(File var1) {
      if (!var1.exists()) {
         boolean var2 = var1.mkdirs();
         if (!var2) {
         }
      }
   }

   private DependencyManager loadDependencies(File var1) {
      File var2 = new File(var1, ".dependencies");
      return new XmlBasedDependencyManager(var2);
   }

   private void buildStore(DefaultFile var1) {
      File var2 = var1.getPhysicalRepresentation();
      if (var2.isDirectory()) {
         for (File var6 : var2.listFiles()) {
            String var7 = var6.getAbsolutePath().substring(this.rootPath.length());
            DefaultFile var8 = this.createFile(var1, var7);
            this.buildStore(var8);
         }
      }
   }

   private DefaultFile createFile(DefaultFile var1, String var2) {
      return new DefaultFile(var1, var2);
   }

   @Override
   public FileStoreFile getRoot() {
      return this.rootFile;
   }

   public DefaultFile getFile(String var1) {
      return this.rootFile.findChild(FileStoreUtils.formatPath(var1));
   }

   @Override
   public void deleteAll(boolean var1) throws FileStoreException {
      this.deleteAllImpl(this.rootFile);
      if (!var1) {
         this.deleteFile(this.rootFile);
      }

      this.dependencyManager.deleteAll();
   }

   private void deleteAllImpl(DefaultFile var1) throws FileStoreException {
      Iterator var2 = var1.getChildren();

      while (var2.hasNext()) {
         DefaultFile var3 = (DefaultFile)var2.next();
         this.deleteAllImpl(var3);
         this.deleteFile(var3);
      }
   }

   private void deleteFile(DefaultFile var1) throws FileStoreException {
      File var2 = var1.getPhysicalRepresentation();
      if (var2.exists() && !var2.delete()) {
         throw new FileStoreException("Failed to delete the old file " + var1.getPhysicalRepresentation().getAbsolutePath() + ".");
      }

      var1.setParent(null);
   }

   @Override
   public void addDependency(String var1, String var2) {
      this.dependencyManager.add(var1, var2);
   }

   @Override
   public List<? extends FileStoreFile> getRequiredFiles(String var1) {
      ArrayList var2 = new ArrayList();
      this.collectRequiredFiles(var1, var2);
      return var2;
   }

   private void collectRequiredFiles(String var1, List<FileStoreFile> var2) {
      HashSet var3 = new HashSet();
      var3.add(var1);
      this.collectRequiredFilePaths(var1, var3);

      for (Object __etlItem131 : var3) {
         String var5 = (String) __etlItem131;
         if (!var5.equals(var1)) {
            DefaultFile var6 = this.getFile(var5);
            if (var6 != null) {
               var2.add(var6);
            }
         }
      }
   }

   private void collectRequiredFilePaths(String var1, Set<String> var2) {
      for (String var4 : this.dependencyManager.getRequiredFiles(var1)) {
         if (!var2.contains(var4)) {
            var2.add(var4);
         }
      }
   }

   @Override
   public FileStoreFile addFile(String var1, String var2, FileCollisionResolver var3) throws FileStoreException {
      return this.addFile(var1, var2.getBytes(Charsets.UTF_8), var3);
   }

   @Override
   public FileStoreFile addFile(String var1, byte[] var2, FileCollisionResolver var3) throws FileStoreException {
      String var4 = FileStoreUtils.formatPath(var1);
      String var5 = new File(var4).getParent();
      DefaultFile var6 = this.getParentCreateIfNotExist(var5);
      this.ensureFolderExistsOnDisk(var6);
      DefaultFile var7 = new DefaultFile(null, var4);
      DefaultFile var8 = this.checkIfFileExists(this.rootFile, var4);
      if (var8 == null) {
         var7.setParent(var6);
         this.createFileOnDisk(var7, var2);
         return var7;
      } else {
         return this.resolveCollision(var8, var7, var6, var2, var3);
      }
   }

   private DefaultFile getParentCreateIfNotExist(String var1) {
      if (var1 != null && var1.length() != 0) {
         DefaultFile var2 = this.rootFile;
         StringTokenizer var3 = new StringTokenizer(var1, File.separator, true);
         StringBuilder var4 = new StringBuilder();

         while (var3.hasMoreTokens()) {
            String var5 = var3.nextToken();
            var4.append(var5);
            if (var5.charAt(0) != File.separatorChar) {
               DefaultFile var6 = var2.findChild(var5);
               if (var6 == null) {
                  var6 = this.createFile(var2, var4.toString());
               }

               var2 = var6;
            }
         }

         return var2;
      } else {
         return this.rootFile;
      }
   }

   private void ensureFolderExistsOnDisk(DefaultFile var1) throws FileStoreException {
      File var2 = var1.getPhysicalRepresentation();
      if (var2.exists()) {
         if (!var2.isDirectory()) {
            throw new FileStoreException(
               "The path " + var2.getAbsolutePath() + " was expected to represent a folder, but the corresponding location on disk is a file."
            );
         }
      } else {
         if (!var2.mkdirs()) {
            throw new FileStoreException("Failed to create the folder " + var2.getAbsolutePath() + ".");
         }
      }
   }

   private DefaultFile checkIfFileExists(DefaultFile var1, String var2) {
      DefaultFile var3 = var1.findChild(var2);
      return var3 != null && var3.getPhysicalRepresentation().exists() ? var3 : null;
   }

   private void createFileOnDisk(DefaultFile var1, byte[] var2) throws FileStoreException {
      try {
         Files.write(var2, var1.getPhysicalRepresentation());
      } catch (IOException var4) {
         throw new FileStoreException("Failed to create the file " + var1.getPhysicalRepresentation().getAbsolutePath() + ".", var4);
      }
   }

   private FileStoreFile resolveCollision(DefaultFile var1, DefaultFile var2, DefaultFile var3, byte[] var4, FileCollisionResolver var5) throws FileStoreException {
      if (this.isIdentical(var1, var4)) {
         return var1;
      }

      FileCollisionResolver var6 = var5 != null ? var5 : this.getCollisionResolver();
      FileCollisionResolution var7 = var6.resolve(var1, var4);
      switch (var7.getAction()) {
         case USE_EXISTING:
            return var1;
         case REPLACE_EXISTING:
            return this.onReplaceExistingFile(var1, var2, var3, var4);
         case RENAME_AND_USE_NEW:
            return this.onRenameNewFile(var7.getNewName(), var3, var4, var6);
         default:
            return null;
      }
   }

   private boolean isIdentical(DefaultFile var1, byte[] var2) throws FileStoreException {
      try {
         byte[] var3 = this.md5.hashBytes(var2).asBytes();
         return var1.hasIdenticalContents(var3);
      } catch (Exception var4) {
         throw new FileStoreException(var4);
      }
   }

   private FileStoreFile onReplaceExistingFile(DefaultFile var1, DefaultFile var2, DefaultFile var3, byte[] var4) throws FileStoreException {
      this.deleteFile(var1);
      var2.setParent(var3);
      this.createFileOnDisk(var2, var4);
      return var2;
   }

   private FileStoreFile onRenameNewFile(String var1, DefaultFile var2, byte[] var3, FileCollisionResolver var4) throws FileStoreException {
      String var5 = var2.getPath() + File.separator + var1;
      return this.addFile(var5, var3, var4);
   }

   @Override
   public FileStoreFile addFile(File var1, String var2, FileCollisionResolver var3) throws FileStoreException {
      try {
         byte[] var4 = Files.toByteArray(var1);
         return this.addFile(var2, var4, var3);
      } catch (IOException var5) {
         throw new FileStoreException("Failed to add the file " + var2 + ".", var5);
      }
   }

   private FileCollisionResolver getCollisionResolver() {
      if (this.collisionResolver == null) {
         this.collisionResolver = this.createCollisionResolver();
      }

      return this.collisionResolver;
   }

   protected FileCollisionResolver createCollisionResolver() {
      return new DefaultFileStore.DefaultCollisionResolver();
   }

   public void report(PrintStream var1) {
      this.report(this.rootFile, var1, "");
   }

   private void report(FileStoreFile var1, PrintStream var2, String var3) {
      var2.println(var3 + var1.getPath());
      String var4 = var3 + "    ";
      Iterator var5 = var1.getChildren();

      while (var5.hasNext()) {
         this.report((FileStoreFile)var5.next(), var2, var4);
      }
   }

   @Override
   public boolean deleteFile(String var1) {
      DefaultFile var2 = this.getFile(var1);
      if (var2 == null) {
         return true;
      }

      try {
         this.deleteFile(var2);
         return true;
      } catch (FileStoreException var4) {
         ErrorLog.log(this.getClass(), null, var4);
         return false;
      }
   }

   @Override
   public void copyDependencies(Map<String, String> var1) throws IOException {
      this.dependencyManager.copy(var1);
   }

   private static class DefaultCollisionResolver implements FileCollisionResolver {
      private DefaultCollisionResolver() {
      }

      @Override
      public FileCollisionResolution resolve(FileStoreFile var1, String var2) {
         return FileCollisionResolver.USE_EXISTING.resolve(var1, var2);
      }

      @Override
      public FileCollisionResolution resolve(FileStoreFile var1, byte[] var2) {
         return FileCollisionResolver.USE_EXISTING.resolve(var1, var2);
      }
   }
}
