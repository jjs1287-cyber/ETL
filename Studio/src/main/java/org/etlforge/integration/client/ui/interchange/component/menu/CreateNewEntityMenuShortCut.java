package org.etlforge.integration.client.ui.interchange.component.menu;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.system.Disposable;

public final class CreateNewEntityMenuShortCut implements Disposable {
   private static final KeyStroke SHORTCUT = KeyStroke.getKeyStroke("ctrl pressed N");
   private final JRootPane rootPane;
   private final Object oldKey;
   private final Action oldAction;

   public CreateNewEntityMenuShortCut(EntityConfigLookup var1, NewEntityMenuFactory var2, ApplicationWindow var3) {
      this.rootPane = var3.getFrame().getRootPane();
      InputMap var4 = this.rootPane.getInputMap(2);
      ActionMap var5 = this.rootPane.getActionMap();
      this.oldKey = var4.get(SHORTCUT);
      this.oldAction = this.oldKey != null ? var5.get(this.oldKey) : null;
      var4.put(SHORTCUT, "new-entity");
      var5.put("new-entity", new CreateNewEntityMenuShortCut.ActionImpl(var1, var2));
   }

   @Override
   public void dispose() {
      if (this.oldKey != null) {
         this.rootPane.getInputMap(2).put(SHORTCUT, this.oldKey);
         this.rootPane.getActionMap().put(this.oldKey, this.oldAction);
      }
   }

   private class ActionImpl extends AbstractAction {
      private final EntityConfigLookup configLookup;
      private final NewEntityMenuFactory menuFactory;

      public ActionImpl(EntityConfigLookup var2, NewEntityMenuFactory var3) {
         this.configLookup = var2;
         this.menuFactory = var3;
         this.putValue("AcceleratorKey", CreateNewEntityMenuShortCut.SHORTCUT);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         JPopupMenu var2 = this.createPopup();
         if (var2 != null) {
            Dimension var3 = var2.getPreferredSize();
            int var4 = (CreateNewEntityMenuShortCut.this.rootPane.getWidth() - var3.width) / 2;
            int var5 = (CreateNewEntityMenuShortCut.this.rootPane.getHeight() - var3.height) / 2;
            var2.show(CreateNewEntityMenuShortCut.this.rootPane, var4, var5);
         }
      }

      private JPopupMenu createPopup() {
         return new FileNewMenu(this.configLookup, this.menuFactory).asPopupMenu();
      }
   }
}
