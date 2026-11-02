package org.etlforge.integration.client.server.login;

import org.etlforge.integration.client.server.IllegalServerAddressException;
import org.etlforge.integration.client.server.ServerAddress;
import org.etlforge.integration.client.server.ServerAddressFactory;
import org.etlforge.util.crypto.DigestUtils;

public final class LoginCredentials {
   private static final boolean KEEP_PASSWORDS_1_2_COMPATIBLE = Boolean.getBoolean("KeepPasswordsCompatibleWith_1.2");
   private final ServerAddressFactory addressFactory;
   private final String server;
   private final String user;
   private final String password;
   private final String passwordHash;
   private final boolean useHttps;
   private final boolean storePasswordOnClient;

   public LoginCredentials(ServerAddressFactory var1, String var2, String var3, String var4, boolean var5, boolean var6) {
      this.addressFactory = var1;
      this.server = var2;
      this.user = var3;
      this.password = var4;
      this.passwordHash = KEEP_PASSWORDS_1_2_COMPATIBLE ? var4 : DigestUtils.getMd5DigestAsHexString(var4);
      this.useHttps = var5;
      this.storePasswordOnClient = var6;
   }

   public LoginCredentials(ServerAddressFactory var1, String var2, String var3, LoginCredentials.PasswordHash var4, boolean var5) {
      this.addressFactory = var1;
      this.server = var2;
      this.user = var3;
      this.password = null;
      this.passwordHash = var4.hash;
      this.useHttps = var5;
      this.storePasswordOnClient = false;
   }

   public String getServer() {
      return this.server;
   }

   public ServerAddress getServerAddress() throws IllegalServerAddressException {
      return this.addressFactory.fromString(this.server, this.useHttps);
   }

   public String getUser() {
      return this.user;
   }

   public String getPassword() {
      return this.password;
   }

   public String getPasswordHash() {
      return this.passwordHash;
   }

   public boolean isUsingHttps() {
      return this.useHttps;
   }

   public boolean storePasswordOnClient() {
      return this.storePasswordOnClient;
   }

   @Override
   public String toString() {
      String var1 = this.user + " @ " + this.server;
      if (this.useHttps) {
         var1 = var1 + " using https";
      }

      return var1;
   }

   public static class PasswordHash {
      public String hash;

      public PasswordHash(String var1) {
         this.hash = var1;
      }
   }
}
