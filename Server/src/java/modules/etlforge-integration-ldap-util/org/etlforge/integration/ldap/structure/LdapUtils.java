package org.etlforge.integration.ldap.structure;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Map;
import org.etlforge.util.name.Nameable;

public final class LdapUtils {
   public static <T extends UniqueLdapEntity & Nameable> String getRepresentation(T var0) {
      String var1 = ((Nameable)var0).getName();
      if (var1 == null || var1.length() == 0) {
         var1 = var0.getOid().toString();
      }

      return var1;
   }

   public static <T extends UniqueLdapEntity> Map<NumericOid, T> toMap(T[] var0) {
      Function var1 = new Function<T, NumericOid>() {
         public NumericOid apply(T var1) {
            return var1.getOid();
         }
      };
      return Maps.uniqueIndex(Arrays.asList((T[])var0), var1);
   }

   private LdapUtils() {
   }
}
