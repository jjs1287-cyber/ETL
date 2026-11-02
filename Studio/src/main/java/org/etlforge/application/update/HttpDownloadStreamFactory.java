package org.etlforge.application.update;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.io.InputSupplier;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.net.ProxySettings;
import org.etlforge.util.net.ProxyUtils;

final class HttpDownloadStreamFactory implements DownloadStreamFactory {
   private volatile Supplier<ProxySettings> proxySettingsSupplier = Suppliers.ofInstance(ProxySettings.NONE);

   public void setProxySettingsSupplier(Supplier<ProxySettings> var1) {
      this.proxySettingsSupplier = Preconditions.checkNotNull(var1);
   }

   @Override
   public InputSupplier<InputStream> newStreamSupplier(final String var1) {
      ArgCheck.checkNotBlank(var1);
      return new InputSupplier<InputStream>() {
         public InputStream getInput() throws IOException {
            DefaultHttpClient var1x = new DefaultHttpClient();
            int var2 = (int)TimeUnit.MINUTES.toMillis(5L);
            var1x.getParams().setParameter("http.connection.timeout", var2);
            var1x.getParams().setParameter("http.sockey.timeout", var2);
            HttpDownloadStreamFactory.this.configureProxy(var1x);
            HttpResponse var3 = HttpDownloadStreamFactory.this.sendRequest(var1x, var1);
            return new HttpDownloadStreamFactory.InputStreamImpl(var3.getEntity().getContent(), var1x);
         }
      };
   }

   private void configureProxy(DefaultHttpClient var1) {
      ProxySettings var2 = this.proxySettingsSupplier.get();
      ProxyUtils.configureApacheHttpClient(var1, var2);
   }

   private HttpResponse sendRequest(DefaultHttpClient var1, String var2) throws IOException, ClientProtocolException {
      HttpGet var3 = new HttpGet(var2);
      var3.addHeader("Cache-Control", "no-cache");
      var3.addHeader("Pragma", "no-cache");
      return var1.execute(var3);
   }

   private static class InputStreamImpl extends InputStream {
      private final InputStream wrappee;
      private final HttpClient client;

      public InputStreamImpl(InputStream var1, HttpClient var2) {
         this.wrappee = var1;
         this.client = var2;
      }

      @Override
      public int read() throws IOException {
         return this.wrappee.read();
      }

      @Override
      public int read(byte[] var1) throws IOException {
         return this.wrappee.read(var1);
      }

      @Override
      public int read(byte[] var1, int var2, int var3) throws IOException {
         return this.wrappee.read(var1, var2, var3);
      }

      @Override
      public long skip(long var1) throws IOException {
         return this.wrappee.skip(var1);
      }

      @Override
      public int available() throws IOException {
         return this.wrappee.available();
      }

      @Override
      public void close() throws IOException {
         this.client.getConnectionManager().shutdown();
      }

      @Override
      public synchronized void mark(int var1) {
         this.wrappee.mark(var1);
      }

      @Override
      public synchronized void reset() throws IOException {
         this.wrappee.reset();
      }

      @Override
      public boolean markSupported() {
         return this.wrappee.markSupported();
      }
   }
}
