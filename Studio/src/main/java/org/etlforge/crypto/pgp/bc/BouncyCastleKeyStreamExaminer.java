package org.etlforge.crypto.pgp.bc;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;
import org.etlforge.crypto.CryptoException;
import org.etlforge.crypto.pgp.KeyStreamExaminer;
import org.etlforge.io.KongaIoUtils;

final class BouncyCastleKeyStreamExaminer implements KeyStreamExaminer {
   @Override
   public long[] findDecryptionKeyIds(InputStream var1) throws IOException, CryptoException {
      InputStream var2 = null;

      try {
         ArrayList var3 = new ArrayList();
         var2 = PGPUtil.getDecoderStream(var1);
         PGPSecretKeyRingCollection var4 = new PGPSecretKeyRingCollection(var2);
         Iterator var5 = var4.getKeyRings();

         while (var5.hasNext()) {
            PGPSecretKeyRing var6 = (PGPSecretKeyRing)var5.next();
            Iterator var7 = var6.getSecretKeys();

            while (var7.hasNext()) {
               PGPSecretKey var8 = (PGPSecretKey)var7.next();
               if (!var8.isSigningKey()) {
                  var3.add(var8.getKeyID());
               }
            }
         }

         return toLongArray(var3);
      } catch (PGPException var12) {
         throw new CryptoException(var12.getMessage(), var12);
      } finally {
         KongaIoUtils.close(var2);
      }
   }

   private static long[] toLongArray(List<Long> var0) {
      int var1 = var0.size();
      long[] var2 = new long[var1];

      for (int var3 = 0; var3 < var1; var3++) {
         var2[var3] = (Long)var0.get(var3);
      }

      return var2;
   }
}
