package org.etlforge.integration.client.ui.structure.editor.ldap;

import java.text.MessageFormat;
import org.etlforge.integration.client.ui.structure.editor.resources.StructureEditorResources;
import org.etlforge.integration.ldap.structure.LdapUtils;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;

final class PackageResources {
   private static String getString(String var0) {
      return StructureEditorResources.getString(var0);
   }

   private static String getFormattedMessage(String var0, Object... var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   private PackageResources() {
   }

   static class Wizard {
      static final String TITLE = PackageResources.getString("LdapWizard.Title");

      static class ClassSelector {
         static final String TITLE = PackageResources.getString("LdapWizard.ClassSelector.Title");
         static final String MAIN_CAPTION = PackageResources.getString("LdapWizard.ClassSelector.MainCaption");
         static final String SUB_CAPTION = PackageResources.getString("LdapWizard.ClassSelector.SubCaption");
         static final String AVAILABLE = PackageResources.getString("LdapWizard.ClassSelector.Available");
         static final String SELECTED = PackageResources.getString("LdapWizard.ClassSelector.Selected");
         static final String PREVIEW = PackageResources.getString("LdapWizard.ClassSelector.Preview");
         static final String AUXILIARY_MESSAGE = PackageResources.getString("LdapWizard.ClassSelector.Auxiliary.Message");
         static final String AUXILIARY_TITLE = PackageResources.getString("LdapWizard.ClassSelector.Auxiliary.Title");
         static final String SEARCH = PackageResources.getString("LdapWizard.ClassSelector.Search");
         static final String SEARCH_TOOLTIP = PackageResources.getString("LdapWizard.ClassSelector.Search.ToolTip");

         static String getAuxiliaryToolTip(ObjectClassDefinition var0) {
            return PackageResources.getFormattedMessage("LdapWizard.ClassSelector.Auxiliary.ToolTip", LdapUtils.getRepresentation(var0));
         }
      }

      static class StructureReviewer {
         static final String TITLE = PackageResources.getString("LdapWizard.StructureReviewer.Title");
         static final String MAIN_CAPTION = PackageResources.getString("LdapWizard.StructureReviewer.MainCaption");
         static final String SUB_CAPTION = PackageResources.getString("LdapWizard.StructureReviewer.SubCaption");
         static final String TREE_TITLE = PackageResources.getString("LdapWizard.StructureReviewer.TreeTitle");
      }
   }
}
