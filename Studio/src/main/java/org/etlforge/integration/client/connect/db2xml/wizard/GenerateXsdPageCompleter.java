package org.etlforge.integration.client.connect.db2xml.wizard;

import com.google.common.base.Charsets;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.etlforge.application.appdir.ApplicationTempFolder;
import org.etlforge.application.filestore.FileCollisionResolver;
import org.etlforge.application.filestore.FileStoreException;
import org.etlforge.integration.client.mapping.serverapi.xml.GetXmlStructureFilesCallback;
import org.etlforge.integration.client.mapping.serverapi.xml.XmlInfoProvider;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;
import org.etlforge.integration.filetransfer.client.ServerFileUploadResult;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;
import org.etlforge.integration.filetransfer.serverapi.UploadServerFileCallback;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.util.crypto.DigestUtils;

final class GenerateXsdPageCompleter {
   private final XsdGeneratorModel model;
   private final XsdFileStore xsdFiles;
   private final WaitService waitService;
   private final ExecutorService executor = Executors.newFixedThreadPool(1);
   private final CountDownLatch latch = new CountDownLatch(1);
   private Future<?> currentJob;
   private volatile boolean success;
   private volatile boolean canceled;

   public GenerateXsdPageCompleter(XsdGeneratorModel var1, XsdFileStore var2, WaitService var3) {
      this.model = var1;
      this.xsdFiles = var2;
      this.waitService = var3;
   }

   public void cancel() {
      this.canceled = true;
      if (this.currentJob != null) {
         this.currentJob.cancel(true);
         this.latch.countDown();
      }
   }

   public void waitUntilDone() throws InterruptedException {
      this.latch.await();
   }

   public boolean isSuccess() {
      return !this.canceled && this.success;
   }

   public void start() {
      WaitLock var1 = this.waitService.startWait();

      try {
         Future var2 = this.executor.submit(new GenerateXsdPageCompleter.XsdDownloader());
         this.currentJob = var2;
         Collection var3 = (Collection)var2.get();
         this.currentJob = null;
         if (var3 != null && !this.canceled) {
            boolean var4 = this.doesFileAlreadyExistOnServer(var3);
            if (!var4) {
               this.checkNameConflict(var3);
            }

            if (!this.canceled) {
               File var5 = this.writeToFileStore();
               if (var4) {
                  this.success = true;
               } else {
                  ServerFileFolder var6 = var3.isEmpty() ? null : (ServerFileFolder)Iterables.get(var3, 0);
                  GenerateXsdPageCompleter.XsdUploader var7 = new GenerateXsdPageCompleter.XsdUploader(var6, var5);
                  Future var8 = this.executor.submit(var7);
                  this.success = (Boolean)var8.get();
                  this.currentJob = null;
               }
            }
         }
      } catch (InterruptedException var15) {
         Thread.currentThread().interrupt();
      } catch (ExecutionException var16) {
         var16.printStackTrace();
      } catch (IOException var17) {
         var17.printStackTrace();
      } catch (FileStoreException var18) {
         var18.printStackTrace();
      } finally {
         var1.release();
         this.latch.countDown();
      }
   }

   private boolean doesFileAlreadyExistOnServer(Collection<ServerFileFolder> var1) {
      String var2 = DigestUtils.getMd5DigestAsHexString(this.model.getContent());
      String var3 = this.model.getFileName();

      for (ServerFileFolder var5 : var1) {
         ServerFile var6 = var5.getFile(var3);
         if (var6 != null) {
            String var7 = var6.getMD5();
            if (var2.equals(var7)) {
               return true;
            }
         }
      }

      return false;
   }

   private void checkNameConflict(Collection<ServerFileFolder> var1) {
      HashSet<String> var2 = Sets.newHashSet();

      for (ServerFileFolder var4 : var1) {
         for (ServerFile var6 : var4.allFiles()) {
            var2.add(var6.getName().toLowerCase());
         }
      }

      this.checkNameConflict(var2);
   }

   private void checkNameConflict(Set<String> var1) {
      String var2 = this.model.getFileName().toLowerCase();
      if (var1.contains(var2)) {
         FileNameConflictHandler var3 = new FileNameConflictHandler(this.model);
         FileNameConflictHandler.Resolution var4 = var3.resolveConflict();
         switch (var4) {
            case NEW_NAME:
               this.checkNameConflict(var1);
               break;
            case CANCEL:
               this.cancel();
               return;
            case OVERWRITE:
               return;
            default:
               throw new RuntimeException("Unexpected conflict resolution: " + var4);
         }
      }
   }

   private File writeToFileStore() throws IOException, FileStoreException {
      File var1 = ApplicationTempFolder.newLikelyUniqueFolder("xsd", true);
      File var2 = new File(var1, this.model.getFileName());
      Files.write(this.model.getContent(), var2, Charsets.UTF_8);
      this.xsdFiles.addFile(var2.getAbsolutePath(), this.model.getFileName(), null, FileCollisionResolver.REPLACE_EXISTING);
      return var2;
   }

   private static class XsdDownloader implements Callable<Collection<ServerFileFolder>>, GetXmlStructureFilesCallback {
      private Collection<ServerFileFolder> folders;

      private XsdDownloader() {
      }

      public Collection<ServerFileFolder> call() throws Exception {
         IntegrationServer var1 = IntegrationServer.getInstance();
         XmlInfoProvider var2 = var1.getServerCall(XmlInfoProvider.class);
         var2.getXmlStructureFiles(this);
         return this.folders;
      }

      @Override
      public void caught(Throwable var1) {
      }

      @Override
      public void cancelled() {
      }

      @Override
      public void structureFilesRetrieved(ServerInfo var1, Collection<ServerFileFolder> var2) {
         this.folders = var2;
      }
   }

   private static class XsdUploader implements Callable<Boolean>, UploadServerFileCallback {
      private final ServerFileFolder folder;
      private final File file;
      private volatile boolean success;

      public XsdUploader(ServerFileFolder var1, File var2) {
         this.folder = var1;
         this.file = var2;
      }

      public Boolean call() throws Exception {
         IntegrationServer var1 = IntegrationServer.getInstance();
         XmlInfoProvider var2 = var1.getServerCall(XmlInfoProvider.class);
         var2.uploadFile(this.file, this.folder, null, this);
         return this.success;
      }

      @Override
      public void caught(Throwable var1) {
      }

      @Override
      public void cancelled() {
      }

      @Override
      public void fileWasUploaded(ServerFileUploadResult var1) {
         this.success = true;
      }
   }
}
