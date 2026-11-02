package org.etlforge.integration.client.ui.structure.editor.ldap.tree;

import javax.swing.Icon;
import org.etlforge.integration.client.ui.structure.editor.resources.StructureEditorResources;

final class PackageResources {
   private static String getString(String var0) {
      return StructureEditorResources.getString(var0);
   }

   private PackageResources() {
   }

   static class Tree {
      static final String ATTRIBUTE_PROPERTIES_TITLE = PackageResources.getString("Ldap.SelectionTree.AttributePropertiesTitle");

      static class Search {
         static final String CAPTION = PackageResources.getString("Ldap.SelectionTree.Search.Caption");
         static final String TOOLTIP = PackageResources.getString("Ldap.SelectionTree.Search.ToolTip");
      }

      static class SelectAll {
         static final String NAME = PackageResources.getString("Ldap.SelectionTree.SelectRequired.Name");
         static final String TOOLTIP = PackageResources.getString("Ldap.SelectionTree.SelectRequired.ToolTip");
      }

      static class SetAuxClasses {
         static final String NAME = PackageResources.getString("Ldap.SelectionTree.SetAuxClasses.Name");
         static final String TOOLTIP = PackageResources.getString("Ldap.SelectionTree.SetAuxClasses.ToolTip");
      }
   }

   static class TreeModel {
      static final String CLASS_NODE_PREFIX = PackageResources.getString("Ldap.SelectionTree.ClassNodePrefix");
      static final String NOT_IN_CACHE = PackageResources.getString("Ldap.SelectionTree.NotInCache");
      static final Icon ENTRY_ICON = null;
   }
}
