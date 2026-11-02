package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.SourceActivityNode;
import org.etlforge.integration.data.entity.SystemSource;

public final class UseSourceFromPreviousOperationAction extends AbstractAction {
   private final OperationGraphController controller;
   private final SourceActivityNode sourceNode;

   public UseSourceFromPreviousOperationAction(OperationGraphController var1, SourceActivityNode var2) {
      super("Use Source From Previous Operation");
      this.controller = var1;
      this.sourceNode = var2;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.controller
         .setActivityNodeContent(this.sourceNode.getParent().getDataObject(), this.sourceNode.getDataObject(), SystemSource.SOURCE_FROM_PREVIOUS_OPERATION);
   }
}
