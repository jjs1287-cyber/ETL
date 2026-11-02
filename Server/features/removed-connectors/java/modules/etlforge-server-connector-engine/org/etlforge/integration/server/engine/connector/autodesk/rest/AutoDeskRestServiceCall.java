package org.etlforge.integration.server.engine.connector.autodesk.rest;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpRequestBase;
import org.etlforge.integration.server.engine.connector.util.HttpCall;
import org.etlforge.integration.server.engine.connector.util.HttpCallResponse;
import org.etlforge.util.net.ProxySettings;
import org.xml.sax.SAXException;

public class AutoDeskRestServiceCall {
   private final HttpRequestBase httpRequest;
   private final ProxySettings proxySettings;

   AutoDeskRestServiceCall(ProxySettings var1, HttpRequestBase var2) {
      this(var1, var2, null);
   }

   AutoDeskRestServiceCall(ProxySettings var1, HttpRequestBase var2, AutoDeskSession var3) {
      this.proxySettings = var1;
      this.httpRequest = var2;
      this.setHeaders();
      if (var3 != null) {
         this.httpRequest.addHeader("Cookie", var3.toCookie());
      }
   }

   private void setHeaders() {
      this.httpRequest.addHeader("Content-Type", "application/xml;charset=utf-8");
      this.httpRequest.addHeader("Accept", "application/xml;charset=utf-8");
   }

   AutoDeskCallResponse execute() throws ClientProtocolException, IOException, SAXException {
      return this.execute(false);
   }

   AutoDeskCallResponse execute(boolean var1) throws ClientProtocolException, IOException, SAXException {
      HttpCallResponse var2 = HttpCall.execute(this.proxySettings, this.httpRequest, var1);
      String var3 = AutoDeskErrorResponse.getErrorMessage(var2);
      return new AutoDeskCallResponse(var2, var3);
   }

   AutoDeskCallResponse executeFile(String var1) throws ClientProtocolException, IOException, SAXException {
      HttpCallResponse var2 = HttpCall.executeFile(this.proxySettings, this.httpRequest, var1);
      String var3 = AutoDeskErrorResponse.getErrorMessageFile(var2, var1);
      return new AutoDeskCallResponse(var2, var3);
   }
}
