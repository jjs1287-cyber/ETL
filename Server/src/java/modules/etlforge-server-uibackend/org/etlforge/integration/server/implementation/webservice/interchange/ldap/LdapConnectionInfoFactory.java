package org.etlforge.integration.server.implementation.webservice.interchange.ldap;

import java.rmi.RemoteException;
import org.etlforge.integration.ldap.LdapSecurityLevel;
import org.etlforge.integration.server.db.LocationPasswordLookup;
import org.etlforge.integration.server.ldap.LdapConnectionInfo;
import org.etlforge.util.net.Port;

class LdapConnectionInfoFactory {
   private LocationPasswordLookup pwdLookup;

   public void setLocationPasswordLookup(LocationPasswordLookup var1) {
      this.pwdLookup = var1;
   }

   public LdapConnectionInfo createConnectionInfo(WsLdapConnectionParams var1, String var2, String var3) throws RemoteException {
      try {
         String var4 = var1.getPassword();
         if (var4 == null && this.pwdLookup != null) {
            var4 = this.pwdLookup.lookupPassword(var2, var3);
         }

         LdapSecurityLevel var5 = LdapSecurityLevel.fromId(var1.getSecurityLevel());
         return new LdapConnectionInfo(var1.getServer(), getPort(var1), var1.getUser(), var4, var5);
      } catch (Exception var6) {
         throw new RemoteException(var6.getMessage());
      }
   }

   private static Port getPort(WsLdapConnectionParams var0) throws RemoteException {
      String var1 = var0.getPort();
      if (var1 != null && var1.length() > 0) {
         try {
            return Port.valueOf(Integer.parseInt(var1));
         } catch (NumberFormatException var3) {
            throw new RemoteException("Invalid port number: " + var1, var3);
         } catch (IllegalArgumentException var4) {
            throw new RemoteException("Invalid port number: " + var1, var4);
         }
      } else {
         return null;
      }
   }
}
