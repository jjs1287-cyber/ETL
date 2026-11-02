package org.etlforge.integration.server.engine.connector.netsuite.ws;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpRequestBase;
import org.etlforge.integration.server.engine.connector.exception.ConnectorNoResponseRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorWebServiceRuntimeException;
import org.etlforge.integration.server.engine.connector.util.HttpCall;
import org.etlforge.integration.server.engine.connector.util.HttpCallResponse;
import org.etlforge.util.net.ProxySettings;

public class NetSuiteSoapServiceCall {
   private final HttpRequestBase httpRequest;
   private final ProxySettings proxySettings;

   NetSuiteSoapServiceCall(ProxySettings var1, HttpRequestBase var2) {
      this.proxySettings = var1;
      this.httpRequest = var2;
   }

   public ProxySettings getProxySettings() {
      return this.proxySettings;
   }

   HttpCallResponse execute() {
      return this.execute(false);
   }

   HttpCallResponse execute(boolean var1) {
      try {
         HttpCallResponse var2 = HttpCall.execute(this.proxySettings, this.httpRequest, var1);
         this.checkCallResponse(var2);
         return var2;
      } catch (ClientProtocolException var3) {
         throw new ConnectorWebServiceRuntimeException(var3);
      } catch (IOException var4) {
         throw new ConnectorWebServiceRuntimeException(var4);
      }
   }

   HttpCallResponse executeFile(String var1) {
      try {
         HttpCallResponse var2 = HttpCall.executeFile(this.proxySettings, this.httpRequest, var1);
         this.checkCallResponse(var2);
         return var2;
      } catch (ClientProtocolException var3) {
         throw new ConnectorWebServiceRuntimeException(var3);
      } catch (IOException var4) {
         throw new ConnectorWebServiceRuntimeException(var4);
      }
   }

   private void checkCallResponse(HttpCallResponse var1) {
      if (var1.hasNoResponseContent()) {
         String var2 = "No valid response! possibly caused by invalid request data \n" + this.httpRequest.getURI();
         throw new ConnectorNoResponseRuntimeException(var2);
      }
   }
}
