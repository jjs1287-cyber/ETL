package org.etlforge.integration.ldap.structure;

public final class LdapToCromConverterFactory {
   public static LdapToCromConverter newSourceStructureConverter() {
      return LdapToCromConverterImpl.newSourceStructureConverter();
   }

   public static LdapToCromConverter newTargetStructureConverter() {
      return LdapToCromConverterImpl.newTargetStructureConverter();
   }

   private LdapToCromConverterFactory() {
   }
}
