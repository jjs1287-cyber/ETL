package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;

public final class ContentProviderGate {
   private final OperationGraphController controller;
   private final OperationWrapperNode opNode;

   public ContentProviderGate(OperationGraphController var1, OperationWrapperNode var2) {
      this.controller = var1;
      this.opNode = var2;
   }

   public boolean isOpen() {
      return this.controller.isChangeAllowed(this.opNode);
   }
}
