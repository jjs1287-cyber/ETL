package com.etlforge.integration.salesforce.wsdl;

public final class LoginUrl {
   private final String defaultUrl;
   private final String sandboxUrl;

   public LoginUrl(SalesforceWsdlInfo var1) {
      this(var1.getUrl());
   }

   public LoginUrl(String var1) {
      this.defaultUrl = var1;
      this.sandboxUrl = this.createSandboxUrl();
   }

   private String createSandboxUrl() {
      int var1 = this.defaultUrl.indexOf("://");
      String var2 = this.defaultUrl.substring(var1 + 3);
      int var3 = var2.indexOf(46);
      String var4 = var2.substring(0, var3);
      if (var4.toLowerCase().equals("test")) {
         return this.defaultUrl;
      }

      String var5 = this.defaultUrl.substring(0, var1 + 3);
      return var5 + "test" + var2.substring(var3);
   }

   public String getSandboxUrl() {
      return this.sandboxUrl;
   }

   public String getDefaultUrl() {
      return this.defaultUrl;
   }

   public boolean isValid(String var1) {
      return this.defaultUrl.equalsIgnoreCase(var1) || this.sandboxUrl.equalsIgnoreCase(var1);
   }

   @Override
   public String toString() {
      return this.defaultUrl;
   }
}
