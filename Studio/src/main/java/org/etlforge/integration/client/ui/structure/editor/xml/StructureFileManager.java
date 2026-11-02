package org.etlforge.integration.client.ui.structure.editor.xml;

import com.google.common.collect.Maps;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.etlforge.integration.client.mapping.serverapi.xml.XmlInfoProvider;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;
import org.etlforge.integration.filetransfer.client.download.FileDownloadManager;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdCache;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdUploadManager;

public final class StructureFileManager {
   private final StructureFileSelectionModel model;
   private final StructureFileDownloader downloader;
   private final XsdFileStore fileStore;
   private XsdUploadManager uploadManager;
   private volatile ServerFile selectedStructureFile;
   private volatile Map<String, ServerFile> uploadedFiles;

   public StructureFileManager(StructureFileSelectionModel var1, StructureFileDownloader var2, XsdFileStore var3) {
      this.model = var1;
      this.downloader = var2;
      this.fileStore = var3;
      this.uploadedFiles = Maps.newHashMap();
   }

   public ServerFile getStructureFile() {
      return this.selectedStructureFile;
   }

   public void initialize() {
      IntegrationServer var1 = IntegrationServer.getInstance();
      ServerInfo var2 = var1.getServerInfo();
      if (var2 != null) {
         List var3 = XsdCache.restore(var2.getGuid());
         if (!var3.isEmpty()) {
            this.model.setAvailableFolders(var3);
            return;
         }
      }

      this.loadFilesFromServer();
   }

   public void loadFilesFromServer() {
      this.downloader.startDownload();
   }

   public synchronized boolean doUpload(File var1) {
      String var2 = var1.getAbsolutePath();
      return !this.uploadedFiles.containsKey(var2) ? this.uploadStructureFile(var2) : true;
   }

   private boolean uploadStructureFile(String var1) {
      this.abortUploadInProgress();
      boolean var2 = this.executeUpload(var1, null);
      if (var2) {
         ServerFile var3 = this.uploadManager.getMainFile();
         this.onFileUploaded(var1, var3);
      }

      return var2;
   }

   private void abortUploadInProgress() {
      if (this.uploadManager != null) {
         this.uploadManager.abort();
      }
   }

   private boolean executeUpload(String var1, ServerFileFolder var2) {
      XmlInfoProvider var3 = IntegrationServer.getInstance().getServerCall(XmlInfoProvider.class);
      this.uploadManager = new XsdUploadManager(var3, this.fileStore);

      try {
         this.model.setBusy(true);
         this.uploadManager.upload(var2, var1);
      } finally {
         this.model.setBusy(false);
      }

      return this.uploadManager.isSuccess();
   }

   private void onFileUploaded(String var1, ServerFile var2) {
      this.uploadedFiles.put(var1, var2);
      this.selectedStructureFile = var2;
   }

   public boolean downloadStructureFile(ServerFile var1) {
      this.selectedStructureFile = var1;
      XsdDownloadClient var2 = new XsdDownloadClient(this.model, this.fileStore);
      FileDownloadManager var3 = new FileDownloadManager(var2);
      var3.downloadIfNecessary(var1);
      return var3.isSuccess();
   }

   public synchronized void cancelCurrentJobs() {
      this.downloader.cancel();
      if (this.uploadManager != null) {
         this.uploadManager.abort();
         this.uploadManager = null;
      }
   }

   public synchronized void setExistingFiles(ServerInfo var1, Collection<ServerFileFolder> var2) {
      XsdCache.store(var1.getGuid(), var2);
      if (this.uploadManager != null) {
         this.uploadManager.setExistingFiles(var2);
      }
   }
}
