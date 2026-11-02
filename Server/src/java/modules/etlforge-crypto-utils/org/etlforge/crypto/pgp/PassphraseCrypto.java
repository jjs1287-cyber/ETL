package org.etlforge.crypto.pgp;

import java.io.IOException;
import org.etlforge.crypto.CryptoException;

public interface PassphraseCrypto {
   byte[] encrypt(byte[] var1, char[] var2, SymmetricKeyAlgorithm var3) throws IOException, CryptoException;

   byte[] decrypt(byte[] var1, char[] var2) throws IOException, CryptoException;
}
