package org.etlforge.application.ui.window.menu;

import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.menu.ActionListMenuExtension;
import org.etlforge.application.ui.menu.MenuExtensionPoint;

public final class ViewMenuExtension extends ActionListMenuExtension {
   public ViewMenuExtension(List<? extends Action> var1) {
      super("menu.View", var1, MenuExtensionPoint.EXTENSION_POINT_2, "View.KnownServers");
   }
}
