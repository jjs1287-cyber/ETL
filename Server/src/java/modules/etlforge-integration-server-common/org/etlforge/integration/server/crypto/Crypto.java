package org.etlforge.integration.server.crypto;

import com.google.common.base.Charsets;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.etlforge.util.crypto.DefaultPBEStringCryptographer;
import org.etlforge.util.crypto.StringCryptographerException;
import org.etlforge.util.crypto.StringDecrypterException;
import org.etlforge.util.crypto.StringEncryptionException;

public final class Crypto {
   private static final String algorithm = "PBEWITHMD5AND128BITAES-CBC-OPENSSL";

   public static String encryptString(String var0) throws ServerCryptoException {
      return encryptString(var0, "ybd?)5!j}X3^Ki${>Dq}Sz$/Le50c[");
   }

   public static String encryptString(String var0, String var1) throws ServerCryptoException {
      try {
         byte[] var2 = getBytes("!;.\n\t ;:");
         DefaultPBEStringCryptographer var3 = new DefaultPBEStringCryptographer(var2, 1);
         var3.setAlgorithm("PBEWITHMD5AND128BITAES-CBC-OPENSSL");
         var3.setPassphrase(var1);
         byte[] var4 = var3.encrypt(var0);
         Base64 var5 = new Base64();
         return new String(var5.encode(var4));
      } catch (StringCryptographerException var6) {
         throw new ServerCryptoException(var6);
      } catch (StringEncryptionException var7) {
         throw new ServerCryptoException(var7);
      }
   }

   private static byte[] getBytes(String var0) {
      try {
         return var0.getBytes(Charsets.US_ASCII.name());
      } catch (UnsupportedEncodingException var2) {
         throw new RuntimeException(var2);
      }
   }

   public static String decryptString(String var0) throws ServerCryptoException {
      return decryptString(var0, "ybd?)5!j}X3^Ki${>Dq}Sz$/Le50c[");
   }

   public static String decryptString(String var0, String var1) throws ServerCryptoException {
      try {
         byte[] var2 = getBytes("!;.\n\t ;:");
         DefaultPBEStringCryptographer var3 = new DefaultPBEStringCryptographer(var2, 1);
         var3.setAlgorithm("PBEWITHMD5AND128BITAES-CBC-OPENSSL");
         var3.setPassphrase(var1);
         Base64 var4 = new Base64();
         byte[] var5 = var4.decode(getBytes(var0));
         return var3.decrypt(var5);
      } catch (StringCryptographerException var6) {
         throw new ServerCryptoException(var6);
      } catch (StringDecrypterException var7) {
         throw new ServerCryptoException(var7);
      }
   }

   private Crypto() {
   }

   static {
      Security.addProvider(new BouncyCastleProvider());
   }
}
