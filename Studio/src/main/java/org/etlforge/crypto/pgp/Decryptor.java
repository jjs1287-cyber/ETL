package org.etlforge.crypto.pgp;

import java.io.IOException;
import org.etlforge.crypto.CryptoException;

public interface Decryptor {
   DecryptionResult decrypt(byte[] var1, Key var2, char[] var3) throws IOException, CryptoException;

   Long getEncryptionKeyId(byte[] var1) throws IOException, CryptoException;

   void setVerificationKey(Key var1);
}
