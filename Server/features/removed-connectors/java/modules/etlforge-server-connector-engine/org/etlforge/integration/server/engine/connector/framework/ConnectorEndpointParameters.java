package org.etlforge.integration.server.engine.connector.framework;

import java.util.Properties;
import org.etlforge.util.net.ProxySettings;

public final class ConnectorEndpointParameters {
   private final String endpointTypeName;
   private final String host;
   private final String user;
   private String password;
   private final Properties AdditionalParameters;
   private final ProxySettings proxySettings;

   public ConnectorEndpointParameters(String var1, String var2, String var3, String var4, Properties var5, ProxySettings var6) {
      this.endpointTypeName = var1;
      this.host = var2;
      this.user = var3;
      this.password = var4;
      this.AdditionalParameters = var5;
      this.proxySettings = var6;
   }

   public ProxySettings getProxySettings() {
      return this.proxySettings;
   }

   public boolean hasProxySettings() {
      return !this.proxySettings.equals(ProxySettings.NONE);
   }

   public String getEndpointTypeName() {
      return this.endpointTypeName;
   }

   public String getHost() {
      return this.host;
   }

   public String getUser() {
      return this.user;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String var1) {
      this.password = var1;
   }

   public Properties getParameters() {
      return this.AdditionalParameters;
   }

   public String getEndpointString() {
      String var1 = "EndpointType="
         + this.getEndpointTypeName()
         + ";Server="
         + this.getHost()
         + ";User="
         + this.getUser()
         + ";Password=<removed>;"
         + this.getParameters().toString();
      return this.hasProxySettings() ? var1 + " ProxySettings: " + this.proxySettings : var1;
   }
}
