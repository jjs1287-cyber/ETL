package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import org.etlforge.integration.data.entity.XsltTransform;
import org.etlforge.integration.data.entity.operation.pipeline.XsltTransformActivity;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;

public final class XsltTransformActivityNode extends EntityActivityNode<XsltTransform> {
   public XsltTransformActivityNode(XsltTransformActivity var1, OperationWrapperNode var2) {
      super(var1, var2);
   }

   public XsltTransformActivity getDataObject() {
      return (XsltTransformActivity)super.getDataObject();
   }

   @Override
   public GraphNodeUi2D createUi(Graph2D var1) {
      return new XsltTransformNodeUi2D(var1, this);
   }
}
