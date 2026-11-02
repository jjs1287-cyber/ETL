package org.etlforge.util.id;

import java.io.Serializable;

public abstract class KongaID implements Serializable {
   @Override
   public abstract boolean equals(Object var1);

   @Override
   public abstract int hashCode();

   public static boolean areEqual(KongaID var0, KongaID var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }
}
