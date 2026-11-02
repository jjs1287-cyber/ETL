package org.etlforge.integration.client.ui.interchange.entity.transformation.page;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import org.etlforge.integration.client.ui.interchange.entity.transformation.menu.EditTransformationMenu;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;

final class EditActionsShortcut {
   private final TransformationPage page;

   public EditActionsShortcut(TransformationPage var1) {
      this.page = var1;
   }

   public void install() {
      this.page.addPostPageLoadingJob(new Runnable() {
         @Override
         public void run() {
            EditActionsShortcut.this.installImpl();
         }
      });
   }

   private void installImpl() {
      TreeMapper var1 = this.page.getTreeMapper();
      var1.registerKeyboardShortCut(KeyStroke.getKeyStroke("ctrl alt pressed E"), "edit", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            EditActionsShortcut.this.showPopup();
         }
      });
   }

   private void showPopup() {
      JPopupMenu var1 = this.createPopup();
      TreeMapper var2 = this.page.getTreeMapper();
      var2.showPopupMenu(var1);
   }

   private JPopupMenu createPopup() {
      EditTransformationMenu var1 = new EditTransformationMenu(this.page);
      JPopupMenu var2 = new JPopupMenu();
      var1.populate(var2);
      return var2;
   }
}
