package org.etlforge.application.ui.view.menu;

import com.google.common.base.Preconditions;
import javax.swing.Action;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public final class ActionBasedViewMenuItemFactory implements ViewMenuItemFactory {
   private final Action action;

   public ActionBasedViewMenuItemFactory(Action var1) {
      Preconditions.checkNotNull(var1, "action");
      this.action = var1;
   }

   public KongaMenuItem createMenuItem(ApplicationWindow var1) {
      return new KongaMenuItem(this.action);
   }
}
