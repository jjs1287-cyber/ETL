package org.etlforge.application.ui.window.menu;

import java.util.List;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.menu.ActionListMenuExtension;
import org.etlforge.application.ui.menu.MenuExtensionPoint;

public final class ActionsMenuExtension extends ActionListMenuExtension {
   public ActionsMenuExtension(List<ApplicationAction> var1) {
      super("menu.Actions", var1, MenuExtensionPoint.EXTENSION_POINT_1, "Actions.ClientExtension");
   }
}
