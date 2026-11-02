package org.etlforge.integration.filetransfer.client.download;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.zip.DataFormatException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.filestore.FileCollisionResolver;
import org.etlforge.application.filestore.FileStoreException;
import org.etlforge.application.filestore.FileStoreFile;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobManager;
import org.etlforge.collections.Pair;
import org.etlforge.integration.client.server.ETLForgeServerEncoding;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.serverapi.FileDownloadDependency;

public final class FileDownloadManager {
   private final FileDownloadClient client;
   private final Queue<FileDownloadManager.FileDownloadRequest> jobQueue;
   private final Set<String> hasBeenDownloaded;
   private volatile boolean success;
   private volatile boolean aborted;
   private UiJob currentJob;
   private DownloadedFiles files;

   public FileDownloadManager(FileDownloadClient var1) {
      Preconditions.checkNotNull(var1, "client");
      this.client = var1;
      this.jobQueue = new LinkedList<>();
      this.hasBeenDownloaded = Sets.newHashSet();
   }

   public void download(ServerFile var1) {
      this.jobQueue.offer(new FileDownloadManager.FileDownloadRequest(var1.getFilePath(), null));
      this.beginDownload();
   }

   public void downloadIfNecessary(ServerFile var1) {
      if (this.isFileUpToDate(var1)) {
         this.success = true;
      } else {
         this.download(var1);
      }
   }

   public boolean isSuccess() {
      return this.success;
   }

   public DownloadedFiles getDownloadedFiles() {
      return this.files;
   }

   void handleDownloadError(String var1, Throwable var2) {
      try {
         this.client.handleError(Strings.format("FileDownload.Job.ErrorMessage", var1), var2);
      } finally {
         this.abort();
      }
   }

   void userCancelledDownload() {
      try {
         this.client.downloadCancelled();
      } finally {
         this.abort();
      }
   }

   synchronized void fileWasDownloaded(ServerInfo var1, String var2, String var3, List<? extends FileDownloadDependency> var4, String var5) {
      try {
         this.hasBeenDownloaded.add(var2);
         String var6 = this.inflateContents(var2, var3);
         if (var6 != null) {
            if (this.isLocal(var2)) {
               this.addFileToFileStore(var6, var2, var5);
            }

            this.addToListOfDownloadedFiles(var1, var2, var6);
         }

         this.processDependencies(var2, var4);
      } catch (FileStoreException var7) {
         this.client.handleError("Failed to store the downloaded file \"" + var2 + "\" locally on the client. Please contact ETLForge support.", var7);
         this.abort();
      }
   }

   private boolean isLocal(String var1) {
      return !var1.contains("://");
   }

   private void addToListOfDownloadedFiles(ServerInfo var1, String var2, String var3) {
      if (this.files == null) {
         this.files = new DownloadedFiles(var1);
      }

      ServerFile var4 = ServerFile.builder(var2).withContents(var3).build();
      this.files.addFile(var4);
   }

   private void beginDownload() {
      this.client.downloadStarted();
      this.processJobQueue();
      this.success = !this.aborted;
      if (this.success) {
         this.client.downloadComplete(this.files);
      }
   }

   private void processJobQueue() {
      while (!this.jobQueue.isEmpty() && !this.aborted) {
         FileDownloadManager.FileDownloadRequest var1 = this.jobQueue.poll();
         if (!this.hasBeenDownloaded.contains(var1.getFileToDownload())) {
            FileDownloadJob var2 = this.createDownloadJob(var1);
            this.runJobAndWait(var2);
         }
      }
   }

   private FileDownloadJob createDownloadJob(FileDownloadManager.FileDownloadRequest var1) {
      return new FileDownloadJob(this, var1.getFileToDownload(), var1.getParentFile());
   }

   private void runJobAndWait(FileDownloadJob var1) {
      this.currentJob = var1;
      var1.submit();

      try {
         var1.waitUntilDone();
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
         var3.printStackTrace();
      }
   }

   private void cancelCurrentJob() {
      if (this.currentJob != null) {
         UiJobManager var1 = ApplicationUi.getJobManager();
         var1.cancelJob(this.currentJob);
         this.currentJob = null;
      }
   }

   private String inflateContents(String var1, String var2) {
      try {
         return ETLForgeServerEncoding.base64DecodeAndInflateString(var2);
      } catch (DataFormatException var4) {
         this.client.handleError("Failed to uncompress the contents of the downloaded file \"" + var1 + "\". Please contact ETLForge support.", var4);
         this.abort();
         return null;
      }
   }

   private void addFileToFileStore(String var1, String var2, String var3) throws FileStoreException {
      FileCollisionResolver var4 = FileCollisionResolver.REPLACE_EXISTING;
      this.client.getFileStore().addFile(var2, var1, var4);
      if (var3 != null) {
         this.client.getFileStore().addDependency(var3, var2);
      }
   }

   private void processDependencies(String var1, List<? extends FileDownloadDependency> var2) {
      for (FileDownloadDependency var4 : var2) {
         this.processDependency(var1, var4);
      }
   }

   private void processDependency(String var1, FileDownloadDependency var2) {
      if (this.isDependencyUpToDate(var2)) {
         this.client.getFileStore().addDependency(var1, var2.getPath());
      } else {
         String var3 = var2.getPath();
         if (!this.hasBeenDownloaded.contains(var3)) {
            this.jobQueue.offer(new FileDownloadManager.FileDownloadRequest(var3, var1));
         }
      }
   }

   private boolean isFileUpToDate(ServerFile var1) {
      return this.isFileUpToDate(var1.getFilePath(), var1.getMD5());
   }

   private boolean isDependencyUpToDate(FileDownloadDependency var1) {
      return this.isFileUpToDate(var1.getPath(), var1.getMD5());
   }

   private boolean isFileUpToDate(String var1, String var2) {
      try {
         FileStoreFile var3 = this.client.getFileStore().getFile(var1);
         if (var3 != null) {
            return var2.equals(var3.getMD5());
         }
      } catch (Exception var4) {
         ErrorLog.log(FileDownloadManager.class, "An error occurred while checking the client file store for the selected server file", var4);
      }

      return false;
   }

   private void abort() {
      if (!this.aborted) {
         this.aborted = true;
         this.success = false;
         this.cancelCurrentJob();
      }
   }

   private static final class FileDownloadRequest {
      private final Pair<String, String> data;

      public FileDownloadRequest(String var1, String var2) {
         this.data = Pair.of(var1, var2);
      }

      public String getFileToDownload() {
         return this.data.first;
      }

      public String getParentFile() {
         return this.data.second;
      }
   }
}
