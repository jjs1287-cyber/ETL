package org.etlforge.crypto.pgp;

public interface PgpProvider {
   Encryptor getEncryptor();

   Decryptor getDecryptor();

   KeyStreamExaminer getKeyStreamExaminer();

   PassphraseCrypto getPassphraseCrypto();
}
