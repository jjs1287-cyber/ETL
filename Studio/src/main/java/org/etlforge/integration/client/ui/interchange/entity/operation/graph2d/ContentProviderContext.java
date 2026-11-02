package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

public final class ContentProviderContext<C> {
   private final OperationPipeline pipeline;
   private final PipelineActivity<C> activity;

   public static <C> ContentProviderContext<C> create(OperationPipeline var0, PipelineActivity<C> var1) {
      return new ContentProviderContext<>(var0, var1);
   }

   public ContentProviderContext(OperationPipeline var1, PipelineActivity<C> var2) {
      this.pipeline = var1;
      this.activity = var2;
   }

   public OperationPipeline getPipeline() {
      return this.pipeline;
   }

   public PipelineActivity<C> getActivity() {
      return this.activity;
   }
}
