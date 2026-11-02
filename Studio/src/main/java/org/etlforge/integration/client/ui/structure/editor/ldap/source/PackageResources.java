package org.etlforge.integration.client.ui.structure.editor.ldap.source;

import java.text.MessageFormat;
import org.etlforge.integration.client.ui.structure.editor.resources.StructureEditorResources;

final class PackageResources {
   private static String getString(String var0) {
      return StructureEditorResources.getString(var0);
   }

   private static String getFormattedMessage(String var0, Object... var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   private PackageResources() {
   }

   static class SearchParamsDefiner {
      static final String BASE_DN = PackageResources.getString("LdapWizard.SearchParamsDefiner.BaseDN");
      static final String ATTRIBUTES = PackageResources.getString("LdapWizard.SearchParamsDefiner.Attributes");
      static final String FILTER = PackageResources.getString("LdapWizard.SearchParamsDefiner.Filter");
      static final String BASE_PATH_TOOLTIP = PackageResources.getString("LdapWizard.SearchParamsDefiner.BasePathToolTip");
      static final String SCOPE_TITLE = PackageResources.getString("LdapWizard.SearchParamsDefiner.Scope.Title");

      static String getDropDownToopTip(String var0) {
         return PackageResources.getFormattedMessage("LdapWizard.SearchParamsDefiner.DropDown.ToolTip", var0);
      }
   }

   static class SearchParamsPage {
      static final String TITLE = PackageResources.getString("LdapWizard.SearchParamsPage.Title");
      static final String MAIN_CAPTION = PackageResources.getString("LdapWizard.SearchParamsPage.MainCaption");
      static final String SUB_CAPTION = PackageResources.getString("LdapWizard.SearchParamsPage.SubCaption");
   }

   static class SourceSelector {
      static final String TITLE = PackageResources.getString("LdapWizard.SourceSelector.Title");
      static final String MAIN_CAPTION = PackageResources.getString("LdapWizard.SourceSelector.MainCaption");
      static final String SUB_CAPTION = PackageResources.getString("LdapWizard.SourceSelector.SubCaption");
      static final String COMBO_LABEL = PackageResources.getString("LdapWizard.SourceSelector.Combo");
   }
}
