package org.etlforge.integration.data.entity.operation.flow;

public interface FlowChangeProcessor {
   void process(RouteInsertedChange var1);

   void process(RouteRemovedChange var1);

   void process(NodeInsertedChange var1);

   void process(NodeRemovedChange var1);

   void process(NewOperationPipelineChange var1);

   void process(FlowRebuiltChange var1);
}
