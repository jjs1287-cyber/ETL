package org.etlforge.integration.data.structure.ldap;

public enum LdapSearchScope {
   BASE(0),
   ONE_LEVEL(1),
   SUB_TREE(2);

   private final int apiCode;

   LdapSearchScope(int var3) {
      this.apiCode = var3;
   }

   public static LdapSearchScope fromApiCode(int var0) {
      for (LdapSearchScope var4 : values()) {
         if (var4.apiCode == var0) {
            return var4;
         }
      }

      throw new IllegalArgumentException("There is no LdapSearchScope corresponding to the API Code " + Integer.toString(var0) + ".");
   }

   public int getApiCode() {
      return this.apiCode;
   }
}
