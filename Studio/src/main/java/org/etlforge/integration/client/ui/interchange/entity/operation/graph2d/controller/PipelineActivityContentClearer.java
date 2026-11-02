package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo.ActivityContentChangedEdit;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityPosition;

class PipelineActivityContentClearer {
   private final OperationGraphController controller;

   PipelineActivityContentClearer(OperationGraphController var1) {
      this.controller = var1;
   }

   void clearNodeContent(PipelineActivityNode var1) {
      if (!var1.getDataObject().isEmpty()) {
         if (this.controller.isChangeAllowed(var1)) {
            Object var2 = var1.getDataObject().getContent();
            var1.getDataObject().removeContent();
            this.controller.getGraph().fireActiveObjectsChanged();
            if (var2 instanceof IntegrationEntity) {
               this.sendUndoableEdit(var1, (IntegrationEntity)var2);
            }
         }
      }
   }

   private void sendUndoableEdit(PipelineActivityNode var1, IntegrationEntity var2) {
      OperationId var3 = var1.getParent().getOperationId();
      PipelineActivityPosition var4 = var1.getParent().getPipeline().getPosition(var1.getDataObject());
      ActivityContentChangedEdit var5 = new ActivityContentChangedEdit(this.controller, var3, var4, null, var2.getID());
      this.controller.fireGraphDataEdit(var5);
   }
}
