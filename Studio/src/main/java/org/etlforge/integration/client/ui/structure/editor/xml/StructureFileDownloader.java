package org.etlforge.integration.client.ui.structure.editor.xml;

import com.google.common.base.Optional;
import java.util.Collection;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.mapping.serverapi.xml.GetXmlStructureFilesCallback;
import org.etlforge.integration.client.mapping.serverapi.xml.XmlInfoProvider;
import org.etlforge.integration.client.server.AbstractServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;

public final class StructureFileDownloader {
   private final StructureFileSelectionModel model;
   private final XmlInfoProvider serverApi;
   private StructureFileDownloader.Job currentJob;
   private volatile Optional<String> currentFilePath = Optional.absent();

   public StructureFileDownloader(StructureFileSelectionModel var1, XmlInfoProvider var2) {
      this.model = var1;
      this.serverApi = var2;
   }

   public void setCurrentFile(String var1) {
      this.currentFilePath = Optional.of(var1);
   }

   public void startDownload() {
      this.model.setBusy(true);
      this.model.clearAvailableFiles();
      synchronized (this) {
         this.cancel();
         this.currentJob = new StructureFileDownloader.Job();
         this.currentJob.submit();
      }
   }

   public void cancel() {
      synchronized (this) {
         if (this.currentJob != null) {
            this.currentJob.cancel();
            this.currentJob = null;
         }
      }
   }

   private class Job extends UiJob {
      private final StructureFileDownloader.Job.Callback callback = new StructureFileDownloader.Job.Callback();

      public Job() {
         super("Downloading a list of available JSON structures");
         this.setBlockUntilDone(true);
         this.setUncaughtExceptionHandler(this.callback);
      }

      @Override
      protected void runImpl() throws InterruptedException {
         StructureFileDownloader.this.serverApi.getXmlStructureFiles(this.callback);
      }

      private class Callback extends AbstractServerCallback implements GetXmlStructureFilesCallback {
         private Callback() {
         }

         @Override
         public void caught(Throwable var1) {
            try {
               super.caught(var1);
            } finally {
               StructureFileDownloader.this.model.setBusy(false);
            }
         }

         @Override
         public void cancelled() {
            StructureFileDownloader.this.model.setBusy(false);
         }

         @Override
         public void structureFilesRetrieved(ServerInfo var1, Collection<ServerFileFolder> var2) {
            if (!Job.this.isCancelled()) {
               try {
                  StructureFileDownloader.this.model.setAvailableFolders(var2);
                  if (StructureFileDownloader.this.currentFilePath.isPresent()) {
                     StructureFileDownloader.this.model.setSelectedServerFileFromPath(StructureFileDownloader.this.currentFilePath.get());
                  }
               } finally {
                  StructureFileDownloader.this.model.setBusy(false);
               }
            }
         }
      }
   }
}
