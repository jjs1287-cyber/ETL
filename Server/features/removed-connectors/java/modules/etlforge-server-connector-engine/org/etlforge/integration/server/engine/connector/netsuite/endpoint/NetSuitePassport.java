package org.etlforge.integration.server.engine.connector.netsuite.endpoint;

import com.google.common.base.Strings;
import org.etlforge.integration.design.netsuite.NetSuiteWsdlDownloadUrl;
import org.etlforge.integration.server.engine.connector.exception.ConnectorInvalidInputRuntimeException;
import org.etlforge.util.net.ProxySettings;

public class NetSuitePassport {
   private final String email;
   private final String password;
   private final String account;
   private final boolean isSandBox;
   private final ProxySettings proxySettings;
   private final NetSuiteWsdlDownloadUrl netsuiteWsdlDownloadUrl;

   public NetSuitePassport(ProxySettings var1, boolean var2, String var3, String var4, String var5, String var6) {
      this.isSandBox = var2;
      this.email = var3;
      this.password = var4;
      this.account = var5;
      this.proxySettings = var1;
      this.netsuiteWsdlDownloadUrl = new NetSuiteWsdlDownloadUrl(var6);
   }

   public ProxySettings getProxySettings() {
      return this.proxySettings;
   }

   public boolean isSandBox() {
      return this.isSandBox;
   }

   public String getNetsuiteWsdlDownloadUrl() {
      return this.netsuiteWsdlDownloadUrl.getWsdlDownloadUrl();
   }

   public void validate() {
      StringBuilder var1 = new StringBuilder();
      if (Strings.isNullOrEmpty(this.getEmail())) {
         var1.append("No email,");
      }

      if (Strings.isNullOrEmpty(this.getAccount())) {
         var1.append("No account,");
      }

      if (!this.netsuiteWsdlDownloadUrl.isValid()) {
         var1.append("invalid wsdl download url,");
      }

      boolean var2 = var1.length() > 0;
      if (var2) {
         String var3 = "No input for " + var1.substring(0, var1.length() - 1);
         throw new ConnectorInvalidInputRuntimeException(var3);
      }
   }

   public String getAccount() {
      return this.account;
   }

   public String getAccountKey() {
      return this.netsuiteWsdlDownloadUrl.getKey() + this.isSandBox + this.account;
   }

   public String getEmail() {
      return this.email;
   }

   public String getPassword() {
      return this.password;
   }
}
