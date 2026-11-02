package com.etlforge.integration.client.sfdc.data;

import org.etlforge.integration.data.entity.operation.pipeline.BulkLoadActivity;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.sfdc.BulkLoadPipelineBrowser;

public final class BulkPipelineProtector extends OperationPipelineProtector {
   @Override
   protected void protectPipeline(OperationPipeline var1) {
      BulkLoadPipelineBrowser var2 = new BulkLoadPipelineBrowser(var1);
      BulkLoadActivity var3 = var2.getBulkLoadActivity();
      var3.setReadOnly(true);
   }
}
