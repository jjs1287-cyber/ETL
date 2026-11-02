package org.etlforge.integration.jitterpack.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.etlforge.application.Application;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.project.jitterpack.ImportParameters;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.JitterPackConstants;
import org.etlforge.integration.client.project.jitterpack.JitterPackDescriptor;
import org.etlforge.integration.client.project.jitterpack.JitterPackLocator;
import org.etlforge.integration.client.project.jitterpack.JitterPackLocatorCallback;
import org.etlforge.integration.client.project.jitterpack.VersionInfo;
import org.etlforge.io.Folder;
import org.etlforge.util.progress.ProgressEvent;
import org.etlforge.util.version.Version;

public final class DefaultJitterPackLocator implements JitterPackLocator {
   private ExecutorService notifierThread;
   private boolean running;
   private JitterPackLocatorCallback callback;
   private DefaultJitterPackLocator.FileTraverser fileTraverser;

   @Override
   public synchronized void search(File var1, boolean var2, ImportParameters var3, boolean var4, JitterPackLocatorCallback var5) {
      if (!this.running) {
         this.running = true;
         this.callback = var5;
         this.notifierThread = Executors.newFixedThreadPool(1);
         this.fileTraverser = new DefaultJitterPackLocator.FileTraverser(var1, var2, var3, var4);
         ApplicationWorker var6 = Application.getWorker();
         var6.submitForParallel(ApplicationWorker.GUARD.protect(this.fileTraverser));
      }
   }

   @Override
   public synchronized void stop() {
      if (this.running) {
         this.fileTraverser.cancel();
         this.notifierThread.shutdown();
         this.running = false;
      }
   }

   private void progressStarted() {
      ProgressEvent var1 = new ProgressEvent(this, "");
      this.callback.progressStarted(var1);
   }

   private void progressEnded() {
      ProgressEvent var1 = new ProgressEvent(this, "");
      this.callback.progressEnded(var1);
   }

   private void updateStatus(File var1, int var2) {
      float var3 = -1.0F;
      ProgressEvent var4 = new ProgressEvent(this, var3, "Searching in: " + var1.getAbsolutePath(), 0);
      this.callback.progressUpdated(var4);
   }

   private class FileExaminer implements Runnable {
      private final File file;
      private final ImportParameters params;
      private final Version version;

      public FileExaminer(File var2, ImportParameters var3, Version var4) {
         this.file = var2;
         this.params = var3;
         this.version = var4;
      }

      @Override
      public void run() {
         JitterPack var1 = JitterPackUtils.createFromFileSilently(this.file, this.version, this.params);
         if (var1 != null) {
            DefaultJitterPackLocator.this.callback.locatedJitterPack(var1);
         }
      }
   }

   private class FileTraverser implements Runnable {
      private final File root;
      private final boolean recursively;
      private final ImportParameters params;
      private final FileFilter fileFilter;
      private int numberOfCheckedFolders;
      private boolean canceled;

      public FileTraverser(File var2, boolean var3, ImportParameters var4, boolean var5) {
         this.root = var2;
         this.recursively = var3;
         this.params = var4;
         this.fileFilter = var5 ? JitterPackConstants.FILE_FILTER : null;
      }

      @Override
      public void run() {
         if (!this.isCancelled()) {
            DefaultJitterPackLocator.this.progressStarted();
            this.traverseFileTree(this.root);
            if (!this.isCancelled()) {
               this.waitForNotifierThread();
               DefaultJitterPackLocator.this.progressEnded();
            }
         }
      }

      private void waitForNotifierThread() {
         DefaultJitterPackLocator.this.notifierThread.shutdown();

         try {
            DefaultJitterPackLocator.this.notifierThread.awaitTermination(10L, TimeUnit.SECONDS);
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
            var2.printStackTrace();
         }
      }

      private void traverseFileTree(File var1) {
         if (!this.isCancelled()) {
            DefaultJitterPackLocator.this.updateStatus(var1, ++this.numberOfCheckedFolders);
            ArrayList var2 = null;

            for (File var6 : new Folder(var1).listFiles(this.fileFilter)) {
               if (var6.isDirectory()) {
                  if (this.recursively) {
                     if (var2 == null) {
                        var2 = new ArrayList();
                     }

                     var2.add(var6);
                  }
               } else {
                  try {
                     this.checkFile(var6);
                  } catch (IOException var8) {
                     var8.printStackTrace();
                  }
               }

               if (this.isCancelled()) {
                  return;
               }
            }

            if (var2 != null) {
               for (Object __etlItem152 : var2) {
                  File var10 = (File) __etlItem152;
                  if (this.isCancelled()) {
                     return;
                  }

                  this.traverseFileTree(var10);
               }
            }
         }
      }

      public synchronized void cancel() {
         this.canceled = true;
      }

      public synchronized boolean isCancelled() {
         return this.canceled;
      }

      private void checkFile(File var1) throws IOException {
         if (EncryptionChecker.isPossibleEncryptedJitterPack(var1) && this.params.getPassword() == null) {
            JitterPack var2 = new JitterPack(var1, JitterPackDescriptor.UNKNOWN_ENCRYPTED);
            DefaultJitterPackLocator.this.callback.locatedJitterPack(var2);
         }

         VersionInfo var3 = JitterPackUtils.isPossibleJitterPack(var1, this.params);
         if (var3 != null) {
            this.examinePossibleJitterPack(var1, this.params, var3);
         }
      }

      private void examinePossibleJitterPack(File var1, ImportParameters var2, VersionInfo var3) {
         DefaultJitterPackLocator.this.notifierThread.submit(DefaultJitterPackLocator.this.new FileExaminer(var1, var2, var3.createdIn()));
      }
   }
}
