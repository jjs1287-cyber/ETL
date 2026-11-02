package org.etlforge.integration.ldap;

import java.util.Hashtable;
import org.etlforge.system.EnumWithId;
import org.etlforge.system.EnumWithIdLookup;

public enum LdapSecurityLevel implements EnumWithId<Integer> {
   NON_SECURE(0),
   SECURE(1),
   SECURE_SSL(2);

   private final int id;

   LdapSecurityLevel(int var3) {
      this.id = var3;
   }

   public void applyToContext(Hashtable<Object, Object> var1) {
      switch (this) {
         case SECURE_SSL:
            var1.put("java.naming.security.protocol", "ssl");
      }
   }

   public Integer getId() {
      return this.id;
   }

   public static LdapSecurityLevel fromId(int var0) {
      return EnumWithIdLookup.lookup(LdapSecurityLevel.class, var0);
   }
}
