package org.etlforge.integration.client.ui.interchange.component.menu;

import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.menu.ActionListMenuExtension;
import org.etlforge.application.ui.menu.MenuExtensionPoint;

public final class HelpMenuExtension extends ActionListMenuExtension {
   public HelpMenuExtension(List<? extends Action> var1) {
      super("menu.Help", var1, MenuExtensionPoint.EXTENSION_POINT_1, "Help.Design");
   }
}
