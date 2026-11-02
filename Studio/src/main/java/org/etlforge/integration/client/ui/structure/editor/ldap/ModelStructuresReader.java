package org.etlforge.integration.client.ui.structure.editor.ldap;

import org.etlforge.integration.client.ldap.ui.DefaultClassStructureRetrieverCallback;
import org.etlforge.integration.client.ldap.ui.ObjectClassStructureRetriever;
import org.etlforge.integration.client.ldap.ui.SelectedObjectClasses;
import org.etlforge.integration.client.ldap.ui.SelectedObjectStructures;
import org.etlforge.integration.ldap.client.cache.LdapStructureServerCache;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;
import org.etlforge.ui.wait.WaitService;

final class ModelStructuresReader {
   public SelectedObjectStructures getModelStructures(SelectedObjectClasses var1, LdapStructureServerCache var2) {
      SelectedObjectStructures var3 = new SelectedObjectStructures(var1);
      DefaultClassStructureRetrieverCallback var4 = new DefaultClassStructureRetrieverCallback();
      ObjectClassStructureRetriever var5 = new ObjectClassStructureRetriever();
      var5.setLdapCache(var2);
      var5.retrieveStructures(var1.getClassDefinitionOids(), var1.getLdapLocation(), var4, true, WaitService.NONE);
      ObjectClassStructure[] var6 = var4.getStructures();
      if (var6 == null) {
         return null;
      }

      for (ObjectClassStructure var10 : var6) {
         var3.addStructure(var10.getOid(), var10);
      }

      return var3;
   }
}
