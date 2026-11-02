package org.etlforge.integration.client.ui.structure.editor.xml;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.integration.filetransfer.client.download.DownloadedFiles;
import org.etlforge.integration.filetransfer.client.download.FileDownloadClient;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;

final class XsdDownloadClient implements FileDownloadClient {
   private final StructureFileSelectionModel model;
   private final XsdFileStore xsdFileStore;

   public XsdDownloadClient(StructureFileSelectionModel var1, XsdFileStore var2) {
      this.model = var1;
      this.xsdFileStore = var2;
   }

   @Override
   public void downloadCancelled() {
      this.model.setBusy(false);
   }

   @Override
   public void downloadComplete(DownloadedFiles var1) {
      this.model.setBusy(false);
   }

   @Override
   public void downloadStarted() {
      this.model.setBusy(true);
   }

   @Override
   public FileStore getFileStore() {
      return this.xsdFileStore.getBackingFileStore();
   }

   @Override
   public void handleError(String var1, Throwable var2) {
      this.model.setBusy(false);
      ErrorLog.attention(XsdDownloadClient.class, var1, var2);
   }
}
