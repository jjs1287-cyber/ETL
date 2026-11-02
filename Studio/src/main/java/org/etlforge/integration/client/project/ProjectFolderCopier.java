package org.etlforge.integration.client.project;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.io.FileUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.io.Folder;

public final class ProjectFolderCopier {
   private final File projectLocation;
   private final File target;
   private boolean multiThread;

   public ProjectFolderCopier(File var1, File var2) {
      this.projectLocation = var1;
      this.target = var2;
   }

   public void setMultiThread(boolean var1) {
      this.multiThread = var1;
   }

   public File doCopy() throws IOException {
      try {
         this.target.mkdirs();
         if (this.multiThread) {
            this.doMultiThreadedCopying();
         } else {
            this.copyProjectDefinitions();
            this.copyOtherProjectFiles();
         }

         return this.target;
      } catch (IOException var2) {
         this.deleteFolder();
         throw var2;
      }
   }

   private void doMultiThreadedCopying() throws IOException {
      ExecutorService var1 = Executors.newFixedThreadPool(2);

      try {
         Future var2 = var1.submit(new ProjectFolderCopier.ProjectDefinitionsCopier());
         Future var3 = var1.submit(new ProjectFolderCopier.OtherProjectFilesCopier());
         this.waitForCompletion(var2);
         this.waitForCompletion(var3);
      } finally {
         var1.shutdownNow();
      }
   }

   private void waitForCompletion(Future<Void> var1) throws IOException, RuntimeException {
      try {
         var1.get();
      } catch (InterruptedException var4) {
         Thread.currentThread().interrupt();
      } catch (ExecutionException var5) {
         Throwable var3 = var5.getCause();
         if (var3 instanceof IOException) {
            throw (IOException)var3;
         }

         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         }

         throw new RuntimeException(var3);
      }
   }

   private void copyProjectDefinitions() throws IOException {
      Folder var1 = new Folder(this.projectLocation);
      File[] var2 = var1.listNonFolders();
      Folder var3 = ProjectFolders.getProjectDataFolder(var1.asJavaFile());
      Folder var4 = ProjectFolders.getProjectDataFolder(this.target);
      IntegrationProjectLock.acquire();

      try {
         for (File var8 : var2) {
            FileUtils.copyFileToDirectory(var8, this.target);
         }

         if (var3.exists()) {
            var3.copyTo(var4);
         }
      } finally {
         IntegrationProjectLock.release();
      }
   }

   private void copyOtherProjectFiles() throws IOException {
      Folder var1 = new Folder(this.projectLocation);
      Folder var2 = ProjectFolders.getProjectDataFolder(var1.asJavaFile());

      for (Folder var6 : var1.listSubFolders()) {
         if (!var6.equals(var2)) {
            boolean var7 = var6.getName().equals("cache");
            if (var7) {
               if (this.multiThread) {
                  this.copyCacheFolderMultiThreaded(var6);
               } else {
                  this.copyCacheFolderSingleThreaded(var6);
               }
            } else {
               Folder var8 = new Folder(this.target, var6.getName());
               var6.copyTo(var8);
            }
         }
      }
   }

   private void copyCacheFolderMultiThreaded(Folder var1) throws IOException {
      Folder var2 = new Folder(this.target, var1.getName());
      Folder var3 = var1.subFolder("TransformationStructures");
      Folder var4 = var1.subFolder("WebServiceCallStructures");
      var2.createOnDisk();
      ProjectFolderCopier.ChunkedFolderCopier var5 = new ProjectFolderCopier.ChunkedFolderCopier(var3, var2.subFolder(var3.getName()));
      var5.copy();
      var4.copyTo(var2.subFolder(var4.getName()));
   }

   private void copyCacheFolderSingleThreaded(Folder var1) throws IOException {
      Folder var2 = new Folder(this.target, var1.getName());

      for (Folder var6 : var1.listSubFolders()) {
         if (!var6.getName().equals("session")) {
            var6.copyTo(var2.subFolder(var6.getName()));
         }
      }

      for (File var10 : var1.listNonFolders()) {
         var2.copyFileToThisFolder(var10);
      }
   }

   private void deleteFolder() {
      try {
         org.etlforge.io.FileUtils.deleteFilesRecursively(this.target);
         FileUtils.deleteDirectory(this.target);
      } catch (IOException var2) {
         ErrorLog.internal(ProjectFolderCopier.class, "Failed to delete partial project copy", var2);
      }
   }

   private class ChunkedFolderCopier {
      private static final int CHUNK_SIZE = 10;
      private final Folder source;
      private final Folder target;

      public ChunkedFolderCopier(Folder var2, Folder var3) {
         this.source = var2;
         this.target = var3;
         var3.createOnDisk();
      }

      public void copy() throws IOException {
         ExecutorService var1 = Executors.newFixedThreadPool(8);

         try {
            ArrayList var2 = Lists.newArrayList();
            ArrayList var3 = Lists.newArrayList(this.source.listFiles());

            for (byte var4 = 0; var4 < var3.size(); var4 += 10) {
               List var5 = var3.subList(var4, Math.min(var3.size(), var4 + 10));
               Future var6 = var1.submit(new ProjectFolderCopier.FileBatchCopier(this.target, var5));
               var2.add(var6);
            }

            for (Object __etlItem179 : var2) {
               Future var11 = (Future) __etlItem179;
               ProjectFolderCopier.this.waitForCompletion(var11);
            }
         } finally {
            var1.shutdownNow();
         }
      }
   }

   private static class FileBatchCopier implements Callable<Void> {
      private final Folder destination;
      private final List<File> files;

      public FileBatchCopier(Folder var1, List<File> var2) {
         this.destination = var1;
         this.files = var2;
      }

      public Void call() throws Exception {
         for (File var2 : this.files) {
            FileUtils.copyFileToDirectory(var2, this.destination.asJavaFile());
         }

         return null;
      }
   }

   private class OtherProjectFilesCopier implements Callable<Void> {
      private OtherProjectFilesCopier() {
      }

      public Void call() throws Exception {
         ProjectFolderCopier.this.copyOtherProjectFiles();
         return null;
      }
   }

   private class ProjectDefinitionsCopier implements Callable<Void> {
      private ProjectDefinitionsCopier() {
      }

      public Void call() throws Exception {
         ProjectFolderCopier.this.copyProjectDefinitions();
         return null;
      }
   }
}
