package org.etlforge.integration.data.entity.sfdc;

import org.etlforge.integration.data.entity.operation.pipeline.BulkLoadActivity;
import org.etlforge.integration.data.entity.operation.pipeline.OperationTemplate;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.TargetActivity;

final class BulkQueryPipelineTemplate extends OperationTemplate {
   public static final String BULKQUERY_ROLE = "Bulk Query";
   public static final String TARGET_ROLE = "Target File";

   public BulkQueryPipelineTemplate() {
      super(BulkQueryOperationType.TYPE);
   }

   @Override
   protected PipelineActivity[] createChain() {
      BulkLoadActivity var1 = new BulkLoadActivity("Bulk Query");
      var1.setReadOnly(true);
      TargetActivity var2 = new TargetActivity("Target File");
      var2.setReadOnly(true);
      return new PipelineActivity[]{var1, var2};
   }
}
