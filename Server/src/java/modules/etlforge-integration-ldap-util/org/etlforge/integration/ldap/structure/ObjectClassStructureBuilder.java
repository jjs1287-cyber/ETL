package org.etlforge.integration.ldap.structure;

import java.util.LinkedHashMap;
import java.util.Map;

public class ObjectClassStructureBuilder {
   private NumericOid oid;
   private String name;
   private Map<String, LdapStructureAttribute> attributes = new LinkedHashMap<>();

   public void setOid(NumericOid var1) {
      if (var1 == null) {
         throw new NullPointerException("oid");
      }

      this.oid = var1;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public void addAttribute(LdapStructureAttribute var1) {
      this.attributes.put(var1.getName(), var1);
   }

   public ObjectClassStructure getStructure() {
      if (this.oid == null) {
         throw new IllegalStateException("Must set the OID first");
      }

      ObjectClassStructureImpl var1 = new ObjectClassStructureImpl(this.oid, this.name, this.attributes.values());
      this.attributes = new LinkedHashMap<>();
      return var1;
   }
}
