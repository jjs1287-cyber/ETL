package org.etlforge.integration.client.ui.interchange.component.tree;

import javax.swing.Icon;
import org.etlforge.application.ui.resources.ApplicationResources;

enum ExpansionMode {
   EXPAND,
   COLLAPSE;

   public void apply(MultiTreeContentViewer var1) {
      switch (this) {
         case EXPAND:
            var1.expandAll(null);
            break;
         case COLLAPSE:
            var1.collapseAll(null);
            break;
         default:
            throw new RuntimeException("Unexpected mode: " + this);
      }
   }

   public Icon getIcon() {
      switch (this) {
         case EXPAND:
            return ApplicationResources.ICONS.getIcon("ExpandTree.16");
         case COLLAPSE:
            return ApplicationResources.ICONS.getIcon("CollapseTree.16");
         default:
            throw new RuntimeException("Unexpected mode: " + this);
      }
   }
}
