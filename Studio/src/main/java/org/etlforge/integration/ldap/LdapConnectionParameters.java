package org.etlforge.integration.ldap;

import org.etlforge.util.net.Port;

public final class LdapConnectionParameters {
   public final String serverName;
   public final Port port;
   public final String userName;
   public final String password;
   public final LdapSecurityLevel securityLevel;

   public LdapConnectionParameters(String var1, Port var2, String var3, String var4, LdapSecurityLevel var5) {
      this.serverName = var1;
      this.port = var2;
      this.userName = var3;
      this.password = var4;
      this.securityLevel = var5;
   }
}
