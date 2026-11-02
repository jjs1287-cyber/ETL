package org.etlforge.crypto.aes;

import java.io.File;
import java.io.IOException;
import org.etlforge.crypto.CryptoException;

public interface AesCipher {
   int BLOCK_SIZE = 128;

   void encrypt(File var1, File var2) throws IOException, CryptoException;

   byte[] encrypt(byte[] var1) throws IOException, CryptoException;

   void decrypt(File var1, File var2) throws IOException, CryptoException;

   byte[] decrypt(byte[] var1) throws IOException, CryptoException;

   void setKeyLength(AesKeyLength var1);

   void setPassphrase(char[] var1);

   void setIterationCount(int var1);

   void setSalt(byte[] var1);
}
