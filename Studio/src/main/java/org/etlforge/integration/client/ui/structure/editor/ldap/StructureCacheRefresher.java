package org.etlforge.integration.client.ui.structure.editor.ldap;

import org.etlforge.integration.client.ldap.ui.ObjectClassStructureRetriever;
import org.etlforge.integration.client.ldap.ui.SelectedObjectClasses;
import org.etlforge.integration.client.ldap.ui.SelectedObjectStructures;
import org.etlforge.integration.client.mapping.serverapi.ldap.GetLdapStructuresCallback;
import org.etlforge.integration.ldap.client.cache.LdapStructureServerCache;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.wait.WaitService;

final class StructureCacheRefresher {
   private final LdapStructureServerCache ldapCache;
   private final WaitService waitService;

   public StructureCacheRefresher(LdapStructureServerCache var1, WaitService var2) {
      this.ldapCache = var1;
      this.waitService = var2;
   }

   public void refresh(final SelectedObjectClasses var1, final Receiver<SelectedObjectStructures> var2) {
      GetLdapStructuresCallback var3 = new GetLdapStructuresCallback() {
         @Override
         public void structuresRetrieved(ObjectClassStructure[] var1x) {
            SelectedObjectStructures var2x = new SelectedObjectStructures(var1);

            for (ObjectClassStructure var6 : var1x) {
               var2x.addStructure(var6.getOid(), var6);
            }

            var2.handle(var2x);
         }

         @Override
         public void cancelled() {
         }

         @Override
         public void caught(Throwable var1x) {
         }
      };
      ObjectClassStructureRetriever var4 = new ObjectClassStructureRetriever();
      var4.setLdapCache(this.ldapCache);
      var4.retrieveStructures(var1.getClassDefinitionOids(), var1.getLdapLocation(), var3, false, this.waitService);
   }
}
