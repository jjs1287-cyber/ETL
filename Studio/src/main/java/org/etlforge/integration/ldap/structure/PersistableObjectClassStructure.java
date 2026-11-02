package org.etlforge.integration.ldap.structure;

import org.etlforge.util.persist.Persistable;
import org.etlforge.util.persist.Persistor;

class PersistableObjectClassStructure implements Persistable {
   private final ObjectClassStructure structure;

   PersistableObjectClassStructure(ObjectClassStructure var1) {
      this.structure = var1;
   }

   ObjectClassStructure getStructure() {
      return this.structure;
   }

   @Override
   public String getId() {
      return this.structure.getOid().toString();
   }

   @Override
   public void save(Persistor var1) {
      var1.putString("oid", this.structure.getOid().toString());
      var1.putString("name", this.structure.getName());

      for (LdapStructureAttribute var5 : this.structure.getAttributes()) {
         LdapStructureAttributePersistor.write(var5, var1);
      }
   }
}
