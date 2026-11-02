package org.etlforge.integration.client.ui.interchange.component.menu;

import com.google.common.base.Function;
import java.util.List;
import javax.swing.Action;
import javax.swing.JMenuItem;
import org.etlforge.application.ui.menu.AbstractMenu;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public class InterchangeMenu extends AbstractMenu {
   private final List<? extends Action> actions;

   public InterchangeMenu(String var1, List<? extends Action> var2) {
      super(var1);
      this.actions = var2;
   }

   @Override
   protected final List<JMenuItem> getMenuItems() {
      List var1 = KongaListUtils.transform(this.actions, new InterchangeMenu.ItemFactory());
      this.addAdditionalItems(var1);
      return var1;
   }

   protected void addAdditionalItems(List<JMenuItem> var1) {
   }

   protected JMenuItem toItem(Action var1) {
      return var1 != null ? new KongaMenuItem(var1) : null;
   }

   private class ItemFactory implements Function<Action, JMenuItem> {
      private ItemFactory() {
      }

      public JMenuItem apply(Action var1) {
         return var1 == null ? null : InterchangeMenu.this.toItem(var1);
      }
   }
}
