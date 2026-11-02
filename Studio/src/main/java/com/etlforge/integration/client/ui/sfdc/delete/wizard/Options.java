package com.etlforge.integration.client.ui.sfdc.delete.wizard;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.etlforge.integration.data.entity.SalesforceDelete;
import java.util.Collections;
import java.util.List;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

final class Options {
   private final ImmutableList<SoapHeader> soapHeaders;
   private final Schedule schedule;
   private final boolean useBuiltInResponse;
   private final boolean utf8;

   public static Options defaultOptions(Schedule var0, boolean var1, boolean var2) {
      return new Options(Collections.EMPTY_LIST, var0, var1, var2);
   }

   public Options(List<SoapHeader> var1, Schedule var2, boolean var3) {
      this(var1, var2, var3, false);
   }

   private Options(List<SoapHeader> var1, Schedule var2, boolean var3, boolean var4) {
      this.soapHeaders = ImmutableList.copyOf(var1);
      this.schedule = var2;
      this.useBuiltInResponse = var3;
      this.utf8 = var4;
   }

   public void applyTo(Operation var1) {
      synchronized (var1.getDataLock()) {
         var1.setSchedule(this.schedule);
      }
   }

   public void applyTo(SalesforceDelete var1) {
      String var2 = this.utf8 ? Charsets.UTF_8.name() : "";
      var1.setFileEncodingName(var2);
   }

   public SoapHeader[] getSoapHeaders() {
      return this.soapHeaders.toArray(new SoapHeader[this.soapHeaders.size()]);
   }

   public boolean useBuiltInResponse() {
      return this.useBuiltInResponse;
   }
}
