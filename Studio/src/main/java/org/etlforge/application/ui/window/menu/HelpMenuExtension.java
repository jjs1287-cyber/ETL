package org.etlforge.application.ui.window.menu;

import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.menu.ActionListMenuExtension;
import org.etlforge.application.ui.menu.MenuExtensionPoint;

public class HelpMenuExtension extends ActionListMenuExtension {
   public HelpMenuExtension(List<? extends Action> var1) {
      super("menu.Help", var1, MenuExtensionPoint.EXTENSION_POINT_2, "Help.WebPages");
   }
}
