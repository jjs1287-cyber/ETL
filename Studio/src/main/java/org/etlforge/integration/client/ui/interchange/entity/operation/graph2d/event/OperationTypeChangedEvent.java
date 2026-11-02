package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.event;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.ui.graph.model.GraphModel;
import org.etlforge.ui.graph.model.GraphModelEvent;
import org.etlforge.ui.graph.node.AbstractGraphNode;

public class OperationTypeChangedEvent extends GraphModelEvent {
   private final OperationWrapperNode node;
   private final ImmutableList<? extends AbstractGraphNode> oldChildren;

   public OperationTypeChangedEvent(GraphModel var1, OperationWrapperNode var2, List<? extends AbstractGraphNode> var3) {
      super(var1);
      this.node = var2;
      this.oldChildren = ImmutableList.copyOf(var3);
   }

   public final OperationWrapperNode getNode() {
      return this.node;
   }

   public final ImmutableList<? extends AbstractGraphNode> getOldChildren() {
      return this.oldChildren;
   }
}
