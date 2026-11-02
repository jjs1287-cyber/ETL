package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import org.etlforge.application.DebugMode;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.ViewPreferencesAction;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.prefs.OperationGraphPreferencesPage;
import org.etlforge.ui.widget.menu.KongaMenu;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

class CanvasContextMenuFactory {
   private final OperationGraphController controller;

   public CanvasContextMenuFactory(OperationGraphController var1) {
      this.controller = var1;
   }

   public JPopupMenu createMenu() {
      KongaPopupMenu var1 = new KongaPopupMenu();
      var1.addItems(
         new DeployAction(this.controller),
         null,
         this.controller.getCollapseAllPipelinesAction(),
         new HideEmailNodesAction(this.controller),
         new RouteFilterMenuBuilder(this.controller.getGraph()).buildMenu(),
         new ResetGraphLayoutAction(this.controller),
         null,
         this.controller.getGraphPrinter().getPrintAction(),
         this.controller.getGraphPrinter().getPageSetupAction(),
         null,
         this.createPreferencesAction()
      );
      if (DebugMode.IS_ENABLED) {
         var1.add(this.createDebugMenu());
      }

      return var1;
   }

   private JMenu createDebugMenu() {
      KongaMenu var1 = new KongaMenu("Debug");
      var1.addActions(new ViewLayoutFileAction(this.controller), new DeleteLayoutFileAction(this.controller));
      return var1;
   }

   private Action createPreferencesAction() {
      ApplicationWindow var1 = this.controller.getWindow();
      ViewPreferencesAction var2 = new ViewPreferencesAction(var1);
      var2.putValue("AcceleratorKey", null);
      var2.setInitialPath(OperationGraphPreferencesPage.PATH());
      return var2;
   }
}
