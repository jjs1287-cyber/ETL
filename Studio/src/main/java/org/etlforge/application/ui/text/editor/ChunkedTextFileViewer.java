package org.etlforge.application.ui.text.editor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.io.FileChunkReader;
import org.etlforge.system.callback.Receiver;

public final class ChunkedTextFileViewer extends PagedTextArea {
   private FileChunkReader fileReader;
   private final Object lock = new Object();

   public void openFile(File var1, Charset var2, int var3) {
      this.openFile(var1, var2.name(), var3);
   }

   public void openFile(File var1, String var2, int var3) {
      synchronized (this.lock) {
         this.closeReader();
         this.fileReader = new FileChunkReader(var1, var3, var2);
         this.readFirstChunk();
      }
   }

   public void closeFile() {
      synchronized (this.lock) {
         this.closeReader();
      }
   }

   private void readFirstChunk() {
      try {
         this.fileReader.open();
         this.setNumberOfPages(this.fileReader.getNumberOfChunks());
         if (this.fileReader.hasNext()) {
            this.setCurrentPage(1);
         }
      } catch (IOException var2) {
         this.handleReadError(var2);
      }
   }

   private void closeReader() {
      if (this.fileReader != null) {
         this.fileReader.close();
         this.fileReader = null;
         this.reset();
         this.setEnabled(false);
      }
   }

   @Override
   protected void getTextForPage(int var1, Receiver<String> var2) {
      ChunkedTextFileViewer.ReadPageTask var3 = new ChunkedTextFileViewer.ReadPageTask(var1, var2);
      this.runBackgroundTask(var3);
   }

   private void runBackgroundTask(Runnable var1) {
      ApplicationWorker var2 = Application.getWorker();
      var2.submitForParallel(var1);
   }

   private void handleReadError(Exception var1) {
      ErrorLog.attention(ChunkedTextFileViewer.class, "An error occurred while reading from the file.", var1);
   }

   private class ReadPageTask implements Runnable {
      private final int index;
      private final Receiver<String> callback;

      public ReadPageTask(int var2, Receiver<String> var3) {
         this.index = var2;
         this.callback = var3;
      }

      @Override
      public final void run() {
         String var1 = "";

         try {
            synchronized (ChunkedTextFileViewer.this.lock) {
               if (ChunkedTextFileViewer.this.fileReader != null) {
                  var1 = this.read();
               }
            }
         } catch (Exception var5) {
            ChunkedTextFileViewer.this.handleReadError(var5);
         }

         this.callback.handle(var1);
      }

      private String read() throws IOException {
         return ChunkedTextFileViewer.this.fileReader.gotoChunk(this.index);
      }
   }
}
