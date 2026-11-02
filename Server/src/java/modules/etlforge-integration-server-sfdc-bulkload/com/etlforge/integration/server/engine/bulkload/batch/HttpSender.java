package com.etlforge.integration.server.engine.bulkload.batch;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.etlforge.integration.server.engine.bulkload.BulkLoadEndpoint;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.Proxy.Type;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.codec.binary.Base64;
import org.etlforge.util.net.ProxySettings;
import org.etlforge.util.net.ProxyUtils;

final class HttpSender implements BatchSender {
   private final URL url;
   private final String sessionId;
   private final String fileHeader;
   private HttpURLConnection urlConnection;
   private OutputStreamWriter writer;
   private final boolean compress;
   private volatile DebugRequestHeaders debugHeaders;
   private volatile ProxySettings proxySettings = ProxySettings.NONE;

   public HttpSender(BulkLoadEndpoint var1, String var2, String var3, boolean var4) {
      this.sessionId = var1.getSessionId();
      this.fileHeader = var3;
      this.compress = var4;

      try {
         this.url = new URL(var1.getSendBatchUrl(var2));
      } catch (MalformedURLException var6) {
         throw new RuntimeException(var6);
      }
   }

   public void setProxySettings(ProxySettings var1) {
      this.proxySettings = Preconditions.checkNotNull(var1, "px");
   }

   public void setDebugHeaders(DebugRequestHeaders var1) {
      this.debugHeaders = var1;
   }

   @Override
   public void forceClose() {
      try {
         if (this.writer != null) {
            this.writer.close();
         }
      } catch (Exception var2) {
      }

      this.writer = null;
      this.urlConnection = null;
   }

   @Override
   public boolean isOpen() {
      return this.urlConnection != null;
   }

   @Override
   public void sendBundle(String var1, int var2, int var3) throws Exception {
      if (!this.isOpen()) {
         this.openConnection(var2, var3);
      }

      this.writer.write(var1);
   }

   private void openConnection(int var1, int var2) throws Exception {
      this.urlConnection = this.createUrlConnection();
      if (this.debugHeaders != null) {
         this.debugHeaders.addToRequest(this.urlConnection, var1, var2);
      }

      this.urlConnection.connect();
      this.writer = this.createWriter();
      this.writeHeader();
   }

   private HttpURLConnection createUrlConnection() throws IOException {
      Proxy var1 = this.getProxy();
      HttpURLConnection var2 = (HttpURLConnection)this.url.openConnection(var1);
      var2.setChunkedStreamingMode(0);
      var2.setDoOutput(true);
      var2.addRequestProperty("X-SFDC-Session", this.sessionId);
      var2.addRequestProperty("Content-Type", "text/csv; charset=UTF-8");
      if (this.compress) {
         var2.addRequestProperty("Content-Encoding", "gzip");
      }

      if (this.goesThroughProxy()) {
         this.applyProxyCredentials(var2);
      }

      return var2;
   }

   private Proxy getProxy() {
      if (this.goesThroughProxy()) {
         InetSocketAddress var1 = new InetSocketAddress(this.proxySettings.getHost(), this.proxySettings.getPort().getPort());
         return new Proxy(Type.HTTP, var1);
      } else {
         return Proxy.NO_PROXY;
      }
   }

   private boolean goesThroughProxy() {
      return !this.proxySettings.equals(ProxySettings.NONE);
   }

   private void applyProxyCredentials(HttpURLConnection var1) {
      if (this.proxySettings.hasUsername()) {
         String var2 = this.proxySettings.getUser() + ":" + this.proxySettings.getPassword();
         String var3 = "Basic " + new String(Base64.encodeBase64(var2.getBytes()));
         var1.addRequestProperty("Proxy-Authorization", var3);
         var1.addRequestProperty("Https-Proxy-Authorization", var3);
      }

      ProxyUtils.updateNtlmDomainSystemProperty(this.proxySettings);
   }

   private OutputStreamWriter createWriter() throws IOException, UnsupportedEncodingException {
      OutputStream var1 = this.urlConnection.getOutputStream();
      if (this.compress) {
         var1 = new GZIPOutputStream(var1);
      }

      return new OutputStreamWriter(var1, Charsets.UTF_8);
   }

   private void writeHeader() throws IOException {
      this.writer.write(this.fileHeader);
      this.writer.write("\n");
   }

   @Override
   public BatchResponse terminateRequest() throws Exception {
      if (this.writer == null) {
         return null;
      } else {
         this.writer.close();
         HttpBatchResponse var1 = new HttpBatchResponse(this.urlConnection);
         this.writer = null;
         this.urlConnection = null;
         if (var1 != null && var1.readError != null) {
            throw var1.readError;
         } else {
            return var1;
         }
      }
   }
}
