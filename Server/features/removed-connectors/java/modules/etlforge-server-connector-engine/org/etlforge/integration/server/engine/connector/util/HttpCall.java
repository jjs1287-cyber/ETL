package org.etlforge.integration.server.engine.connector.util;

import com.google.common.io.ByteStreams;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.io.FileUtils;
import org.etlforge.util.file.InvalidCharactersReplacer;
import org.etlforge.util.net.ProxySettings;
import org.etlforge.util.net.ProxyUtils;

public class HttpCall {
   private static final Logger logger = Logger.getLogger("ConnectorHttpCall");
   private final HttpRequestBase request;
   private final DefaultHttpClient client = new DefaultHttpClient();
   private HttpEntity responseEntity;
   private HttpCallResponse callResponse;
   private final boolean enableCookie;

   private HttpCall(ProxySettings var1, HttpRequestBase var2) {
      this(var1, var2, false);
   }

   private HttpCall(ProxySettings var1, HttpRequestBase var2, boolean var3) {
      ProxyUtils.configureApacheHttpClient(this.client, var1);
      this.request = var2;
      this.enableCookie = var3;
      this.initExecution();
   }

   private void initExecution() {
      this.responseEntity = null;
      this.callResponse = new HttpCallResponse();
   }

   public static HttpCallResponse execute(ProxySettings var0, HttpRequestBase var1, boolean var2) throws ClientProtocolException, IOException {
      HttpCall var3 = new HttpCall(var0, var1, var2);

      try {
         return var3.execute();
      } finally {
         var3.dispose();
      }
   }

   public static HttpCallResponse execute(ProxySettings var0, HttpRequestBase var1) throws ClientProtocolException, IOException {
      return execute(var0, var1, false);
   }

   public static HttpCallResponse executeFile(ProxySettings var0, HttpRequestBase var1, String var2) throws ClientProtocolException, IOException {
      HttpCall var3 = new HttpCall(var0, var1);

      try {
         return var3.executeFile(var2);
      } finally {
         var3.dispose();
      }
   }

   private HttpCallResponse execute() throws ClientProtocolException, IOException {
      this.executeCall();
      this.handleResponse();
      return this.callResponse;
   }

   private HttpCallResponse executeFile(String var1) throws ClientProtocolException, IOException {
      this.executeCall();
      this.handleResponseFile(var1);
      return this.callResponse;
   }

   private static String getHeader(HttpResponse var0, String var1) {
      if (var0 == null) {
         return null;
      }

      Header[] var2 = var0.getHeaders(var1);
      if (var2 != null && var2.length != 0) {
         StringBuilder var3 = new StringBuilder();

         for (int var4 = 0; var4 < var2.length; var4++) {
            if (var4 > 0) {
               var3.append(';');
            }

            var3.append(var2[var4].getValue());
         }

         return var3.toString();
      } else {
         return null;
      }
   }

   private void executeCall() throws ClientProtocolException, IOException {
      logger.info("start connector ws call. ");
      long var1 = System.currentTimeMillis();
      this.initExecution();
      CloseableHttpResponse var3 = this.client.execute(this.request);
      logger.info("start process response. ");
      if (this.enableCookie) {
         this.callResponse.setCookie(getHeader(var3, "Set-Cookie"));
      }

      StatusLine var4 = var3.getStatusLine();
      this.callResponse.setStatusLine(var4.getStatusCode(), var4.toString());
      if (this.callResponse.isRedirectUrl()) {
         this.callResponse.redirectUrl = getHeader(var3, "Location");
      }

      this.responseEntity = var3.getEntity();
      if (this.responseEntity != null) {
         this.callResponse.setResponseCharset(EntityUtils.getContentCharSet(this.responseEntity));
      }

      this.callResponse.callingTimeMillis = System.currentTimeMillis() - var1;
      logger.info("end connector ws call. duration(milliseconds)=" + this.callResponse.callingTimeMillis);
   }

   private void handleResponse() throws IllegalStateException, IOException {
      this.callResponse.isReponseInFile = false;
      if (this.responseEntity != null) {
         byte[] var1 = ByteStreams.toByteArray(this.responseEntity.getContent());
         this.callResponse.setHasNoResponseContent(var1.length < 1);
         Charset var2 = this.callResponse.isUtf8() ? ConnectorExecutionSystem.ConnectorDefaultCharset_UTF8 : this.callResponse.responseCharsetNonUtf8;
         this.callResponse.response = new String(var1, var2);
      }
   }

   private void handleResponseFile(String var1) throws IOException {
      this.callResponse.isReponseInFile = true;
      if (this.responseEntity != null) {
         String var2 = var1;
         if (!this.callResponse.isUtf8()) {
            String var3 = this.callResponse.responseCharsetEncoding.length() < 8
               ? this.callResponse.responseCharsetEncoding
               : this.callResponse.responseCharsetEncoding.substring(0, 8);
            InvalidCharactersReplacer var4 = new InvalidCharactersReplacer("-");
            var2 = FileUtils.getPostfixFilename(var1, var4.replace(var3), ".xml");
         }

         File var8 = new File(var2);
         FileOutputStream var9 = new FileOutputStream(var8);

         try {
            this.responseEntity.writeTo(var9);
         } finally {
            var9.close();
         }

         this.callResponse.setHasNoResponseContent(!var8.exists() || var8.length() < 1L);
         if (!this.callResponse.isUtf8()) {
            ConnectorExecutionSystem.copyFileUtf8Xml(var2, this.callResponse.responseCharsetNonUtf8, var1);
         }
      }
   }

   private void dispose() {
      this.client.getConnectionManager().shutdown();
   }
}
