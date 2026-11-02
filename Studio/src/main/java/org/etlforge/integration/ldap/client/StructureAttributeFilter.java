package org.etlforge.integration.ldap.client;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.ldap.structure.AttributeFilter;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.integration.ldap.structure.LdapStructureEntry;
import org.etlforge.integration.ldap.structure.NumericOid;

public final class StructureAttributeFilter implements AttributeFilter {
   private final Set<LdapStructureEntry> entries = Sets.newHashSet();
   private final Map<NumericOid, Set<String>> selectedAttributes;

   public StructureAttributeFilter(LdapStructure var1) {
      for (LdapStructureEntry var5 : var1.getClassEntries()) {
         this.entries.add(var5);
      }

      this.selectedAttributes = Maps.newHashMap();
   }

   @Override
   public boolean test(LdapStructureEntry var1, LdapStructureAttribute var2) {
      if (!this.entries.contains(var1)) {
         return false;
      }

      if (var1.isExtendedAttribute(var2)) {
         return true;
      }

      NumericOid var3 = var1.getOid();
      Set var4 = null;
      if (this.selectedAttributes.containsKey(var3)) {
         var4 = this.selectedAttributes.get(var3);
      } else {
         String[] var5 = var1.getAttributeNames();
         var4 = var5 != null ? Sets.newHashSet(var5) : null;
         this.selectedAttributes.put(var3, var4);
      }

      return var4 != null ? var4.contains(var2.getName()) : true;
   }
}
