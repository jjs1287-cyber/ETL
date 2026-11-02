package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo.ActivityContentChangedEdit;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.pipeline.MutablePipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityPosition;

public class PipelineActivityContentSetter<E extends IntegrationEntity> {
   private final OperationGraphController controller;

   public PipelineActivityContentSetter(OperationGraphController var1) {
      this.controller = var1;
   }

   public boolean setContent(OperationWrapper var1, MutablePipelineActivity<E> var2, E var3) {
      if (!this.controller.isChangeAllowed(var1)) {
         return false;
      }

      IntegrationEntity var4 = (IntegrationEntity)var2.getContent();
      var2.setContent(var3);
      this.controller.getGraph().fireActiveObjectsChanged();
      if (var4 == null || !var4.getID().equals(var3.getID())) {
         this.sendUndoableEdit(var1, var2, var3, var4);
      }

      return true;
   }

   private void sendUndoableEdit(OperationWrapper var1, PipelineActivity var2, IntegrationEntity var3, IntegrationEntity var4) {
      OperationId var5 = var1.getActivity().getID();
      PipelineActivityPosition var6 = var1.getPipeline().getPosition(var2);
      ActivityContentChangedEdit var7 = new ActivityContentChangedEdit(
         this.controller, var5, var6, var3 != null ? var3.getID() : null, var4 != null ? var4.getID() : null
      );
      this.controller.fireGraphDataEdit(var7);
   }
}
