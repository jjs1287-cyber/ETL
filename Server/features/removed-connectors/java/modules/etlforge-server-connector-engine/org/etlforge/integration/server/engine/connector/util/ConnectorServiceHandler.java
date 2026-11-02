package org.etlforge.integration.server.engine.connector.util;

import java.io.IOException;
import java.net.ConnectException;
import org.apache.http.client.ClientProtocolException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorWebServiceRuntimeException;
import org.etlforge.io.FileUtils;
import org.etlforge.util.net.ProxySettings;

public abstract class ConnectorServiceHandler {
   final String url;
   final String authInfoXml;
   final String connectorServiceDescription;
   private final ProxySettings proxySettings;

   public ConnectorServiceHandler(ProxySettings var1, String var2, String var3, String var4) {
      this.proxySettings = var1;
      this.url = var2;
      this.authInfoXml = var3;
      this.connectorServiceDescription = var4;
   }

   private String buildRequest(String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("<Request>");
      var2.append("<body>");
      var2.append(XmlUtils.removeXmlHeader(var1));
      var2.append("</body>");
      var2.append(this.authInfoXml);
      var2.append("</Request>");
      return var2.toString();
   }

   protected abstract void buildRequestBodyFile(XmlWriter var1, String var2) throws IOException;

   private void buildRequestFile(String var1, String var2) {
      XmlWriter var3 = new XmlWriter(var2);

      try {
         var3.append(XmlUtils.xmlUtf8Header);
         var3.append("<Request>");
         var3.append("<body>");
         this.buildRequestBodyFile(var3, var1);
         var3.append("</body>");
         var3.append(this.authInfoXml);
         var3.append("</Request>");
      } catch (IOException var8) {
         throw new ConnectorServerRuntimeException(var8);
      } finally {
         var3.dispose();
      }
   }

   public String callWs(String var1) {
      return this.callWs(var1, true);
   }

   public String callWs(String var1, boolean var2) {
      String var3 = this.buildRequest(var1);
      String var4 = this.call(var3);
      ConnectorResponseSanityChecker.checkXml(var4, var2);
      return var4;
   }

   private String call(String var1) {
      HttpPostCall var2 = new HttpPostCall(this.proxySettings, this.url);

      try {
         return var2.call(var1);
      } catch (ClientProtocolException var4) {
         throw new ConnectorWebServiceRuntimeException(var4);
      } catch (IOException var5) {
         this.processSpecialException(var5);
         throw new ConnectorWebServiceRuntimeException(var5);
      }
   }

   private void processSpecialException(IOException var1) {
      if (var1 instanceof ConnectException) {
         String var2 = "The call to url " + this.url + " failed. \n reason : " + var1.getMessage();
         throw new ConnectorWebServiceRuntimeException(
            var2 + " \n Please confirm the " + this.connectorServiceDescription + " Connector Service is started", var1
         );
      }
   }

   public void callWsFile(String var1, String var2) {
      String var3 = FileUtils.getPostfixFilenameXml(var1, "_req");
      this.buildRequestFile(var1, var3);
      this.callFile(var3, var2);
      ConnectorResponseSanityChecker.checkXmlFile(var2);
   }

   private void callFile(String var1, String var2) {
      HttpPostCall var3 = new HttpPostCall(this.proxySettings, this.url);

      try {
         var3.callFile(var1, var2);
      } catch (ClientProtocolException var5) {
         throw new ConnectorWebServiceRuntimeException(var5);
      } catch (IOException var6) {
         this.processSpecialException(var6);
         throw new ConnectorWebServiceRuntimeException(var6);
      }
   }
}
