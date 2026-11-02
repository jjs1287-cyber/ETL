package org.etlforge.integration.server.engine.connector.util;

import java.io.File;
import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.etlforge.integration.server.engine.connector.exception.ConnectorNoResponseRuntimeException;
import org.etlforge.util.net.ProxySettings;
import org.xml.sax.SAXException;

public class HttpPostCall {
   static final String ENCODING = "UTF-8";
   static final String ContentType = "application/xml; charset=UTF-8";
   final HttpPost post;
   private final ProxySettings proxySettings;

   public HttpPostCall(ProxySettings var1, String var2) {
      this.post = new HttpPost(var2);
      this.post.setHeader("Content-Type", "application/xml; charset=UTF-8");
      this.proxySettings = var1;
   }

   public String call(String var1) throws ClientProtocolException, IOException {
      StringEntity var2 = new StringEntity(var1, "UTF-8");
      this.post.setEntity(var2);
      HttpCallResponse var3 = HttpCall.execute(this.proxySettings, this.post);
      this.checkCallResponse(var3);
      return var3.getResponse();
   }

   public void callFile(String var1, String var2) throws ClientProtocolException, IOException {
      FileEntity var3 = new FileEntity(new File(var1), "application/xml; charset=UTF-8");
      this.post.setEntity(var3);
      HttpCallResponse var4 = HttpCall.executeFile(this.proxySettings, this.post, var2);
      this.checkCallResponse(var4);
   }

   private void checkCallResponse(HttpCallResponse var1) {
      var1.throwRuntimeExceptionOnBadResponse("for request to " + this.post.getURI());
      if (var1.hasNoResponseContent()) {
         String var2 = "No valid response! possibly caused by invalid request data \n" + this.post.getURI();
         throw new ConnectorNoResponseRuntimeException(var2);
      }
   }

   public static void main(String[] var0) throws SAXException, IOException {
      HttpPostCall var1 = new HttpPostCall(ProxySettings.NONE, "https://localhost:46909");
      String var2 = var1.call("");
      System.out.println(var2);
   }
}
