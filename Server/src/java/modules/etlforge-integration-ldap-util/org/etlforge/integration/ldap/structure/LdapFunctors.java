package org.etlforge.integration.ldap.structure;

import com.google.common.base.Function;

public final class LdapFunctors {
   public static final Function<String, NumericOid> STRING_TO_OID = new Function<String, NumericOid>() {
      public NumericOid apply(String var1) {
         return new NumericOid(var1);
      }
   };
   public static final Function<ObjectClassDefinition, NumericOid> CLASS_TO_OID = new Function<ObjectClassDefinition, NumericOid>() {
      public NumericOid apply(ObjectClassDefinition var1) {
         return var1.getOid();
      }
   };
   public static final Function<ObjectClassDefinition, LdapStructureEntry> CLASS_TO_ENTRY = new Function<ObjectClassDefinition, LdapStructureEntry>() {
      public LdapStructureEntry apply(ObjectClassDefinition var1) {
         return new LdapStructureEntry(var1.getOid());
      }
   };

   private LdapFunctors() {
   }
}
