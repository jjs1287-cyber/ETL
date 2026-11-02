package org.etlforge.application.ui.widget;

import javax.swing.Action;
import javax.swing.JPopupMenu;
import org.etlforge.ui.action.ToggleAction;
import org.etlforge.ui.widget.menu.KongaMenuItem;
import org.etlforge.ui.widget.menu.PopupMenuActionPopulator;
import org.etlforge.ui.widget.menu.ToggleActionCheckBoxMenuItem;

public final class PopupMenuActionPopulators {
   public static final PopupMenuActionPopulator DEFAULT = new PopupMenuActionPopulator() {
      @Override
      public void addMenuItem(JPopupMenu var1, Action var2) {
         if (var2 == null) {
            var1.addSeparator();
         } else if (var2 instanceof ToggleAction) {
            var1.add(new ToggleActionCheckBoxMenuItem((ToggleAction)var2));
         } else {
            var1.add(new KongaMenuItem(var2));
         }
      }
   };

   private PopupMenuActionPopulators() {
   }
}
