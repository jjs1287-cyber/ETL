package com.etlforge.integration.client.ui.sfdc.query.wizard;

import com.google.common.base.Charsets;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.salesforce.soap.header.QueryOptions;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

final class Options {
   private final SoapHeader batchSizeHeader;
   private final Schedule schedule;
   private final boolean utf8;

   public Options(Schedule var1, boolean var2) {
      this(2000, var1, var2);
   }

   public Options(int var1, Schedule var2, boolean var3) {
      this.batchSizeHeader = new QueryOptions(var1);
      this.schedule = var2;
      this.utf8 = var3;
   }

   public SoapHeader getBatchSizeHeader() {
      return this.batchSizeHeader;
   }

   public void applyTo(Operation var1) {
      synchronized (var1.getDataLock()) {
         var1.setSchedule(this.schedule);
      }
   }

   public void applyTo(SalesforceQuery var1) {
      String var2 = this.utf8 ? Charsets.UTF_8.name() : "";
      var1.setFileEncodingName(var2);
   }
}
