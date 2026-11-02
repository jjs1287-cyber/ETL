package org.etlforge.integration.data.structure.ldap;

import org.etlforge.util.persist.Persistor;

public class LdapSearchParametersPersistor {
   private static final String CLASSNAME = "ClassName";
   private static final String BASE_DN = "BaseDN";
   private static final String FILTER = "Filter";
   private static final String SCOPE = "Scope";

   public void writeTo(LdapSearchParameters var1, Persistor var2) {
      var2.putString("ClassName", var1.getClassName());
      var2.putText("BaseDN", var1.getBaseDN());
      var2.putText("Filter", var1.getFilter());
      var2.putString("Scope", var1.getScope().name());
   }

   public LdapSearchParameters restoreFrom(Persistor var1) {
      String var2 = var1.getString("ClassName");
      if (var2 != null && !var2.isEmpty()) {
         LdapSearchScope var3 = null;

         try {
            var3 = LdapSearchScope.valueOf(var1.getString("Scope"));
         } catch (RuntimeException var6) {
            throw new IllegalArgumentException("Invalid persistor: the Scope field is missing or invalid");
         }

         String var4 = var1.getString("BaseDN");
         String var5 = var1.getString("Filter");
         return new LdapSearchParameters(var2, var4, var5, var3);
      } else {
         throw new IllegalArgumentException("Invalid persistor: the ClassName field is missing or empty");
      }
   }
}
