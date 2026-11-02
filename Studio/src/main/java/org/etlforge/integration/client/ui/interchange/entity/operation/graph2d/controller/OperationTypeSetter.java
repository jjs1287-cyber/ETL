package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo.OperationTypeEdit;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;

final class OperationTypeSetter implements Disposable, Receiver<OperationTypeEdit.Data> {
   private final OperationGraphController controller;
   private final OperationTypeCache activityCache;

   OperationTypeSetter(OperationGraphController var1) {
      this.controller = var1;
      this.activityCache = new OperationTypeCache();
   }

   @Override
   public void dispose() {
      this.activityCache.dispose();
   }

   public void setOperationType(OperationWrapperNode var1, OperationType var2) {
      Preconditions.checkArgument(var2.isSelectable(), var2.name() + " is not selectable and should therefore not be a choice in the UI");
      OperationType var3 = var1.getPipeline().getType();
      if (this.setOperationTypeImpl(var1, var2)) {
         OperationTypeEdit var4 = new OperationTypeEdit(this.controller, this, var1.getOperationId(), var3, var2);
         this.controller.fireGraphDataEdit(var4);
      }
   }

   public void handle(OperationTypeEdit.Data var1) {
      this.setOperationTypeImpl(var1.opNode, var1.type);
   }

   private boolean setOperationTypeImpl(OperationWrapperNode var1, OperationType var2) {
      if (var1.getPipeline().getType() != var2 && this.controller.isChangeAllowed(var1)) {
         OperationPipeline var3 = this.activityCache.getPipeline(var1, var2);
         var1.getDataObject().setPipeline(var3);
         return true;
      } else {
         return false;
      }
   }
}
