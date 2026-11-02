package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.layout;

import java.util.HashMap;
import java.util.Map;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraph2D;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.ui.graph.model.NodeExpansionState;

public class NodeExpansionMemento {
   private final OperationGraph2D graph;
   private final Map<OperationId, NodeExpansionState> states;

   public NodeExpansionMemento(OperationGraph2D var1) {
      this.graph = var1;
      this.states = new HashMap<>();

      for (OperationWrapperNode var3 : var1.getModel().getOperationNodes()) {
         this.states.put(var3.getOperationId(), NodeExpansionState.get(var1.getLayout(), var3));
      }
   }

   public void restore() {
      for (OperationWrapperNode var2 : this.graph.getModel().getOperationNodes()) {
         NodeExpansionState var3 = this.states.get(var2.getOperationId());
         if (var3 != null) {
            var3.apply(this.graph.getLayout(), var2);
         }
      }
   }
}
