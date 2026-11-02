package org.etlforge.crypto.pgp.bc;

import org.etlforge.crypto.Providers;
import org.etlforge.crypto.pgp.Decryptor;
import org.etlforge.crypto.pgp.Encryptor;
import org.etlforge.crypto.pgp.KeyStreamExaminer;
import org.etlforge.crypto.pgp.PassphraseCrypto;
import org.etlforge.crypto.pgp.PgpProvider;

public final class BouncyCastlePgpProvider implements PgpProvider {
   private static PgpProvider INSTANCE = new BouncyCastlePgpProvider();

   public static PgpProvider getInstance() {
      return INSTANCE;
   }

   private BouncyCastlePgpProvider() {
      Providers.registerBouncyCastle();
   }

   @Override
   public Encryptor getEncryptor() {
      return new BouncyCastleEncryptor();
   }

   @Override
   public Decryptor getDecryptor() {
      return new BouncyCastleDecryptor();
   }

   @Override
   public KeyStreamExaminer getKeyStreamExaminer() {
      return new BouncyCastleKeyStreamExaminer();
   }

   @Override
   public PassphraseCrypto getPassphraseCrypto() {
      return new BouncyCastlePassphraseCryptoImpl();
   }
}
