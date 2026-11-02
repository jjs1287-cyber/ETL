package org.etlforge.crypto.pgp.bc;

import com.google.common.base.Preconditions;
import java.util.EnumMap;
import java.util.Map;
import org.etlforge.crypto.pgp.SymmetricKeyAlgorithm;

final class SymmetricKeyAlgorithmMap {
   private static final Map<SymmetricKeyAlgorithm, Integer> map = new EnumMap<>(SymmetricKeyAlgorithm.class);

   static int convert(SymmetricKeyAlgorithm var0) {
      Preconditions.checkNotNull(var0, "algo");
      return map.get(var0);
   }

   private static void sanityCheck() {
      for (SymmetricKeyAlgorithm var3 : SymmetricKeyAlgorithm.values()) {
         assert map.get(var3) != null;
      }
   }

   private SymmetricKeyAlgorithmMap() {
   }

   static {
      map.put(SymmetricKeyAlgorithm.IDEA, 1);
      map.put(SymmetricKeyAlgorithm.TRIPLE_DES, 2);
      map.put(SymmetricKeyAlgorithm.CAST5, 3);
      map.put(SymmetricKeyAlgorithm.BLOWFISH, 4);
      sanityCheck();
   }
}
