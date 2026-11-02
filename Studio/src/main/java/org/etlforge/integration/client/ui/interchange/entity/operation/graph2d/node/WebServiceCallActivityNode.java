package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.operation.pipeline.WebServiceCallActivity;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;

public final class WebServiceCallActivityNode extends EntityActivityNode<WebServiceCall> {
   public WebServiceCallActivityNode(WebServiceCallActivity var1, OperationWrapperNode var2) {
      super(var1, var2);
   }

   public WebServiceCallActivity getDataObject() {
      return (WebServiceCallActivity)super.getDataObject();
   }

   @Override
   public GraphNodeUi2D createUi(Graph2D var1) {
      return new WebServiceCallNodeUi2D(var1, this);
   }
}
