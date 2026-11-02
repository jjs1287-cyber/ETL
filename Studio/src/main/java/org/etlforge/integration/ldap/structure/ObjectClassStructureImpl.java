package org.etlforge.integration.ldap.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class ObjectClassStructureImpl implements ObjectClassStructure {
   private final NumericOid oid;
   private final String name;
   private final List<LdapStructureAttribute> attributes;

   ObjectClassStructureImpl(NumericOid var1, String var2, Collection<LdapStructureAttribute> var3) {
      this.oid = var1;
      this.name = var2;
      this.attributes = new ArrayList<>(var3);
   }

   @Override
   public LdapStructureAttribute[] getAttributes() {
      return this.attributes.toArray(new LdapStructureAttribute[this.attributes.size()]);
   }

   @Override
   public NumericOid getOid() {
      return this.oid;
   }

   @Override
   public String getName() {
      return this.name;
   }
}
