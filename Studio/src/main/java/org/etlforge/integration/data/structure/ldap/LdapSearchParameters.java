package org.etlforge.integration.data.structure.ldap;

public final class LdapSearchParameters {
   private final String className;
   private final String baseDN;
   private final String filter;
   private final LdapSearchScope scope;
   public static final String TRANSFORMATION_ID = "transformation_id";
   public static final String CLASS_NAME = "classname";
   public static final String PATH = "path";
   public static final String FILTER = "filter";
   public static final String SCOPE = "scope";
   public static final String OID = "oid";

   public static String[] getLdapSearchParametersColumnNames() {
      return new String[]{"transformation_id", "classname", "path", "filter", "scope", "oid"};
   }

   public LdapSearchParameters(String var1, String var2, String var3, LdapSearchScope var4) {
      if (var1.length() == 0) {
         throw new IllegalArgumentException("className cannot be empty");
      }

      if (var4 == null) {
         throw new NullPointerException("scope");
      }

      this.className = var1;
      this.baseDN = getNonNullString(var2);
      this.filter = getNonNullString(var3);
      this.scope = var4;
   }

   public String getClassName() {
      return this.className;
   }

   public String getBaseDN() {
      return this.baseDN;
   }

   public String getFilter() {
      return this.filter;
   }

   public LdapSearchScope getScope() {
      return this.scope;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof LdapSearchParameters ? this.equals((LdapSearchParameters)var1) : false;
      }
   }

   private boolean equals(LdapSearchParameters var1) {
      return this.className.equals(var1.className) && this.filter.equals(var1.filter) && this.baseDN.equals(var1.baseDN) && this.scope == var1.scope;
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.className.hashCode();
      var1 = 31 * var1 + this.filter.hashCode();
      var1 = 31 * var1 + this.baseDN.hashCode();
      return 31 * var1 + this.scope.hashCode();
   }

   private static String getNonNullString(String var0) {
      return var0 != null ? var0 : "";
   }
}
