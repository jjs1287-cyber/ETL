package org.etlforge.integration.client.server.download;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.etlforge.system.ArgCheck;

public final class ServerFileDownloader {
   private final String server;
   private boolean https;

   public ServerFileDownloader(String var1) {
      this.server = ArgCheck.checkNotBlank(var1);
   }

   public ServerFileDownloader withHttps(boolean var1) {
      this.https = var1;
      return this;
   }

   public void downloadToLocalFile(Request var1, File var2) throws ServerFileDownloadException {
      DefaultHttpClient var3 = this.createHttpClient(var1);

      try {
         HttpResponse var4 = this.sendRequest(var3, var1);
         int var5 = var4.getStatusLine().getStatusCode();
         if (var5 != 200) {
            throw new ServerFileDownloadException(this.createClientSideErrorMessage(var1, var4));
         }

         this.streamFileToDisk(var4.getEntity(), var2);
      } finally {
         var3.getConnectionManager().shutdown();
      }
   }

   private DefaultHttpClient createHttpClient(Request var1) {
      DefaultHttpClient var2 = new DefaultHttpClient();
      Credentials var3 = var1.getCredentials();
      var2.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(var3.user, var3.password));
      return var2;
   }

   private HttpResponse sendRequest(DefaultHttpClient var1, Request var2) throws ServerFileDownloadException {
      try {
         HttpGet var3 = this.createHttpGet(var2);
         return var1.execute(var3);
      } catch (ClientProtocolException var5) {
         throw new ServerFileDownloadException(var5);
      } catch (IOException var6) {
         throw new ServerFileDownloadException(var6);
      }
   }

   private HttpGet createHttpGet(Request var1) {
      String var2 = var1.toUrl(this.server, this.https);
      Charset var3 = var1.getEncoding();
      HttpGet var4 = new HttpGet(var2);
      var4.addHeader("Content-Type", "text/plain; charset=" + var3.name());
      var4.addHeader("Accept-Encoding", "gzip");
      return var4;
   }

   private void streamFileToDisk(final HttpEntity var1, File var2) throws ServerFileDownloadException {
      try {
         InputSupplier var3 = new InputSupplier<InputStream>() {
            public InputStream getInput() throws IOException {
               return var1.getContent();
            }
         };
         ByteStreams.copy(var3, Files.newOutputStreamSupplier(var2));
      } catch (IOException var4) {
         throw new ServerFileDownloadException(var4);
      }
   }

   private String createClientSideErrorMessage(Request var1, HttpResponse var2) {
      int var3 = var2.getStatusLine().getStatusCode();
      switch (var3) {
         case 400:
            return String.format("The server does not recognize the requested resource: %s/%s", var1.getCategory(), var1.getFileIdentifier());
         case 401:
            return "Invalid user name or password.";
         case 402:
         default:
            return var2.getStatusLine().getReasonPhrase();
         case 403:
            return "You do not have access privileges to read the requested file.";
         case 404:
            return "The requested file no longer exists on the server. It might have been deleted by the ETLForge Cleanup Service.";
      }
   }
}
