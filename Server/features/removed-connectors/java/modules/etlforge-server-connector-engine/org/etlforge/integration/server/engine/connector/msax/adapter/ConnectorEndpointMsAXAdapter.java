package org.etlforge.integration.server.engine.connector.msax.adapter;

import com.google.common.collect.ImmutableList;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.server.engine.connector.exception.ConnectorInvalidInputRuntimeException;
import org.etlforge.integration.server.engine.connector.msax.rest.MsAxRequestBodyDesign;
import org.etlforge.integration.server.engine.connector.util.testConnectionResponseParser;
import org.etlforge.util.csv.CsvUtils;
import org.etlforge.util.net.Port;
import org.etlforge.util.net.ProxySettings;

public class ConnectorEndpointMsAXAdapter {
   private final String baseUrl;
   private final String authinfo;

   public ConnectorEndpointMsAXAdapter(String var1, String var2, String var3, String var4, String var5, ProxySettings var6) {
      this.baseUrl = getWsBaseUrl(var1, var2);
      this.authinfo = this.getAuthInfo(var3, var4, var5, var6);
   }

   public boolean testConnection(String var1) {
      String var2 = getFirstWsdlUrl(var1);
      return this.testConnectionImpl(var2);
   }

   private static String getFirstWsdlUrl(String var0) {
      if (var0 != null) {
         var0 = var0.trim();
         if (!var0.isEmpty()) {
            ImmutableList var1 = CsvUtils.toValues(var0);
            if (var1.size() > 0) {
               return (String)var1.get(0);
            }
         }
      }

      return "";
   }

   private boolean testConnectionImpl(String var1) {
      String var2 = var1.trim();
      if (var2.isEmpty()) {
         String var3 = "No input for wsdl url";
         throw new ConnectorInvalidInputRuntimeException(var3);
      } else {
         String var4 = MsAxRequestBodyDesign.testConnectionRequestBody("testconnection", var2);
         MsAXServiceHandler var5 = new MsAXServiceHandler(this.getServiceUrl("testconnection"), this.getAuthInfo());
         String var6 = var5.callWs(var4);
         return testConnectionResponseParser.testConnectionResponse("Response", var6);
      }
   }

   private final void appendElement(StringBuilder var1, String var2, String var3) {
      XmlElementUtils.elementValue(var1, var2, var3);
   }

   public String getAuthInfo() {
      return this.authinfo;
   }

   private String getAuthInfo(String var1, String var2, String var3, ProxySettings var4) {
      StringBuilder var5 = new StringBuilder();
      var5.append("<authInfo>");
      this.appendElement(var5, "domain", var3);
      this.appendElement(var5, "username", var1);
      this.appendElement(var5, "password", var2);
      var5.append("</authInfo>");
      boolean var6 = !var4.equals(ProxySettings.NONE);
      if (var6) {
         var5.append("<proxyConnectionInfo>");
         this.appendElement(var5, "host", var4.getHost());
         this.appendElement(var5, "ntlmDomain", var4.getNtlmDomain());
         this.appendElement(var5, "password", var4.getPassword());
         Port var7 = var4.getPort();
         if (var7 != Port.DEFAULT) {
            this.appendElement(var5, "port", String.valueOf(var4.getPort().getPort()));
         }

         this.appendElement(var5, "username", var4.getUser());
         var5.append("</proxyConnectionInfo>");
      }

      return var5.toString();
   }

   private static String getWsBaseUrl(String var0, String var1) {
      if (var1 != null) {
         var1 = var1.trim();
         if (var1.isEmpty()) {
            var1 = null;
         }
      }

      if (var1 == null) {
         return var0;
      } else {
         return !var1.endsWith("/") && !var1.endsWith("\\") ? var1 + "/" : var1;
      }
   }

   public String getServiceUrl(String var1) {
      return this.baseUrl + var1;
   }
}
