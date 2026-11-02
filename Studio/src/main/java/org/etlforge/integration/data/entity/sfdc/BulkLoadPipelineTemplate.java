package org.etlforge.integration.data.entity.sfdc;

import org.etlforge.integration.data.entity.operation.pipeline.BulkLoadActivity;
import org.etlforge.integration.data.entity.operation.pipeline.OperationTemplate;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.SourceActivity;

final class BulkLoadPipelineTemplate extends OperationTemplate {
   public static final String SOURCE_ROLE = "Source File";
   public static final String BULKLOAD_ROLE = "Bulk Load";

   public BulkLoadPipelineTemplate() {
      super(BulkLoadOperationType.TYPE);
   }

   @Override
   protected PipelineActivity[] createChain() {
      SourceActivity var1 = new SourceActivity("Source File");
      var1.setReadOnly(true);
      BulkLoadActivity var2 = new BulkLoadActivity("Bulk Load");
      var2.setReadOnly(true);
      return new PipelineActivity[]{var1, var2};
   }
}
