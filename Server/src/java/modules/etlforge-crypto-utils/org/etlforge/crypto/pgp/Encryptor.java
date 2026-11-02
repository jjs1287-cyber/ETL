package org.etlforge.crypto.pgp;

import java.io.IOException;
import java.util.Date;
import org.etlforge.crypto.CryptoException;

public interface Encryptor {
   byte[] encrypt(byte[] var1, Key var2, boolean var3) throws CryptoException, IOException;

   byte[] signAndEncrypt(byte[] var1, Key var2, boolean var3, Key var4, char[] var5) throws CryptoException, IOException;

   void setMessageInfo(String var1, Date var2);

   void setCompression(Encryptor.Compression var1);

   enum Compression {
      NONE,
      ZIP,
      ZLIB,
      BZIP2;
   }
}
