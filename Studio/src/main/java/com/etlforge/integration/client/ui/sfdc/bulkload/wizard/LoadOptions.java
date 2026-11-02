package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.etlforge.integration.client.ui.sfdc.wizard.prefs.DefaultDateConversionParams;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.salesforce.DateConversionParams;
import org.etlforge.integration.salesforce.bulkload.BatchFileParams;
import org.etlforge.integration.salesforce.bulkload.FileSplitterParams;

final class LoadOptions implements BulkOptions {
   private final BatchFileParams batchParams;
   private final Optional<DateConversionParams> dateParams;
   private final boolean compress;
   private final boolean serialMode;
   private final String assignmentRuleId;
   private final Schedule schedule;
   private final boolean utf8;

   public static LoadOptions defaultOptions(Schedule var0, boolean var1) {
      return new LoadOptions(BatchFileParams.DEFAULT, DefaultDateConversionParams.defaultParams(), true, false, null, var0, var1);
   }

   public LoadOptions(BatchFileParams var1, Optional<DateConversionParams> var2, boolean var3, boolean var4, String var5, Schedule var6, boolean var7) {
      this.batchParams = Preconditions.checkNotNull(var1);
      this.dateParams = Preconditions.checkNotNull(var2);
      this.compress = var3;
      this.serialMode = var4;
      this.assignmentRuleId = var5;
      this.schedule = var6;
      this.utf8 = var7;
   }

   public FileSplitterParams applyTo(FileSplitterParams var1) {
      return new FileSplitterParams(var1.mappings, this.batchParams, this.dateParams);
   }

   @Override
   public void applyTo(SalesforceBulkLoad var1) {
      synchronized (var1.getDataLock()) {
         var1.setCompressionEnabled(this.compress);
         var1.setSerialMode(this.serialMode);
         String var3 = this.utf8 ? Charsets.UTF_8.name() : "";
         var1.setFileEncodingName(var3);
         var1.setAssignmentRuleId(this.assignmentRuleId);
      }
   }

   @Override
   public void applyTo(Operation var1) {
      synchronized (var1.getDataLock()) {
         var1.setSchedule(this.schedule);
      }
   }
}
