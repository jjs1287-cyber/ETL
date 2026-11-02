package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.operation.pipeline.MutablePipelineActivity;

public abstract class EntityActivityNode<E extends IntegrationEntity> extends PipelineActivityNode {
   public EntityActivityNode(MutablePipelineActivity<E> var1, OperationWrapperNode var2) {
      super(var1, var2);
   }

   @Override
   public boolean isRenamable() {
      IntegrationEntity var1 = this.getDataObject().getContent();
      return var1 != null && var1.isRenameable();
   }

   public MutablePipelineActivity<E> getDataObject() {
      return (MutablePipelineActivity<E>)super.getDataObject();
   }
}
