package org.etlforge.crypto.pgp.bc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;
import org.etlforge.crypto.pgp.Encryptor;
import org.etlforge.crypto.pgp.Key;
import org.etlforge.io.KongaIoUtils;

final class BcUtils {
   public static int getCompressionId(Encryptor.Compression var0) {
      switch (var0) {
         case NONE:
            return 0;
         case ZIP:
            return 1;
         case ZLIB:
            return 2;
         case BZIP2:
            return 3;
         default:
            throw new RuntimeException("Unexcpected Compression: " + var0);
      }
   }

   public static byte[] armor(byte[] var0) throws Exception {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      ArmoredOutputStream var2 = new ArmoredOutputStream(var1);
      var2.write(var0);
      var2.close();
      return var1.toByteArray();
   }

   public static byte[] literalize(byte[] var0) throws Exception {
      return literalize(var0, "", new Date());
   }

   public static byte[] literalize(byte[] var0, String var1, Date var2) throws Exception {
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      PGPLiteralDataGenerator var4 = new PGPLiteralDataGenerator();
      OutputStream var5 = var4.open(var3, 'b', var1, var0.length, new Date());
      var5.write(var0);
      var4.close();
      return var3.toByteArray();
   }

   public static PGPSecretKey readSecretKey(File var0, long var1) throws IOException, PGPException {
      FileInputStream var3 = new FileInputStream(var0);

      try {
         return readSecretKey(var3, var1);
      } finally {
         KongaIoUtils.close(var3);
      }
   }

   public static PGPSecretKey readSecretKey(Key var0) throws IOException, PGPException {
      long var1 = var0.getKeyId() != null ? var0.getKeyId() : -1L;
      return readSecretKey(var0.getKeyData(), var1);
   }

   public static PGPSecretKey readSecretKey(InputStream var0, long var1) throws IOException, PGPException {
      InputStream var3 = null;

      try {
         var3 = PGPUtil.getDecoderStream(var0);
         PGPSecretKeyRingCollection var4 = new PGPSecretKeyRingCollection(var3);
         PGPSecretKey var5 = null;
         Iterator var6 = var4.getKeyRings();

         while (var5 == null && var6.hasNext()) {
            PGPSecretKeyRing var7 = (PGPSecretKeyRing)var6.next();
            Iterator var8 = var7.getSecretKeys();

            while (var5 == null && var8.hasNext()) {
               PGPSecretKey var9 = (PGPSecretKey)var8.next();
               if (var9.isSigningKey() && (var1 < 0L || var9.getKeyID() == var1)) {
                  var5 = var9;
               }
            }
         }

         if (var5 == null) {
            throw new IllegalArgumentException("Cannot find signing key in key ring.");
         } else {
            return var5;
         }
      } finally {
         KongaIoUtils.close(var3);
      }
   }

   private BcUtils() {
   }
}
