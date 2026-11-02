package org.etlforge.integration.client.ui.interchange.core.action;

import javax.swing.Action;
import org.etlforge.application.Application;
import org.etlforge.application.DebugMode;
import org.etlforge.ui.widget.menu.KongaMenu;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

public final class EntityDebugActions {
   public void addToPopupMenu(KongaPopupMenu var1) {
      assert DebugMode.IS_ENABLED : "The EntityDebugActions should be used in debug mode only";
      var1.add(Application.getBean("action.ic.AttachedListeners", Action.class));
   }

   public void addToMenu(KongaMenu var1) {
      assert DebugMode.IS_ENABLED : "The EntityDebugActions should be used in debug mode only";
      var1.add(Application.getBean("action.ic.AttachedListeners", Action.class));
   }
}
