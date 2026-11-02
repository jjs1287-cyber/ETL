package org.etlforge.crypto.pgp.bc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;
import org.etlforge.io.KongaIoUtils;

public final class PublicKeyReader {
   public static PGPPublicKey readKey(File var0, long var1) throws IOException, PGPException {
      FileInputStream var3 = null;

      try {
         var3 = new FileInputStream(var0);
         return readKey(var3, var1);
      } finally {
         KongaIoUtils.close(var3);
      }
   }

   public static PGPPublicKey readKey(InputStream var0, long var1) throws IOException, PGPException {
      InputStream var3 = PGPUtil.getDecoderStream(var0);
      PGPPublicKeyRingCollection var4 = new PGPPublicKeyRingCollection(var3);
      return var4.getPublicKey(var1);
   }

   public static PGPPublicKey[] readKeys(File var0, String var1) throws IOException, PGPException {
      FileInputStream var2 = null;

      try {
         var2 = new FileInputStream(var0);
         return readKeys(var2, var1);
      } finally {
         KongaIoUtils.close(var2);
      }
   }

   public static PGPPublicKey[] readKeys(InputStream var0, String var1) throws IOException, PGPException {
      InputStream var2 = PGPUtil.getDecoderStream(var0);
      PGPPublicKeyRingCollection var3 = new PGPPublicKeyRingCollection(var2);
      ArrayList var4 = new ArrayList();
      Iterator var5 = var3.getKeyRings(var1);

      while (var5.hasNext()) {
         Iterator var6 = ((PGPPublicKeyRing)var5.next()).getPublicKeys();

         while (var6.hasNext()) {
            PGPPublicKey var7 = (PGPPublicKey)var6.next();
            if (var7.isEncryptionKey()) {
               var4.add(var7);
            }
         }
      }

      return (PGPPublicKey[]) var4.toArray(new PGPPublicKey[var4.size()]);
   }

   private PublicKeyReader() {
   }
}
