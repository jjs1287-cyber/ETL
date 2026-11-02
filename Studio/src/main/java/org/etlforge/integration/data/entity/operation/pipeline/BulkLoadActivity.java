package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.link.StrongEntityLink;
import org.etlforge.util.id.KongaGuid;

public final class BulkLoadActivity extends EntityActivity<SalesforceBulkLoad> {
   public BulkLoadActivity(SalesforceBulkLoad var1, String var2) {
      super(PipelineActivityType.BULK_LOAD, SalesforceBulkLoad.class, var1, var2);
   }

   public BulkLoadActivity(KongaGuid var1, String var2) {
      super(PipelineActivityType.BULK_LOAD, SalesforceBulkLoad.class, var1, var2);
   }

   public BulkLoadActivity(String var1) {
      super(PipelineActivityType.BULK_LOAD, SalesforceBulkLoad.class, var1);
   }

   @Override
   protected EntityLink createOperationLink(Operation var1) {
      return new StrongEntityLink(this.getContent(), var1);
   }

   @Override
   public String getTypeName() {
      return EntityType.SalesforceBulkLoad.getDisplayName();
   }

   @Override
   public String getTypeNamePlural() {
      return EntityType.SalesforceBulkLoad.getPlural();
   }
}
