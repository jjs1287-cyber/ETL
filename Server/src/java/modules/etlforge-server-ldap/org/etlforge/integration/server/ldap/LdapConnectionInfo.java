package org.etlforge.integration.server.ldap;

import java.util.Hashtable;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import org.etlforge.integration.ldap.LdapSecurityLevel;
import org.etlforge.util.net.Port;

public final class LdapConnectionInfo {
   private static Port DEFAULT_NON_SSL_PORT = Port.valueOf(389);
   private static Port DEFAULT_SSL_PORT = Port.valueOf(636);
   private final String url;
   private final String login;
   private final String password;
   private final LdapSecurityLevel secure;

   public LdapConnectionInfo(String var1, Port var2, String var3, String var4, LdapSecurityLevel var5) {
      if (var5 == null) {
         throw new NullPointerException("secure");
      }

      this.url = createUrl(var1, var2, var5);
      this.login = var3;
      this.password = var4;
      this.secure = var5;
   }

   public DirContext asDirContext() throws LdapException {
      Hashtable var1 = new Hashtable(11);
      var1.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
      var1.put("java.naming.provider.url", this.url);
      if (this.login != null) {
         var1.put("java.naming.security.principal", this.login);
      }

      if (this.password != null) {
         var1.put("java.naming.security.credentials", this.password);
      }

      var1.put("java.naming.security.authentication", "simple");
      this.secure.applyToContext(var1);

      try {
         return new InitialDirContext(var1);
      } catch (NamingException var4) {
         LdapException var3 = new LdapException("Failed to create DirContext", var4);
         var3.setConnectionInfo(this);
         throw var3;
      }
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof LdapConnectionInfo ? this.equals((LdapConnectionInfo)var1) : false;
      }
   }

   private boolean equals(LdapConnectionInfo var1) {
      return this.url.equals(var1.url) && equals(this.login, var1.login) && equals(this.password, var1.password);
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.url.hashCode();
      var1 = 31 * var1 + (this.login != null ? this.login.hashCode() : 0);
      return 31 * var1 + (this.password != null ? this.password.hashCode() : 0);
   }

   @Override
   public String toString() {
      return this.toStringWithoutPassword();
   }

   public String toStringWithoutPassword() {
      return this.toStringImpl(false);
   }

   public String toStringWithPassword() {
      return this.toStringImpl(true);
   }

   private String toStringImpl(boolean var1) {
      StringBuilder var2 = new StringBuilder(this.url);
      var2.append(";login=").append(this.login);
      var2.append(";password=");
      if (var1) {
         var2.append(this.password);
      } else {
         var2.append("[* removed *]");
      }

      var2.append(";security level=").append(this.secure);
      return var2.toString();
   }

   private static String createUrl(String var0, Port var1, LdapSecurityLevel var2) {
      String var3 = var0.trim();
      if (var3.length() == 0) {
         throw new IllegalArgumentException("server may not be empty");
      }

      StringBuilder var4 = new StringBuilder();
      if (!var3.toLowerCase().startsWith("ldap://")) {
         var4.append("ldap://");
      }

      var4.append(var3);
      if (var1 == null) {
         var1 = var2 == LdapSecurityLevel.SECURE_SSL ? DEFAULT_SSL_PORT : DEFAULT_NON_SSL_PORT;
      }

      var4.append(":").append(var1.getPort());
      return var4.toString();
   }

   private static boolean equals(Object var0, Object var1) {
      return var0 != null ? var0.equals(var1) : var1 == null;
   }
}
