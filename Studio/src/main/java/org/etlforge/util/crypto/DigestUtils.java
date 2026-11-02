package org.etlforge.util.crypto;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;

public final class DigestUtils {
   public static byte[] getMd5FileDigest(File var0) throws IOException {
      HashCode var1 = Files.hash(var0, Hashing.md5());
      return var1.asBytes();
   }

   public static String getMd5FileDigestString(File var0) throws IOException {
      HashCode var1 = Files.hash(var0, Hashing.md5());
      return var1.toString();
   }

   public static byte[] getMd5Digest(String var0) {
      HashCode var1 = Hashing.md5().hashBytes(var0.getBytes());
      return var1.asBytes();
   }

   public static String getMd5DigestAsHexString(String var0) {
      HashCode var1 = Hashing.md5().hashBytes(var0.getBytes());
      return var1.toString();
   }

   private DigestUtils() {
   }
}
