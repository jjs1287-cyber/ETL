package org.etlforge.integration.client.ldap.ui;

import org.etlforge.integration.client.ldap.ui.resources.LdapUiResources;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.ui.resources.UiResourceBundle;

final class PackageResources {
   private static String getString(String var0) {
      return LdapUiResources.STRINGS.getString(var0);
   }

   private PackageResources() {
   }

   static final class AttributeViewer {
      static final String NAME = UiResourceBundle.getString("Name") + ":";
      static final String DESC = UiResourceBundle.getString("Description") + ":";
      static final String USE = PackageResources.getString("Ldap.Attribute.Use") + ":";
      static final String CARDINALITY = PackageResources.getString("Ldap.Attribute.Cardinality") + ":";
      static final String READ_ONLY = UiResourceBundle.getString("ReadOnly") + ":";
      static final String OID = PackageResources.getString("Ldap.Attribute.Oid") + ":";

      static String getReadOnlyValue(LdapStructureAttribute var0) {
         return var0.isReadOnly() ? UiResourceBundle.getString("Yes") : UiResourceBundle.getString("No");
      }
   }

   static final class GetClassDefinitionsJob {
      static final String NAME = PackageResources.getString("Ldap.GetClassDefinitionsJob.Name");
      static final String ERROR = PackageResources.getString("Ldap.GetClassDefinitionsJob.Error");
   }

   static final class GetStructuresJob {
      static final String NAME = PackageResources.getString("Ldap.GetStructuresJob.Name");
      static final String ERROR = PackageResources.getString("Ldap.GetStructuresJob.Error");
   }
}
