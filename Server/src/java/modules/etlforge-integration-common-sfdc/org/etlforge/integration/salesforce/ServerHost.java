package org.etlforge.integration.salesforce;

import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.dataelement.DataElementResolver;
import org.etlforge.system.ArgCheck;

public final class ServerHost {
   private final String url;

   public static ServerHost defaultHost() {
      return fromUrl(null);
   }

   public static ServerHost fromUrl(String var0) {
      if (StringUtils.isBlank(var0)) {
         var0 = "https://login.salesforce.com";
      }

      return new ServerHost(var0);
   }

   private ServerHost(String var1) {
      this.url = ArgCheck.checkNotEmpty(var1, "url");
   }

   public String getUrl() {
      return this.url;
   }

   public String getLoginUrl(SalesforceVersion var1) {
      return this.constructLoginUrl(this.url, var1);
   }

   private String constructLoginUrl(String var1, SalesforceVersion var2) {
      String var3 = var1;
      String var4 = "services/Soap/u/" + var2.getId();
      if (var1.contains(var4)) {
         return var1;
      }

      if (!var3.endsWith("/")) {
         var3 = var3 + "/";
      }

      return var3 + var4;
   }

   public String getSandboxUrl() {
      return "https://test.salesforce.com/";
   }

   public String getSandboxLoginUrl(SalesforceVersion var1) {
      return this.constructLoginUrl(this.getSandboxUrl(), var1);
   }

   @Override
   public int hashCode() {
      return this.url.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 instanceof ServerHost && this.url.equals(((ServerHost)var1).url);
   }

   @Override
   public String toString() {
      return this.url;
   }

   ServerHost resolveDataElementDefaultValues() {
      String var1 = DataElementResolver.resolveDefaultValues(this.url);
      return new ServerHost(var1);
   }
}
