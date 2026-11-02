package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.toolbar;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;

abstract class SingleOperationNodeToolbarContribution implements OperationGraphToolbarContribution {
   protected abstract void handleOperationNode(OperationWrapperNode var1);

   protected abstract void handleNoSelection();

   @Override
   public void selectionChanged(Object[] var1) {
      OperationWrapperNode var2 = getOpNode(var1);
      if (var2 != null) {
         this.handleOperationNode(var2);
      } else {
         this.handleNoSelection();
      }
   }

   private static OperationWrapperNode getOpNode(Object[] var0) {
      OperationWrapperNode var1 = null;

      for (Object var5 : var0) {
         if (var5 instanceof OperationWrapperNode) {
            if (var1 == null) {
               var1 = (OperationWrapperNode)var5;
            } else if (var1 != var5) {
               return null;
            }
         } else {
            if (var5 instanceof EmailWrapperNode) {
               return null;
            }

            if (var5 instanceof PipelineActivityNode) {
               OperationWrapperNode var6 = ((PipelineActivityNode)var5).getParent();
               if (var1 == null) {
                  var1 = var6;
               } else if (var1 != var6) {
                  return null;
               }
            }
         }
      }

      return var1;
   }
}
