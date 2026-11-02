package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo.PipelineActivityRemovedEdit;
import org.etlforge.integration.data.entity.operation.pipeline.RemoveActivityVetoException;

class PipelineActivityRemover {
   private final OperationGraphController controller;

   public PipelineActivityRemover(OperationGraphController var1) {
      this.controller = var1;
   }

   public boolean removeActivity(PipelineActivityNode var1) {
      if (!this.controller.isChangeAllowed(var1)) {
         return false;
      }

      try {
         this.removeImpl(var1);
         return true;
      } catch (RemoveActivityVetoException var3) {
         Alert.error(var3.getMessage(), Strings.get("NotAllowed"));
         return false;
      }
   }

   private void removeImpl(PipelineActivityNode var1) throws RemoveActivityVetoException {
      OperationWrapperNode var2 = var1.getParent();
      PipelineActivityRemovedEdit var3 = new PipelineActivityRemovedEdit(this.controller, var1.getDataObject(), var2);
      var2.getPipeline().removeActivity(var1.getDataObject());
      this.controller.fireGraphDataEdit(var3);
   }
}
