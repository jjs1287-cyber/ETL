package org.etlforge.integration.server.engine.connector.msgp.adapter;

import com.google.common.base.Strings;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.server.engine.connector.exception.ConnectorInvalidInputRuntimeException;

public class MsGPConnectorEndpointAdapter {
   private final String baseurl;
   private final String authinfo;
   public static final String SERVERINSTANCENAME = "serverinstancename";
   public static final String DATABASENAME = "databasename";
   public static final String SERVICE_URL = "service_url";
   private static final String SERVICE_Suffix = "GP/";

   public MsGPConnectorEndpointAdapter(String var1, String var2, String var3) {
      this.baseurl = this.getWsBaseUrl(var1);
      this.authinfo = this.getAuthInfo(var2, var3);
   }

   public boolean testConnection() {
      MsGPServiceHandler var3 = new MsGPServiceHandler("TestConnection", this.getWsBaseUrl(), this.getAuthInfo());
      String var4 = var3.callWs("<testconnection/>", false);
      return true;
   }

   public String getWsBaseUrl() {
      return this.baseurl;
   }

   public String getAuthInfo() {
      return this.authinfo;
   }

   private String getWsBaseUrl(String var1) {
      if (Strings.isNullOrEmpty(var1)) {
         throw new ConnectorInvalidInputRuntimeException("No GP Service Url");
      }

      if (var1.length() < 4) {
         throw new ConnectorInvalidInputRuntimeException("GP Service Url " + var1 + " is invalid");
      }

      char var2 = var1.charAt(var1.length() - 1);
      String var3 = var1;
      if (var2 != '/') {
         if (var2 == '\\') {
            var3 = var3.substring(0, var3.length() - 1);
         }

         var3 = var3 + '/';
      }

      String var4 = var3.toUpperCase();
      if (!var4.endsWith("GP/")) {
         var3 = var3 + "GP/";
      }

      return var3;
   }

   private String getAuthInfo(String var1, String var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append("<authInfo>");
      XmlElementUtils.elementValue(var3, "serverinstancename", var1);
      XmlElementUtils.elementValue(var3, "databasename", var2);
      var3.append("</authInfo>");
      return var3.toString();
   }
}
