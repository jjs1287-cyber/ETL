package org.etlforge.integration.server.engine.connector.netsuite.wsdl;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpRequestBase;
import org.etlforge.integration.server.engine.connector.exception.ConnectorWebServiceRuntimeException;
import org.etlforge.integration.server.engine.connector.util.HttpCall;
import org.etlforge.integration.server.engine.connector.util.HttpCallResponse;
import org.etlforge.integration.server.engine.connector.util.HttpRequestFactory;
import org.etlforge.util.net.ProxySettings;

public class NetsuiteWsdlDownloader {
   public static String download(ProxySettings var0, String var1) {
      try {
         String var2 = downloadImpl(var0, var1);
         throwRuntimeExceptionOnHtml(var1, var2);
         return var2;
      } catch (ClientProtocolException var3) {
         var3.printStackTrace();
         throw new ConnectorWebServiceRuntimeException(var3);
      } catch (IOException var4) {
         var4.printStackTrace();
         throw new ConnectorWebServiceRuntimeException(var4);
      }
   }

   private static void throwRuntimeExceptionOnHtml(String var0, String var1) {
      String var2 = var1.length() > 100 ? var1.substring(0, 100) + "..." : var1;
      if (var2.contains("html>")) {
         String var3 = "Invalid Url:" + var0 + "\n Netsuite returns html instead of wsdl: " + var2;
         throw new ConnectorWebServiceRuntimeException(var3);
      }
   }

   public static String downloadImpl(ProxySettings var0, String var1) throws ClientProtocolException, IOException {
      HttpRequestBase var2 = HttpRequestFactory.newHttpRequestBase("GET", var1);
      HttpCallResponse var3 = HttpCall.execute(var0, var2);
      var3.throwRuntimeExceptionOnBadResponse();
      return var3.getResponse();
   }
}
