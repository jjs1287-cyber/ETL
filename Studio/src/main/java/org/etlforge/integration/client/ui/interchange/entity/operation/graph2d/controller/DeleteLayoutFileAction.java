package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.GraphLayoutFileStore;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.GraphLayoutStore;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.OperationId;

public final class DeleteLayoutFileAction extends AbstractAction {
   private final GraphLayoutStore layoutStore;

   public DeleteLayoutFileAction(OperationGraphController var1) {
      super("Delete Layout File...", CommonIcons.DELETE_16);
      OperationId var2 = var1.getMainOperation().getID();
      Operation var3 = var1.getEntityLookup().getEntity(var2, Operation.class);
      this.layoutStore = new GraphLayoutFileStore(var3);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.layoutStore.delete();
   }
}
