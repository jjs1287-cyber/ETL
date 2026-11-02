package org.etlforge.ui.widget.popup;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.etlforge.ui.action.ActionContainer;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public final class PopupMenuActionContainer implements ActionContainer {
   private final JPopupMenu popup = new JPopupMenu();
   private final Map<Action, JMenuItem> addedItems = Maps.newIdentityHashMap();

   public JPopupMenu getPopup() {
      return this.popup;
   }

   @Override
   public void add(Action var1) {
      if (var1 == null) {
         this.popup.addSeparator();
      } else {
         this.addedItems.put(var1, this.createAndAddMenuItem(var1));
      }
   }

   @Override
   public void remove(Action var1) {
      JMenuItem var2 = this.addedItems.get(var1);
      if (var2 != null) {
         this.popup.remove(var2);
         this.addedItems.remove(var1);
      }
   }

   private JMenuItem createAndAddMenuItem(Action var1) {
      KongaMenuItem var2 = new KongaMenuItem(var1);
      this.popup.add(var2);
      return var2;
   }
}
