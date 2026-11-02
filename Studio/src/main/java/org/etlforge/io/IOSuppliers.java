package org.etlforge.io;

import com.google.common.base.Preconditions;
import com.google.common.io.InputSupplier;
import com.google.common.io.OutputSupplier;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;
import org.etlforge.system.ArgCheck;

public final class IOSuppliers {
   public static InputSupplier<InputStream> newInputStreamSupplier(String var0) {
      return new IOSuppliers.UrlInputStreamSupplier(var0, -1);
   }

   public static InputSupplier<InputStream> newInputStreamSupplier(String var0, int var1) {
      return new IOSuppliers.UrlInputStreamSupplier(var0, ArgCheck.checkPositive(var1));
   }

   public static InputSupplier<BufferedInputStream> newBufferedInputStreamSupplier(InputSupplier<? extends InputStream> var0) {
      return new IOSuppliers.BufferedInputStreamSupplier(var0);
   }

   public static OutputSupplier<BufferedOutputStream> newBufferedOutputStreamSupplier(OutputSupplier<? extends OutputStream> var0) {
      return new IOSuppliers.BufferedOutputStreamSupplier(var0);
   }

   private IOSuppliers() {
   }

   private static class BufferedInputStreamSupplier implements InputSupplier<BufferedInputStream> {
      private final InputSupplier<? extends InputStream> unbuffered;

      public BufferedInputStreamSupplier(InputSupplier<? extends InputStream> var1) {
         this.unbuffered = Preconditions.checkNotNull(var1);
      }

      public BufferedInputStream getInput() throws IOException {
         return new BufferedInputStream(this.unbuffered.getInput());
      }
   }

   private static class BufferedOutputStreamSupplier implements OutputSupplier<BufferedOutputStream> {
      private final OutputSupplier<? extends OutputStream> unbuffered;

      public BufferedOutputStreamSupplier(OutputSupplier<? extends OutputStream> var1) {
         this.unbuffered = Preconditions.checkNotNull(var1);
      }

      public BufferedOutputStream getOutput() throws IOException {
         return new BufferedOutputStream(this.unbuffered.getOutput());
      }
   }

   private static class UrlInputStreamSupplier implements InputSupplier<InputStream> {
      private final String url;
      private final int timeoutInMillis;

      public UrlInputStreamSupplier(String var1, int var2) {
         this.url = ArgCheck.checkNotBlank(var1);
         this.timeoutInMillis = var2;
      }

      public InputStream getInput() throws IOException {
         if (this.timeoutInMillis <= 0) {
            return new URL(this.url).openStream();
         }

         URL var1 = new URL(this.url);
         URLConnection var2 = var1.openConnection();
         var2.setConnectTimeout((int)TimeUnit.MINUTES.toMillis(5L));
         return var2.getInputStream();
      }

      @Override
      public String toString() {
         return "(" + this.url + ").openStream";
      }
   }
}
