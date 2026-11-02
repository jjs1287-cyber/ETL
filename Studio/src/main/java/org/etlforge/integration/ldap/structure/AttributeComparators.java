package org.etlforge.integration.ldap.structure;

import java.util.Comparator;
import org.etlforge.util.name.NameSorter;

public final class AttributeComparators {
   public static Comparator<LdapStructureAttribute> nameOrder(boolean var0) {
      return new NameSorter<>(var0);
   }

   public static Comparator<LdapStructureAttribute> useOrder() {
      return useOrder(nameOrder(true));
   }

   public static Comparator<LdapStructureAttribute> useOrder(Comparator<LdapStructureAttribute> var0) {
      return new AttributeComparators.UseAndNameOrder(var0);
   }

   private AttributeComparators() {
   }

   private static class UseAndNameOrder implements Comparator<LdapStructureAttribute> {
      private final Comparator<LdapStructureAttribute> tieBreaker;

      public UseAndNameOrder(Comparator<LdapStructureAttribute> var1) {
         this.tieBreaker = var1;
      }

      public int compare(LdapStructureAttribute var1, LdapStructureAttribute var2) {
         if (var1.getUse() == AttributeUse.REQUIRED) {
            if (var2.getUse() == AttributeUse.OPTIONAL) {
               return -1;
            }
         } else if (var2.getUse() == AttributeUse.REQUIRED) {
            return 1;
         }

         return this.tieBreaker != null ? this.tieBreaker.compare(var1, var2) : 0;
      }
   }
}
