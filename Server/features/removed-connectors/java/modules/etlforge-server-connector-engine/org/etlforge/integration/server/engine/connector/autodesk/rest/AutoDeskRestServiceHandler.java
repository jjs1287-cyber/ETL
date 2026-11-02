package org.etlforge.integration.server.engine.connector.autodesk.rest;

import java.io.File;
import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpRequestBase;
import org.etlforge.integration.server.engine.connector.exception.ConnectorWebServiceRuntimeException;
import org.etlforge.integration.server.engine.connector.util.HttpRequestFactory;
import org.etlforge.util.net.ProxySettings;
import org.xml.sax.SAXException;

public class AutoDeskRestServiceHandler {
   final HttpRequestBase httpRequest;

   AutoDeskRestServiceHandler(String var1, String var2) {
      this.httpRequest = HttpRequestFactory.newHttpRequestBase(var1, var2);
   }

   AutoDeskRestServiceHandler(String var1, String var2, String var3) {
      this.httpRequest = HttpRequestFactory.newHttpEntityEnclosingRequestBase(var1, var2, var3);
   }

   AutoDeskRestServiceHandler(String var1, String var2, File var3) {
      this.httpRequest = HttpRequestFactory.newHttpEntityEnclosingRequestBase(var1, var2, var3);
   }

   AutoDeskCallResponse executeLogin(ProxySettings var1) {
      return this.execute(var1, null);
   }

   AutoDeskCallResponse execute(ProxySettings var1, AutoDeskSession var2) {
      try {
         if (var2 == null) {
            AutoDeskRestServiceCall var7 = new AutoDeskRestServiceCall(var1, this.httpRequest);
            return var7.execute(true);
         } else {
            AutoDeskRestServiceCall var3 = new AutoDeskRestServiceCall(var1, this.httpRequest, var2);
            return var3.execute();
         }
      } catch (ClientProtocolException var4) {
         throw new ConnectorWebServiceRuntimeException(var4);
      } catch (IOException var5) {
         throw new ConnectorWebServiceRuntimeException(var5);
      } catch (SAXException var6) {
         throw new ConnectorWebServiceRuntimeException(var6);
      }
   }

   AutoDeskCallResponse executeFile(ProxySettings var1, AutoDeskSession var2, String var3) {
      try {
         AutoDeskRestServiceCall var4 = new AutoDeskRestServiceCall(var1, this.httpRequest, var2);
         return var4.executeFile(var3);
      } catch (ClientProtocolException var5) {
         throw new ConnectorWebServiceRuntimeException(var5);
      } catch (IOException var6) {
         throw new ConnectorWebServiceRuntimeException(var6);
      } catch (SAXException var7) {
         throw new ConnectorWebServiceRuntimeException(var7);
      }
   }
}
