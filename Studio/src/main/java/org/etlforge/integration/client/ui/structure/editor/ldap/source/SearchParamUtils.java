package org.etlforge.integration.client.ui.structure.editor.ldap.source;

import org.etlforge.integration.ldap.structure.LdapStructureEntryNode;

final class SearchParamUtils {
   static String getDefaultFilter(LdapStructureEntryNode var0) {
      return "(objectClass=" + var0.getName() + ")";
   }

   private SearchParamUtils() {
   }
}
