package org.etlforge.integration.structure.crom;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public final class CromPredicates {
   public static Predicate<CROM> ofType(long var0) {
      return new CromPredicates.TypePredicate(var0);
   }

   public static Predicate<CROM> notOfType(long var0) {
      return Predicates.not(ofType(var0));
   }

   private CromPredicates() {
   }

   private static class TypePredicate implements Predicate<CROM> {
      private final long type;

      public TypePredicate(long var1) {
         this.type = var1;
      }

      public boolean apply(CROM var1) {
         return (var1.getType() & this.type) != 0L;
      }
   }
}
