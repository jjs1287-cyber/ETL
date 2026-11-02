package org.etlforge.ui.widget.tabbedpane;

import javax.swing.Icon;
import org.etlforge.ui.resources.UiResourceBundle;

final class PackageResources {
   static final Icon CLOSE_ICON = UiResourceBundle.getIcon("CloseX");

   private PackageResources() {
      throw new UnsupportedOperationException();
   }

   private static String getString(String var0) {
      return UiResourceBundle.getString(var0);
   }

   static final class CloseAllTabsAction {
      static final String NAME = PackageResources.getString("CloseableTabbedPane.Action.CloseAll.Name");
      static final String TOOLTIP = PackageResources.getString("CloseableTabbedPane.Action.CloseAll.ToolTip");
   }

   static final class CloseOtherTabsAction {
      static final String NAME = PackageResources.getString("CloseableTabbedPane.Action.CloseOthers.Name");
      static final String TOOLTIP = PackageResources.getString("CloseableTabbedPane.Action.CloseOthers.ToolTip");
   }

   static final class CloseTabAction {
      static final String NAME = PackageResources.getString("Close");
      static final String TOOLTIP = PackageResources.getString("CloseableTabbedPane.Action.Close.ToolTip");
   }
}
