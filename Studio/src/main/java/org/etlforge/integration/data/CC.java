package org.etlforge.integration.data;

import com.google.common.base.Charsets;
import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;
import org.etlforge.crypto.Providers;
import org.etlforge.util.crypto.DefaultPBEStringCryptographer;
import org.etlforge.util.crypto.StringCryptographerException;
import org.etlforge.util.crypto.StringDecrypterException;
import org.etlforge.util.crypto.StringEncryptionException;

public final class CC {
   public static String hide(String var0) {
      try {
         byte[] var1 = getBytes("!;.\n\t ;:");
         DefaultPBEStringCryptographer var2 = new DefaultPBEStringCryptographer(var1, 1);
         var2.setAlgorithm("PBEWITHMD5AND128BITAES-CBC-OPENSSL");
         var2.setPassphrase("ybd?)5!j}X3^Ki${>Dq}Sz$/Le50c[");
         byte[] var3 = var2.encrypt(var0);
         Base64 var4 = new Base64();
         return new String(var4.encode(var3), Charsets.US_ASCII);
      } catch (StringCryptographerException var5) {
         throw new RuntimeException(var5);
      } catch (StringEncryptionException var6) {
         throw new RuntimeException(var6);
      }
   }

   private static byte[] getBytes(String var0) {
      try {
         return var0.getBytes(Charsets.US_ASCII.name());
      } catch (UnsupportedEncodingException var2) {
         throw new RuntimeException(var2);
      }
   }

   public static String reveal(String var0) {
      try {
         byte[] var1 = getBytes("!;.\n\t ;:");
         DefaultPBEStringCryptographer var2 = new DefaultPBEStringCryptographer(var1, 1);
         var2.setAlgorithm("PBEWITHMD5AND128BITAES-CBC-OPENSSL");
         var2.setPassphrase("ybd?)5!j}X3^Ki${>Dq}Sz$/Le50c[");
         Base64 var3 = new Base64();
         byte[] var4 = var3.decode(getBytes(var0));
         return var2.decrypt(var4);
      } catch (StringCryptographerException var5) {
         throw new RuntimeException(var5);
      } catch (StringDecrypterException var6) {
         throw new IllegalArgumentException(var6);
      }
   }

   private CC() {
   }

   static {
      Providers.registerDefaultProvider();
   }
}
