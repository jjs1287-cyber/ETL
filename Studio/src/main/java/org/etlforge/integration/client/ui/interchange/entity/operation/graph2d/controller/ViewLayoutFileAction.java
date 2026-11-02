package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.GraphLayoutFileStore;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.GraphLayoutStore;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.OperationId;

public final class ViewLayoutFileAction extends AbstractAction {
   private final ApplicationWindow appWin;
   private final GraphLayoutStore layoutStore;

   public ViewLayoutFileAction(OperationGraphController var1) {
      super("View Layout File...");
      this.appWin = var1.getWindow();
      OperationId var2 = var1.getMainOperation().getID();
      Operation var3 = var1.getEntityLookup().getEntity(var2, Operation.class);
      this.layoutStore = new GraphLayoutFileStore(var3);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.layoutStore.view(this.appWin);
   }
}
