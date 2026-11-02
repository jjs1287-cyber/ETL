package org.etlforge.integration.client.server;

import com.google.common.base.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.crypto.aes.AesCipher;
import org.etlforge.crypto.aes.AesKeyLength;
import org.etlforge.crypto.aes.KeyGenerator;
import org.etlforge.crypto.aes.bc.BouncyCastleAesProvider;

final class ServerPasswordCrypto {
   private static final String PHRASE = "En rutig elefant boxade mig i magen";
   private static final byte[] SALT = new byte[]{-64, -1, -18, 21, -70, -33, 11, 26};
   private static final int IC = 100;
   private final AesCipher cipher = this.createCipher();

   public ServerPasswordCrypto() {
   }

   public String encrypt(String var1) {
      try {
         byte[] var2 = this.cipher.encrypt(var1.getBytes(Charsets.UTF_8));
         return new String(Base64.encodeBase64(var2), Charsets.UTF_8);
      } catch (Exception var3) {
         ErrorLog.log(this.getClass(), "An error occurred when encrypting the password. The password is not stored on the client.", var3);
         return null;
      }
   }

   public String decrypt(String var1) {
      try {
         byte[] var2 = Base64.decodeBase64(var1.getBytes(Charsets.UTF_8));
         byte[] var3 = this.cipher.decrypt(var2);
         return new String(var3, Charsets.UTF_8);
      } catch (Exception var4) {
         ErrorLog.log(this.getClass(), "An error occurred when decrypting the password.", var4);
         return null;
      }
   }

   private AesCipher createCipher() {
      AesCipher var1 = BouncyCastleAesProvider.getInstance().newCipher(KeyGenerator.PKCS5S2);
      var1.setPassphrase("En rutig elefant boxade mig i magen".toCharArray());
      var1.setKeyLength(AesKeyLength.KEY256);
      var1.setSalt(SALT);
      var1.setIterationCount(100);
      return var1;
   }
}
