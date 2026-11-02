package org.etlforge.integration.client.ui.interchange.entity.transformation.page;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.integration.client.ui.interchange.component.menu.InterchangeMenuDictionary;
import org.etlforge.integration.client.ui.interchange.entity.transformation.menu.TransformationShowDataMenu;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

final class TestActionsShortcut {
   private final TransformationPage page;

   public TestActionsShortcut(TransformationPage var1) {
      this.page = var1;
   }

   public void install() {
      this.page.addPostPageLoadingJob(new Runnable() {
         @Override
         public void run() {
            TestActionsShortcut.this.installImpl();
         }
      });
   }

   private void installImpl() {
      TreeMapper var1 = this.page.getTreeMapper();
      var1.registerKeyboardShortCut(KeyStroke.getKeyStroke("ctrl alt pressed T"), "test", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            TestActionsShortcut.this.showPopup();
         }
      });
   }

   private void showPopup() {
      JPopupMenu var1 = this.createPopup();
      TreeMapper var2 = this.page.getTreeMapper();
      var2.showPopupMenu(var1);
   }

   private JPopupMenu createPopup() {
      KongaPopupMenu var1 = new KongaPopupMenu();
      this.addTestActions(var1);
      var1.addSeparator();
      this.addShowDataNodes(var1);
      return var1;
   }

   private void addTestActions(KongaPopupMenu var1) {
      for (String var5 : new String[]{"action.ic.tx.LoadSource", "action.ic.tx.TestTransformation", "action.ic.tx.TestOperation"}) {
         ApplicationAction var6 = this.page.getPageAction(var5);
         if (var6 != null) {
            var1.add(var6);
         }
      }
   }

   private void addShowDataNodes(KongaPopupMenu var1) {
      TransformationShowDataMenu var2 = new TransformationShowDataMenu(this.page);
      var1.add(var2.createJMenu(InterchangeMenuDictionary.DICTIONARY));
   }
}
