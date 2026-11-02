package org.etlforge.integration.client.ui.structure.editor.ldap.target;

import org.etlforge.integration.client.ui.structure.editor.resources.StructureEditorResources;

final class PackageResources {
   private static String getString(String var0) {
      return StructureEditorResources.getString(var0);
   }

   private PackageResources() {
   }

   static class TargetSelector {
      static final String TITLE = PackageResources.getString("LdapWizard.TargetSelector.Title");
      static final String MAIN_CAPTION = PackageResources.getString("LdapWizard.TargetSelector.MainCaption");
      static final String SUB_CAPTION = PackageResources.getString("LdapWizard.TargetSelector.SubCaption");
      static final String COMBO_LABEL = PackageResources.getString("LdapWizard.TargetSelector.Combo");
   }
}
