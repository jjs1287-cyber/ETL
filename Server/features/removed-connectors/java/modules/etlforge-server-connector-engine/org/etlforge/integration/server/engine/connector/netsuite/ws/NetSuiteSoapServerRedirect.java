package org.etlforge.integration.server.engine.connector.netsuite.ws;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.client.methods.HttpRequestBase;
import org.etlforge.integration.server.engine.connector.exception.ConnectorWebServiceRuntimeException;
import org.etlforge.integration.server.engine.connector.util.HttpCallResponse;
import org.etlforge.integration.server.engine.connector.util.HttpRequestFactory;
import org.etlforge.util.net.ProxySettings;

public class NetSuiteSoapServerRedirect {
   public static URI redirectUrl(String var0, ProxySettings var1, HttpCallResponse var2) {
      URI var3 = redirectUrlImpl(var1, var2);
      return var3 == null ? null : newURI(var0, var3.toString());
   }

   private static URI redirectUrlImpl(ProxySettings var0, HttpCallResponse var1) {
      if (!var1.isRedirectUrl()) {
         return null;
      }

      String var2 = var1.getRedirectUrl();
      HttpRequestBase var3 = HttpRequestFactory.newHttpRequestBase("GET", var2);
      NetSuiteSoapServiceCall var4 = new NetSuiteSoapServiceCall(var0, var3);
      URI var5 = null;

      while (var1.isRedirectUrl()) {
         var5 = newURIInstance(var2);
         var3.setURI(var5);
         var1 = var4.execute();
         var2 = var1.getRedirectUrl();
      }

      return var5;
   }

   public static URI newURI(String var0, String var1) {
      try {
         URI var2 = new URI(var0);
         URI var3 = new URI(var1);
         return new URI(var3.getScheme(), var3.getHost(), var2.getPath(), var2.getFragment());
      } catch (URISyntaxException var5) {
         throw new ConnectorWebServiceRuntimeException(var5);
      }
   }

   public static URI newURIInstance(String var0) {
      try {
         return new URI(var0);
      } catch (URISyntaxException var2) {
         throw new ConnectorWebServiceRuntimeException(var2);
      }
   }
}
