package org.etlforge.integration.salesforce.bulkload.backend;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.id.SalesforceBulkLoadId;
import org.etlforge.lang.Objects;
import org.etlforge.system.ArgCheck;

public final class BulkLoadInstanceId {
   private final SalesforceBulkLoadId bulkloadGuid;
   private final long operationInstanceId;

   public BulkLoadInstanceId(SalesforceBulkLoadId var1, long var2) {
      this.bulkloadGuid = Preconditions.checkNotNull(var1);
      this.operationInstanceId = ArgCheck.checkPositive(var2);
   }

   public SalesforceBulkLoadId getBulkloadGuid() {
      return this.bulkloadGuid;
   }

   public long getOperationInstanceId() {
      return this.operationInstanceId;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.bulkloadGuid, this.operationInstanceId);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof BulkLoadInstanceId)) {
         return false;
      }

      BulkLoadInstanceId var2 = (BulkLoadInstanceId)var1;
      return this.bulkloadGuid.equals(var2.bulkloadGuid) && this.operationInstanceId == var2.operationInstanceId;
   }

   @Override
   public String toString() {
      return String.format("BulkLoad GUID = %s, Operation instance ID: %d", this.bulkloadGuid.toString(), this.operationInstanceId);
   }
}
