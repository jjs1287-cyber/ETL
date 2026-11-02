package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo.PipelineActivityInsertedEdit;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityConnection;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

class PipelineActivityInserter {
   private final OperationGraphController controller;

   PipelineActivityInserter(OperationGraphController var1) {
      this.controller = var1;
   }

   public boolean insertNewActivity(OperationPipeline var1, ActivityConnection var2, PipelineActivity var3) {
      OperationWrapperNode var4 = this.getOperationNodeFor(var1);
      if (var4 == null) {
         return false;
      } else if (this.controller.isChangeAllowed(var4)) {
         this.insertActivityIntoPipeline(var1, var3, var2);
         this.sendEdit(var3, var4);
         return true;
      } else {
         return false;
      }
   }

   private void insertActivityIntoPipeline(OperationPipeline var1, PipelineActivity var2, ActivityConnection var3) {
      PipelineActivity var4 = var3.getFrom();
      PipelineActivity var5 = var3.getTo();
      if (var4 == null) {
         var1.insertFirst(var2);
      } else if (var5 == null) {
         var1.insertLast(var2);
      } else {
         var1.insertBetween(var2, var4, var5);
      }
   }

   private void sendEdit(PipelineActivity var1, OperationWrapperNode var2) {
      PipelineActivityInsertedEdit var3 = new PipelineActivityInsertedEdit(this.controller, var1, var2);
      this.controller.fireGraphDataEdit(var3);
   }

   private OperationWrapperNode getOperationNodeFor(OperationPipeline var1) {
      for (OperationWrapperNode var3 : this.controller.getGraph().getModel().getOperationNodes()) {
         if (var3.getPipeline() == var1) {
            return var3;
         }
      }

      ErrorLog.log(
         PipelineActivityInserter.class,
         "Could not locate the operation node for the pipeline into which a new activity is about to be inserted",
         new Exception()
      );
      return null;
   }
}
