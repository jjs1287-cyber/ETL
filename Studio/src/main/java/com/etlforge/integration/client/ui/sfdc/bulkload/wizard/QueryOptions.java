package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import com.google.common.base.Charsets;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.data.entity.Schedule;

final class QueryOptions implements BulkOptions {
   private final Schedule schedule;
   private final boolean utf8;

   public QueryOptions(Schedule var1, boolean var2) {
      this.schedule = var1;
      this.utf8 = var2;
   }

   @Override
   public void applyTo(SalesforceBulkLoad var1) {
      synchronized (var1.getDataLock()) {
         String var3 = this.utf8 ? Charsets.UTF_8.name() : "";
         var1.setFileEncodingName(var3);
      }
   }

   @Override
   public void applyTo(Operation var1) {
      synchronized (var1.getDataLock()) {
         var1.setSchedule(this.schedule);
      }
   }
}
