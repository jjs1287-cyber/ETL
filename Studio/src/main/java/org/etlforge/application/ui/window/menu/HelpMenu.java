package org.etlforge.application.ui.window.menu;

import com.google.common.base.Supplier;
import java.util.Collections;
import java.util.List;
import javax.swing.Action;
import javax.swing.JMenuItem;
import org.etlforge.application.ui.menu.AbstractMenu;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public final class HelpMenu extends AbstractMenu {
   public static final String ACTION_ID = "help";
   private final Action viewLogsAction;

   public HelpMenu(Action action) {
      super("menu.Help");
      this.viewLogsAction = action;
   }

                                                                     
   public void setAboutUiFactory(Supplier<UiProvider> factory) { }
   public void setCheckForUpdatesAction(Action action) { }

   @Override
   protected List<JMenuItem> getMenuItems() {
      return Collections.<JMenuItem>singletonList(new KongaMenuItem(this.viewLogsAction));
   }
}
