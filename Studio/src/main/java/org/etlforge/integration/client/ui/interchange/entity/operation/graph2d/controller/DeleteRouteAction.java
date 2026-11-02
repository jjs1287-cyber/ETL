package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.data.entity.operation.flow.Route;

class DeleteRouteAction extends AbstractAction {
   private final OperationGraphController controller;
   private final Route route;

   public DeleteRouteAction(OperationGraphController var1, Route var2) {
      super("Delete");
      this.controller = var1;
      this.route = var2;
      this.putValue("ShortDescription", "Delete this route");
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.controller.deleteRoute(this.route);
   }
}
