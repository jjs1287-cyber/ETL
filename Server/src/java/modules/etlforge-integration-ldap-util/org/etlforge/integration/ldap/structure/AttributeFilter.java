package org.etlforge.integration.ldap.structure;

public interface AttributeFilter {
   AttributeFilter ACCEPTS_ALL = new AttributeFilter() {
      @Override
      public boolean test(LdapStructureEntry var1, LdapStructureAttribute var2) {
         return true;
      }
   };

   boolean test(LdapStructureEntry var1, LdapStructureAttribute var2);
}
