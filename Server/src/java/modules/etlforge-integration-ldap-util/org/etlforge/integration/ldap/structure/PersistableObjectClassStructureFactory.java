package org.etlforge.integration.ldap.structure;

import org.etlforge.util.persist.PersistableFactory;
import org.etlforge.util.persist.Persistor;

class PersistableObjectClassStructureFactory implements PersistableFactory<PersistableObjectClassStructure> {
   public PersistableObjectClassStructure restore(Persistor var1) {
      ObjectClassStructureBuilder var2 = new ObjectClassStructureBuilder();
      String var3 = var1.getString("oid");
      var2.setOid(new NumericOid(var3));
      String var4 = var1.getString("name");
      var2.setName(var4);

      for (Persistor var6 : var1.getChildren("attribute")) {
         this.loadAttribute(var6, var2);
      }

      ObjectClassStructure var7 = var2.getStructure();
      return new PersistableObjectClassStructure(var7);
   }

   private void loadAttribute(Persistor var1, ObjectClassStructureBuilder var2) {
      LdapStructureAttribute var3 = LdapStructureAttributePersistor.read(var1);
      var2.addAttribute(var3);
   }
}
