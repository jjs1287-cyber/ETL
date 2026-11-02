package com.etlforge.integration.server.implementation.webservice.bulkloadinfo;

import com.google.common.base.Function;
import com.etlforge.integration.server.engine.bulkload.db.BulkLoadInstanceTabQuery;
import java.util.Date;
import java.util.List;
import org.etlforge.integration.common.server.ServerDateFormat;
import org.etlforge.integration.data.entity.id.SalesforceBulkLoadId;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadInstance;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadInstanceFilter;
import org.etlforge.integration.salesforce.bulkload.backend.FileStats;
import org.etlforge.integration.salesforce.bulkload.backend.SuccessAndFailureCounts;
import org.etlforge.integration.salesforce.bulkload.backend.TimeWindowFilter;
import org.etlforge.integration.server.ExternalApiSupport;
import org.etlforge.integration.server.idmappings.GuidToIdMapper;

final class ListInstancesTask extends Task<WsBulkLoadInstance> {
   private final WsInstanceFilter filter;

   public ListInstancesTask(String var1, String var2, WsInstanceFilter var3) {
      super("listInstances", var1, var2);
      this.filter = var3;
   }

   protected WsBulkLoadInstance[] runTask(ExternalApiSupport var1) throws Exception {
      GuidToIdMapper var2 = var1.getIdMapper();
      BulkLoadInstanceTabQuery var3 = new BulkLoadInstanceTabQuery(var1.getTranDbConnection(), createInstanceFilter(this.filter), var2);
      List var4 = var3.listInstances();
      WsBulkLoadInstance[] var5 = new WsBulkLoadInstance[var4.size()];
      this.populateResponse(var5, var4, new ListInstancesTask.InstanceConverter());
      return var5;
   }

   private static BulkLoadInstanceFilter createInstanceFilter(WsInstanceFilter var0) {
      TimeWindowFilter var1 = TimeWindowFilter.NONE;
      Integer var2 = var0.getMaxRecords() > 0 ? var0.getMaxRecords() : null;
      return new BulkLoadInstanceFilter(new SalesforceBulkLoadId(var0.getBulkLoadGuid()), var2, var1);
   }

   private static class InstanceConverter implements Function<BulkLoadInstance, WsBulkLoadInstance> {
      private final ServerDateFormat dateFormat = Task.getDateFormat();

      private InstanceConverter() {
      }

      public WsBulkLoadInstance apply(BulkLoadInstance var1) {
         WsBulkLoadInstance var2 = new WsBulkLoadInstance();
         var2.setId(new WsBulkLoadInstanceId(var1.getBulkloadGuid().toString(), var1.getOperationInstanceId()));
         var2.setJobId(var1.getJobId());
         var2.setLastOperationInstanceId(var1.getLastOperationInstanceId() == null ? -1L : var1.getLastOperationInstanceId());
         var2.setStartTime(this.dateFormat.format(var1.getStartTime()));
         Date var3 = var1.getEndTime();
         if (var3 != null) {
            var2.setEndTime(this.dateFormat.format(var3));
         }

         var2.setFilePath(var1.getFilePath());
         FileStats var4 = var1.getFileStats();
         if (var4 != null) {
            var2.setFileSize(var4.fileSize);
            var2.setStartPos(var4.startPos);
         } else {
            var2.setFileSize(-1L);
            var2.setStartPos(-1L);
         }

         SuccessAndFailureCounts var5 = var1.getSuccessAndFailureCounts();
         var2.setSuccessRecords(var5.successCount);
         var2.setFailureRecords(var5.failureCount);
         return var2;
      }
   }
}
