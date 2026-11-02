package com.etlforge.integration.salesforce;

import com.google.common.base.Objects;
import java.io.Serializable;
import org.apache.commons.codec.binary.Base64;
import org.etlforge.crypto.Providers;
import org.etlforge.integration.salesforce.LoginCredentials;
import org.etlforge.util.crypto.DefaultPBEStringCryptographer;

public final class EncryptedCredentials implements Serializable {
   private static final long serialVersionUID = 1109499840825565894L;
   public final String username;
   public final String encryptedPassword;
   public final String securityToken;
   public final boolean sandbox;

   public EncryptedCredentials(LoginCredentials var1) {
      this.username = var1.username;
      this.encryptedPassword = this.encrypt(var1.password);
      this.securityToken = var1.securityToken;
      this.sandbox = var1.sandbox;
   }

   private String encrypt(String var1) {
      try {
         return EncryptedCredentials.Crypto.encrypt(var1);
      } catch (Exception var3) {
         return null;
      }
   }

   public LoginCredentials decrypt() {
      String var1 = this.decryptPassword();
      return new LoginCredentials(this.username, var1, this.securityToken, this.sandbox);
   }

   private String decryptPassword() {
      if (this.encryptedPassword != null) {
         try {
            return EncryptedCredentials.Crypto.decrypt(this.encryptedPassword);
         } catch (Exception var2) {
         }
      }

      return null;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.username, this.encryptedPassword, this.securityToken, this.sandbox);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof EncryptedCredentials)) {
         return false;
      }

      EncryptedCredentials var2 = (EncryptedCredentials)var1;
      return this.username.equals(var2.username)
         && Objects.equal(this.encryptedPassword, var2.encryptedPassword)
         && this.securityToken.equals(var2.securityToken)
         && this.sandbox == var2.sandbox;
   }

   @Override
   public String toString() {
      return this.username + " / " + this.encryptedPassword + " / " + this.securityToken + " / sandbox=" + this.sandbox;
   }

   private static class Crypto {
      private static final String algorithm = "PBEWITHMD5AND128BITAES-CBC-OPENSSL";
      private static final String NYCKEL = "Protego Totalumdidum";
      private static final byte[] salt = "(..\n\t \n:".getBytes();
      private static final Base64 base64 = new Base64();

      public static String encrypt(String var0) throws Exception {
         DefaultPBEStringCryptographer var1 = new DefaultPBEStringCryptographer(salt, 1);
         var1.setAlgorithm("PBEWITHMD5AND128BITAES-CBC-OPENSSL");
         var1.setPassphrase("Protego Totalumdidum");
         byte[] var2 = var1.encrypt(var0);
         return new String(base64.encode(var2));
      }

      public static String decrypt(String var0) throws Exception {
         DefaultPBEStringCryptographer var1 = new DefaultPBEStringCryptographer(salt, 1);
         var1.setAlgorithm("PBEWITHMD5AND128BITAES-CBC-OPENSSL");
         var1.setPassphrase("Protego Totalumdidum");
         byte[] var2 = base64.decode(var0.getBytes());
         return var1.decrypt(var2);
      }

      static {
         Providers.registerDefaultProvider();
      }
   }
}
