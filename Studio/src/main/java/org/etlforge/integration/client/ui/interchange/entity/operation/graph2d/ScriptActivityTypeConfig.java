package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.ActivityContentAcceptor;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.ScriptActivityNode;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.entity.operation.pipeline.ScriptActivity;

public final class ScriptActivityTypeConfig extends EntityTypeBasedActivityTypeConfig {
   public ScriptActivityTypeConfig() {
      super(PipelineActivityType.SCRIPT, EntityType.Script);
   }

   @Override
   public PipelineActivityNode createNode(PipelineActivity var1, OperationWrapperNode var2) {
      return new ScriptActivityNode((ScriptActivity)var1, var2);
   }

   @Override
   public EntityContentProvider<Script> getContentProvider(OperationGraphController var1, OperationWrapper var2, PipelineActivity var3) {
      ScriptActivity var4 = (ScriptActivity)var3;
      ContentProviderContext var5 = ContentProviderContext.create(var2.getPipeline(), var4);
      ActivityContentAcceptor var6 = ActivityContentAcceptor.create(var1, var2, var4);
      ScriptContentProvider var7 = new ScriptContentProvider(var5, var1.getProject(), var6);
      var7.setEntityFactory(var1.getEntityFactory());
      return var7;
   }
}
