package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.ActivityContentAcceptor;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.XsltTransformActivityNode;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.XsltTransform;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.entity.operation.pipeline.XsltTransformActivity;

public final class XsltTransformActivityTypeConfig extends EntityTypeBasedActivityTypeConfig {
   public XsltTransformActivityTypeConfig() {
      super(PipelineActivityType.XSLT_TRANSFORM, EntityType.XsltTransform);
   }

   @Override
   public PipelineActivityNode createNode(PipelineActivity var1, OperationWrapperNode var2) {
      return new XsltTransformActivityNode((XsltTransformActivity)var1, var2);
   }

   @Override
   public EntityContentProvider<XsltTransform> getContentProvider(OperationGraphController var1, OperationWrapper var2, PipelineActivity var3) {
      XsltTransformActivity var4 = (XsltTransformActivity)var3;
      ContentProviderContext var5 = ContentProviderContext.create(var2.getPipeline(), var4);
      ActivityContentAcceptor var6 = ActivityContentAcceptor.create(var1, var2, var4);
      XsltTransformContentProvider var7 = new XsltTransformContentProvider(var5, var1.getProject(), var6);
      var7.setEntityFactory(var1.getEntityFactory());
      return var7;
   }
}
