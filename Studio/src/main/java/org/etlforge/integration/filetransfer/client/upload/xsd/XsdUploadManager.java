package org.etlforge.integration.filetransfer.client.upload.xsd;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.io.File;
import java.util.Collection;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;
import org.etlforge.integration.filetransfer.client.upload.FileUploadClient;
import org.etlforge.integration.filetransfer.client.upload.FileUploadJob;
import org.etlforge.integration.filetransfer.client.upload.FileUploader;
import org.etlforge.integration.filetransfer.client.upload.RequiredFilesProvider;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;

public final class XsdUploadManager {
   private final FileUploadApi uploadApi;
   private final XsdFileStore fileStore;
   private XsdUploadClient client;
   private FileUploader uploader;
   private RequiredFilesProvider reqFilesProvider;

   public XsdUploadManager(FileUploadApi var1, XsdFileStore var2) {
      this.uploadApi = Preconditions.checkNotNull(var1, "uploadApi");
      this.fileStore = Preconditions.checkNotNull(var2, "fileStore");
   }

   public void setRequiredFilesProvider(RequiredFilesProvider var1) {
      this.reqFilesProvider = Preconditions.checkNotNull(var1);
   }

   public synchronized void upload(ServerFileFolder var1, String var2) {
      try {
         if (this.uploader != null) {
            this.uploader.abort();
         }

         this.client = new XsdUploadClient(var1, this.fileStore);
         this.uploader = new XsdUploadManager.XsdUploader(this.uploadApi, this.client, var2, var1);
         if (this.reqFilesProvider != null) {
            this.uploader.setRequiredFilesProvider(this.reqFilesProvider);
         }

         this.uploader.start();
      } catch (InterruptedException var4) {
         Thread.currentThread().interrupt();
         this.client.handleError(var4);
      }
   }

   public synchronized ServerFile getMainFile() {
      if (!this.isSuccess()) {
         throw new IllegalStateException("getMainFile() can only be called once the upload process completes successfully.");
      } else {
         return this.client.getMainFile();
      }
   }

   public synchronized boolean isSuccess() {
      return this.uploader.isSuccess();
   }

   public synchronized void abort() {
      this.uploader.abort();
   }

   public void setExistingFiles(Collection<ServerFileFolder> var1) {
      this.client.setExistingFiles(var1);
   }

   private static final class XsdUploader extends FileUploader {
      private final ServerFileFolder folder;

      public XsdUploader(FileUploadApi var1, FileUploadClient var2, String var3, ServerFileFolder var4) {
         super(var1, var2, var3);
         this.folder = var4;
      }

      @Override
      protected FileUploadJob createUploadJob(FileUploadApi var1, ServerFile var2, ImmutableList<File> var3) {
         FileUploadJob var4 = new FileUploadJob("XSD/DTD file upload", this, var2, var3);
         var4.setServerFolder(this.folder);
         return var4;
      }
   }
}
