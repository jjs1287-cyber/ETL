package org.etlforge.application.update;

import com.google.common.io.InputSupplier;
import com.google.common.io.OutputSupplier;
import java.io.InputStream;
import java.io.OutputStream;
import org.etlforge.io.ByteTransfer;
import org.etlforge.io.EstimatingByteTransferCallback;
import org.etlforge.io.ProgressUpdateByteTransferCallback;
import org.etlforge.system.thread.Cancelable;
import org.etlforge.util.StorageSizeUnit;

final class DownloadJob implements Runnable, Cancelable {
   private final ByteTransfer byteTransfer;
   private final DownloadNewVersionCallback callback;
   private volatile boolean canceled;

   public DownloadJob(InputSupplier<? extends InputStream> var1, OutputSupplier<? extends OutputStream> var2, long var3, DownloadNewVersionCallback var5) {
      this.byteTransfer = this.createByteTransfer(var1, var2, var3);
      this.callback = var5;
   }

   private ByteTransfer createByteTransfer(InputSupplier<? extends InputStream> var1, OutputSupplier<? extends OutputStream> var2, long var3) {
      ProgressUpdateByteTransferCallback var5 = var3 > 0L ? new DownloadJob.KnownFileSizeCallback(var3) : new DownloadJob.UnknownFileSizeCallback();
      return ByteTransfer.builder(var1, var2).withChunkSize(8192).withCallback(var5).build();
   }

   private double bytesToMB(long var1) {
      return var1 / (1.0 * StorageSizeUnit.Megabytes.toBytes(1L));
   }

   @Override
   public void run() {
      if (!this.canceled) {
         this.callback.progressUpdate(0, "Connecting to download site");

         try {
            this.byteTransfer.call();
            if (!this.canceled) {
               this.callback.downloadComplete();
            }
         } catch (Exception var2) {
            if (!this.canceled) {
               this.callback.downloadFailed(var2);
            }
         }
      }
   }

   @Override
   public boolean cancel() {
      if (this.canceled) {
         return false;
      }

      this.canceled = true;
      this.byteTransfer.cancel();
      this.callback.downloadCanceled();
      return true;
   }

   private class KnownFileSizeCallback extends EstimatingByteTransferCallback {
      private final double fileSizeInMb;

      public KnownFileSizeCallback(long var2) {
         super(var2);
         this.fileSizeInMb = DownloadJob.this.bytesToMB(var2);
      }

      @Override
      protected void handleProgressUpdate(long var1, long var3, long var5) {
         int var7 = this.calculatePercentage(var3);
         String var8 = this.formatMessage(var3, var5);
         DownloadJob.this.callback.progressUpdate(var7, var8);
      }

      private int calculatePercentage(long var1) {
         int var3 = (int)(100.0 * var1 / this.getNumberOfBytesToTransfer());
         return Math.min(100, var3);
      }

      private String formatMessage(long var1, long var3) {
         double var5 = DownloadJob.this.bytesToMB(var1);
         String var7 = String.format("%.1f / %.1f MB", var5, this.fileSizeInMb);
         String var8 = this.formatTimeLeftMessage(var1, var3);
         if (!var8.isEmpty()) {
            var7 = var7 + ", " + var8;
         }

         return var7;
      }
   }

   private class UnknownFileSizeCallback extends ProgressUpdateByteTransferCallback {
      private UnknownFileSizeCallback() {
      }

      @Override
      protected long estimateSecondsLeft(long var1) {
         return -1L;
      }

      @Override
      protected void handleProgressUpdate(long var1, long var3, long var5) {
         double var7 = DownloadJob.this.bytesToMB(var3);
         String var9 = String.format("%.1f MB", var7);
         DownloadJob.this.callback.progressUpdate(-1, var9);
      }
   }
}
