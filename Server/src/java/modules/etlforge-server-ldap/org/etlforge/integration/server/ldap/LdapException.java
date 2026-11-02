package org.etlforge.integration.server.ldap;

public class LdapException extends RuntimeException {
   private LdapConnectionInfo connectionInfo;

   public LdapException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public LdapException(String var1) {
      super(var1);
   }

   public LdapException(Throwable var1) {
      super(var1);
   }

   public void setConnectionInfo(LdapConnectionInfo var1) {
      this.connectionInfo = var1;
   }

   public LdapConnectionInfo getConnectionInfo() {
      return this.connectionInfo;
   }

   @Override
   public String getMessage() {
      String var1 = super.getMessage();
      Throwable var2 = this.getCause();
      if (var2 == null) {
         return var1;
      }

      StringBuilder var3 = new StringBuilder(var1);
      if (!var1.endsWith(".")) {
         var3.append(". ");
      }

      var3.append("Cause: ").append(var2.toString());
      return var3.toString();
   }
}
