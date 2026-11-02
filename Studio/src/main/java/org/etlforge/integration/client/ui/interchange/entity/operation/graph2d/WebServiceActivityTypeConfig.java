package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import java.util.List;
import javax.swing.Action;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.ActivityContentAcceptor;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.WebServiceCallActivityNode;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.entity.operation.pipeline.WebServiceCallActivity;

public final class WebServiceActivityTypeConfig extends EntityTypeBasedActivityTypeConfig {
   public WebServiceActivityTypeConfig() {
      super(PipelineActivityType.WEBSERVICECALL, EntityType.WebServiceCall);
   }

   @Override
   public PipelineActivityNode createNode(PipelineActivity var1, OperationWrapperNode var2) {
      return new WebServiceCallActivityNode((WebServiceCallActivity)var1, var2);
   }

   @Override
   protected void addToContextMenu(PipelineActivityNode var1, OperationGraphController var2, List<Action> var3) {
      var3.add(var2.getAssignPluginAction());
   }

   @Override
   public EntityContentProvider<WebServiceCall> getContentProvider(OperationGraphController var1, OperationWrapper var2, PipelineActivity var3) {
      WebServiceCallActivity var4 = (WebServiceCallActivity)var3;
      ContentProviderContext var5 = ContentProviderContext.create(var2.getPipeline(), var4);
      ActivityContentAcceptor var6 = ActivityContentAcceptor.create(var1, var2, var4);
      EntityContentProvider var7 = EntityContentProvider.create(
         var5, var1.getProject(), var4.getWebServiceCallFilter(var2.getPipeline()), EntityType.WebServiceCall, WebServiceCall.class, var6
      );
      var7.setEntityFactory(var1.getEntityFactory());
      return var7;
   }
}
