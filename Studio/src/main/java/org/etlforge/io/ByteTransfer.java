package org.etlforge.io;

import com.google.common.base.Preconditions;
import com.google.common.io.CountingOutputStream;
import com.google.common.io.InputSupplier;
import com.google.common.io.OutputSupplier;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import org.etlforge.system.ArgCheck;
import org.etlforge.system.thread.Cancelable;

public final class ByteTransfer implements Callable<Long>, Cancelable {
   private static final int DEFAULT_CHUNK_SIZE = 4096;
   private final InputSupplier<? extends InputStream> inputSupplier;
   private final OutputSupplier<? extends OutputStream> outputSupplier;
   private volatile boolean canceled;
   private InputStream inStream;
   private CountingOutputStream outStream;
   private final int chunkSize;
   private final boolean bufferInput;
   private final boolean bufferOutput;
   private final ByteTransferCallback callback;

   public ByteTransfer(InputSupplier<? extends InputStream> var1, OutputSupplier<? extends OutputStream> var2) {
      this(var1, var2, ByteTransferCallback.NONE);
   }

   public ByteTransfer(InputSupplier<? extends InputStream> var1, OutputSupplier<? extends OutputStream> var2, ByteTransferCallback var3) {
      this(var1, var2, 4096, false, false, var3);
   }

   private ByteTransfer(
      InputSupplier<? extends InputStream> var1, OutputSupplier<? extends OutputStream> var2, int var3, boolean var4, boolean var5, ByteTransferCallback var6
   ) {
      this.inputSupplier = Preconditions.checkNotNull(var1);
      this.outputSupplier = Preconditions.checkNotNull(var2);
      this.chunkSize = var3;
      this.bufferInput = var4;
      this.bufferOutput = var5;
      this.callback = Preconditions.checkNotNull(var6);
   }

   public static ByteTransfer.Builder builder(InputSupplier<? extends InputStream> var0, OutputSupplier<? extends OutputStream> var1) {
      return new ByteTransfer.Builder(var0, var1);
   }

   public Long call() throws Exception {
      if (this.canceled) {
         return 0L;
      }

      this.callback.started();

      try {
         this.connect();
         if (this.canceled) {
            return 0L;
         }

         byte[] var1 = new byte[this.chunkSize];

         int var2;
         while (!this.canceled && (var2 = this.inStream.read(var1, 0, this.chunkSize)) != -1) {
            this.outStream.write(var1, 0, var2);
            this.callback.update(this.outStream.getCount());
         }

         return this.outStream.getCount();
      } finally {
         KongaIoUtils.close(this.inStream);
         KongaIoUtils.close(this.outStream);
      }
   }

   private void connect() throws IOException {
      InputStream var1 = this.inputSupplier.getInput();
      synchronized (this) {
         this.inStream = this.bufferInput ? new BufferedInputStream(var1) : var1;
      }

      OutputStream var2 = this.outputSupplier.getOutput();
      synchronized (this) {
         this.outStream = new CountingOutputStream(this.bufferOutput ? new BufferedOutputStream(var2) : var2);
      }
   }

   @Override
   public boolean cancel() {
      if (this.canceled) {
         return false;
      }

      this.canceled = true;
      synchronized (this) {
         if (this.inStream != null) {
            KongaIoUtils.close(this.inStream);
         }

         if (this.outStream != null) {
            KongaIoUtils.close(this.outStream);
         }

         return true;
      }
   }

   public static final class Builder {
      private InputSupplier<? extends InputStream> inputSupplier;
      private OutputSupplier<? extends OutputStream> outputSupplier;
      private ByteTransferCallback callback = ByteTransferCallback.NONE;
      private int chunkSize = 4096;
      private boolean bufferInput;
      private boolean bufferOutput;

      public Builder(InputSupplier<? extends InputStream> var1, OutputSupplier<? extends OutputStream> var2) {
         this.inputSupplier = Preconditions.checkNotNull(var1);
         this.outputSupplier = Preconditions.checkNotNull(var2);
      }

      public ByteTransfer.Builder withCallback(ByteTransferCallback var1) {
         this.callback = Preconditions.checkNotNull(var1);
         return this;
      }

      public ByteTransfer.Builder withBufferedInput() {
         this.inputSupplier = IOSuppliers.newBufferedInputStreamSupplier(this.inputSupplier);
         return this;
      }

      public ByteTransfer.Builder withBufferedOutput() {
         this.outputSupplier = IOSuppliers.newBufferedOutputStreamSupplier(this.outputSupplier);
         return this;
      }

      public ByteTransfer.Builder withChunkSize(int var1) {
         this.chunkSize = ArgCheck.checkPositive(var1);
         return this;
      }

      public ByteTransfer build() {
         return new ByteTransfer(this.inputSupplier, this.outputSupplier, this.chunkSize, this.bufferInput, this.bufferOutput, this.callback);
      }
   }
}
