package org.etlforge.integration.client.ldap.ui;

import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import org.etlforge.integration.client.mapping.serverapi.ldap.GetObjectClassesCallback;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.ldap.client.cache.LdapStructureCache;
import org.etlforge.integration.ldap.client.cache.LdapStructureServerCache;
import org.etlforge.integration.ldap.structure.LdapUtils;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;

public final class ObjectClassDefinitionRetriever {
   private LdapStructureServerCache ldapCache;

   public void setLdapCache(LdapStructureServerCache var1) {
      this.ldapCache = var1;
   }

   public void retrieveClassDefinitions(LdapLocation var1, GetObjectClassesCallback var2, boolean var3, WaitService var4) {
      if (!var3 || !this.checkCache(var1, var2)) {
         this.askServer(var1, var2, var4);
      }
   }

   public void retrieveClassDefinitions(LdapLocation var1, NumericOid[] var2, GetObjectClassesCallback var3, boolean var4, WaitService var5) {
      if (!var4 || this.ldapCache == null || !this.checkCache(var1, var2, var3)) {
         ObjectClassDefinitionRetriever.FilteredCallback var6 = new ObjectClassDefinitionRetriever.FilteredCallback(var2, var3);
         this.askServer(var1, var6, var5);
      }
   }

   private boolean checkCache(LdapLocation var1, GetObjectClassesCallback var2) {
      LdapStructureCache var3 = this.ldapCache.getCache(var1.getServer());
      ObjectClassDefinition[] var4 = var3.getObjectClasses();
      if (var4.length > 0) {
         var2.objectClassesRetrieved(var4);
         return true;
      } else {
         return false;
      }
   }

   private boolean checkCache(LdapLocation var1, NumericOid[] var2, GetObjectClassesCallback var3) {
      LdapStructureCache var4 = this.ldapCache.getCache(var1.getServer());
      ObjectClassDefinition[] var5 = var4.getObjectClasses(var2);
      if (var5.length == var2.length && !ArrayUtils.contains(var5, null)) {
         var3.objectClassesRetrieved(var5);
         return true;
      } else {
         return false;
      }
   }

   private void askServer(LdapLocation var1, GetObjectClassesCallback var2, WaitService var3) {
      WaitLock var4 = var3.startWait();
      GetClassDefinitionsServerCallJob var5 = new GetClassDefinitionsServerCallJob(var1, var2, var4);
      var5.submit();
   }

   private static final class FilteredCallback implements GetObjectClassesCallback {
      private final NumericOid[] requestedOids;
      private final GetObjectClassesCallback external;

      public FilteredCallback(NumericOid[] var1, GetObjectClassesCallback var2) {
         this.requestedOids = var1;
         this.external = var2;
      }

      @Override
      public void objectClassesRetrieved(ObjectClassDefinition[] var1) {
         ObjectClassDefinition[] var2 = new ObjectClassDefinition[this.requestedOids.length];
         Map var3 = LdapUtils.toMap(var1);

         for (int var4 = 0; var4 < var2.length; var4++) {
            ObjectClassDefinition var5 = (ObjectClassDefinition)var3.get(this.requestedOids[var4]);
            var2[var4] = var5;
         }

         this.external.objectClassesRetrieved(var2);
      }

      @Override
      public void cancelled() {
         this.external.cancelled();
      }

      @Override
      public void caught(Throwable var1) {
         this.external.caught(var1);
      }
   }
}
