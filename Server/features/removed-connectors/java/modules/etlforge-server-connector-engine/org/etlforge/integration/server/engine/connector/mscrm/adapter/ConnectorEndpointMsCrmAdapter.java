package org.etlforge.integration.server.engine.connector.mscrm.adapter;

import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.server.engine.connector.util.testConnectionResponseParser;
import org.etlforge.util.net.Port;
import org.etlforge.util.net.ProxySettings;

public class ConnectorEndpointMsCrmAdapter {
   private final String baseUrl;
   private final String authInfoXml;
   public static final String discoveryServiceAddress = "discoveryServiceAddress";
   public static final String organizationUniqueName = "organizationUniqueName";
   public static final String authType = "authType";
   public static final String domain = "domain";

   public ConnectorEndpointMsCrmAdapter(String var1, String var2, String var3, String var4, String var5, String var6, String var7, ProxySettings var8) {
      this.baseUrl = var1;
      this.authInfoXml = this.getAuthInfo(var2, var3, var4, var5, var6, var7, var8);
   }

   public boolean testConnection() {
      MsCrmServiceHandler var1 = new MsCrmServiceHandler(MsCrmRequestBodyBuilder.TestConnectionOperation(), this.baseUrl, this.authInfoXml);
      String var2 = var1.callWs(MsCrmRequestBodyBuilder.TestConnectionBody());
      return testConnectionResponseParser.testConnectionResponse("Response", var2);
   }

   private void appendElement(StringBuilder var1, String var2, String var3) {
      XmlElementUtils.elementValue(var1, var2, var3);
   }

   private String getAuthInfo(String var1, String var2, String var3, String var4, String var5, String var6, ProxySettings var7) {
      StringBuilder var8 = new StringBuilder();
      var8.append("<authInfo>");
      this.appendElement(var8, "discoveryServiceAddress", var3);
      this.appendElement(var8, "organizationUniqueName", var4);
      this.appendElement(var8, "userName", var1);
      this.appendElement(var8, "password", var2);
      this.appendElement(var8, "domain", var6);
      this.appendElement(var8, "authType", var5);
      var8.append("</authInfo>");
      boolean var9 = var7.equals(ProxySettings.NONE);
      if (!var9) {
         var8.append("<proxyConnectionInfo>");
         this.appendElement(var8, "host", var7.getHost());
         this.appendElement(var8, "ntlmDomain", var7.getNtlmDomain());
         this.appendElement(var8, "password", var7.getPassword());
         Port var10 = var7.getPort();
         if (var10 != Port.DEFAULT) {
            this.appendElement(var8, "port", String.valueOf(var7.getPort().getPort()));
         }

         this.appendElement(var8, "username", var7.getUser());
         var8.append("</proxyConnectionInfo>");
      }

      return var8.toString();
   }

   public String getWsBaseUrl() {
      return this.baseUrl;
   }

   public String getAuthInfo() {
      return this.authInfoXml;
   }
}
