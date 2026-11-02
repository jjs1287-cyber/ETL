package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

public interface OperationNodeController {
   OperationNodeMenuProvider getMenuProvider();

   void expandNode(OperationWrapperNode var1);

   void collapseNode(OperationWrapperNode var1);
}
