package org.etlforge.integration.client.ldap.ui;

import java.util.ArrayList;
import org.etlforge.integration.client.mapping.serverapi.ldap.GetLdapStructuresCallback;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.ldap.client.cache.LdapStructureCache;
import org.etlforge.integration.ldap.client.cache.LdapStructureServerCache;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;

public final class ObjectClassStructureRetriever {
   private LdapStructureServerCache ldapCache;

   public void setLdapCache(LdapStructureServerCache var1) {
      this.ldapCache = var1;
   }

   public void retrieveStructures(LdapStructure var1, GetLdapStructuresCallback var2, boolean var3, WaitService var4) {
      this.retrieveStructures(var1.getObjectClassOids(), var1.getLdapLocation(), var2, var3, var4);
   }

   public void retrieveStructures(NumericOid[] var1, LdapLocation var2, GetLdapStructuresCallback var3, boolean var4, WaitService var5) {
      if (!var4 || this.ldapCache == null || !this.checkCache(var1, var2, var3)) {
         this.askServer(var1, var2, var3, var5);
      }
   }

   private boolean checkCache(NumericOid[] var1, LdapLocation var2, GetLdapStructuresCallback var3) {
      LdapStructureCache var4 = this.ldapCache.getCache(var2.getServer());
      ArrayList var5 = new ArrayList();

      for (NumericOid var9 : var1) {
         ObjectClassStructure var10 = var4.getClassStructure(var9);
         if (var10 == null) {
            return false;
         }

         var5.add(var10);
      }

      var3.structuresRetrieved((ObjectClassStructure[]) var5.toArray(new ObjectClassStructure[var5.size()]));
      return true;
   }

   private void askServer(NumericOid[] var1, LdapLocation var2, GetLdapStructuresCallback var3, WaitService var4) {
      WaitLock var5 = var4.startWait();
      GetClassStructuresServerCallJob var6 = new GetClassStructuresServerCallJob(var1, var2, var3, var5);
      var6.submit();
   }
}
