package org.etlforge.integration.client.ui.interchange.component.page.entity;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import org.etlforge.ui.util.KeyUtils;

final class ShowContextMenuAction extends AbstractAction {
   private static final String KEY = ShowContextMenuAction.class.getName();
   private final IntegrationEntityPage page;

   public static ShowContextMenuAction install(IntegrationEntityPage var0) {
      ShowContextMenuAction var1 = new ShowContextMenuAction(var0);
      var0.getMainPanel().getInputMap(1).put(KeyUtils.CONTEXT_MENU, KEY);
      var0.getMainPanel().getActionMap().put(KEY, var1);
      return var1;
   }

   private ShowContextMenuAction(IntegrationEntityPage var1) {
      this.page = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.page.showContextMenu(8, 8);
   }

   public void uninstall() {
      InputMap var1 = this.page.getMainPanel().getInputMap(1);
      Object var2 = var1.get(KeyUtils.CONTEXT_MENU);
      if (KEY.equals(var2)) {
         var1.remove(KeyUtils.CONTEXT_MENU);
      }

      this.page.getMainPanel().getActionMap().remove(KEY);
   }
}
