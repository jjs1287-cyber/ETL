package org.etlforge.integration.data.entity.sfdc;

import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.data.entity.operation.pipeline.BulkLoadActivity;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineBrowser;

public final class BulkLoadPipelineBrowser extends OperationPipelineBrowser {
   public BulkLoadPipelineBrowser(OperationPipeline var1) {
      super(var1);
   }

   public BulkLoadActivity getBulkLoadActivity() throws IllegalStateException {
      BulkLoadActivity var1 = this.getPipeline().getFirstActivityOfType(BulkLoadActivity.class);
      if (var1 == null) {
         throw new IllegalStateException("The bulk load activity is missing");
      } else {
         return var1;
      }
   }

   public void setBulkLoad(SalesforceBulkLoad var1) {
      BulkLoadActivity var2 = this.getBulkLoadActivity();
      var2.setContent(var1);
   }

   public SalesforceBulkLoad getBulkLoad() {
      BulkLoadActivity var1 = this.getBulkLoadActivity();
      return var1.getContent();
   }
}
