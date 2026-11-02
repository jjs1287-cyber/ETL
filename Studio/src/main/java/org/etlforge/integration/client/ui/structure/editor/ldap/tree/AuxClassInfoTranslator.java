package org.etlforge.integration.client.ui.structure.editor.ldap.tree;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.integration.client.ldap.ui.DefaultClassStructureRetrieverCallback;
import org.etlforge.integration.client.ldap.ui.ObjectClassStructureRetriever;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.ldap.client.cache.LdapStructureServerCache;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;
import org.etlforge.ui.wait.WaitService;

final class AuxClassInfoTranslator {
   private final LdapLocation ldapLocation;
   private final LdapStructureServerCache ldapCache;

   public AuxClassInfoTranslator(LdapLocation var1, LdapStructureServerCache var2) {
      this.ldapLocation = var1;
      this.ldapCache = var2;
   }

   public Map<NumericOid, ObjectClassStructure[]> translate(Map<NumericOid, NumericOid[]> var1) {
      ApplicationUiUtils.logIfOnEDT(this.getClass(), "translate");
      HashMap var2 = Maps.newHashMap();

      for (Entry var4 : var1.entrySet()) {
         ObjectClassStructure[] var5 = this.toStructure((NumericOid[])var4.getValue());
         var2.put(var4.getKey(), var5);
      }

      return var2;
   }

   public ObjectClassStructure[] toStructure(NumericOid[] var1) {
      ObjectClassStructureRetriever var2 = new ObjectClassStructureRetriever();
      var2.setLdapCache(this.ldapCache);
      DefaultClassStructureRetrieverCallback var3 = new DefaultClassStructureRetrieverCallback();
      var2.retrieveStructures(var1, this.ldapLocation, var3, true, WaitService.NONE);
      return var3.getStructures();
   }
}
