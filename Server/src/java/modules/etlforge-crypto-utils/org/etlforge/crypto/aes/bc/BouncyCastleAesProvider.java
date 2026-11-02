package org.etlforge.crypto.aes.bc;

import org.etlforge.crypto.Providers;
import org.etlforge.crypto.aes.AesCipher;
import org.etlforge.crypto.aes.AesCipherProvider;
import org.etlforge.crypto.aes.KeyGenerator;

public final class BouncyCastleAesProvider implements AesCipherProvider {
   private static final AesCipherProvider INSTANCE = new BouncyCastleAesProvider();

   public static AesCipherProvider getInstance() {
      return INSTANCE;
   }

   private BouncyCastleAesProvider() {
      Providers.registerBouncyCastle();
   }

   @Override
   public AesCipher newCipher(KeyGenerator var1) {
      switch (var1) {
         case PKCS5S2:
            return new PKCS5S2AesCipher();
         case OPEN_SSL:
            return new OpenSslAesCipher();
         default:
            throw new RuntimeException("Unexpected KeyGenerator type: " + var1);
      }
   }
}
