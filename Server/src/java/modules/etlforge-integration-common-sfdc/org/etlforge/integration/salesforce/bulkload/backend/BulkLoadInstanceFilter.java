package org.etlforge.integration.salesforce.bulkload.backend;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.id.SalesforceBulkLoadId;

public final class BulkLoadInstanceFilter {
   private final SalesforceBulkLoadId bulkloadGuid;
   private final Integer maxRecordsToGet;
   private final TimeWindowFilter timeWindowFilter;

   public BulkLoadInstanceFilter(SalesforceBulkLoadId var1, Integer var2, TimeWindowFilter var3) {
      this.bulkloadGuid = Preconditions.checkNotNull(var1);
      this.timeWindowFilter = Preconditions.checkNotNull(var3);
      this.maxRecordsToGet = var2;
   }

   public SalesforceBulkLoadId getBulkloadGuid() {
      return this.bulkloadGuid;
   }

   public Integer getMaxRecordsToGet() {
      return this.maxRecordsToGet;
   }

   public TimeWindowFilter getTimeWindowFilter() {
      return this.timeWindowFilter;
   }
}
