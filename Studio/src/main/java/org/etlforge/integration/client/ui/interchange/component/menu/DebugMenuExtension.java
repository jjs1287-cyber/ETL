package org.etlforge.integration.client.ui.interchange.component.menu;

import java.util.List;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.menu.ActionListMenuExtension;
import org.etlforge.application.ui.menu.MenuExtensionPoint;

public final class DebugMenuExtension extends ActionListMenuExtension {
   public DebugMenuExtension(List<? extends ApplicationAction> var1) {
      super("menu.Debug", var1, MenuExtensionPoint.EXTENSION_POINT_2, "Debug.DesignExtension");
   }
}
