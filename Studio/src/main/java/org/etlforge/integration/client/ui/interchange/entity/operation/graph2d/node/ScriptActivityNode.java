package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.operation.pipeline.ScriptActivity;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;

public final class ScriptActivityNode extends EntityActivityNode<Script> {
   public ScriptActivityNode(ScriptActivity var1, OperationWrapperNode var2) {
      super(var1, var2);
   }

   public ScriptActivity getDataObject() {
      return (ScriptActivity)super.getDataObject();
   }

   @Override
   public GraphNodeUi2D createUi(Graph2D var1) {
      return new ScriptNodeUi2D(var1, this);
   }
}
