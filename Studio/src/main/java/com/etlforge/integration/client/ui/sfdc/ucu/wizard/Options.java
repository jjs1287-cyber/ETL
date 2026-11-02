package com.etlforge.integration.client.ui.sfdc.ucu.wizard;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.etlforge.integration.client.ui.sfdc.wizard.prefs.DefaultDateConversionParams;
import java.util.Collections;
import java.util.List;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.sfdc.AbstractSalesforceUCU;
import org.etlforge.integration.salesforce.DateConversionParams;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

final class Options {
   private final Optional<DateConversionParams> dateParams;
   private final boolean insertNulls;
   private final boolean compress;
   private final ImmutableList<SoapHeader> soapHeaders;
   private final Schedule schedule;
   private final boolean utf8;
   private final boolean useBuiltInResponse;

   public static Options defaultOptions(SfdcMethod var0, Schedule var1, boolean var2, boolean var3) {
      return new Options(DefaultDateConversionParams.defaultParams(), false, true, Collections.EMPTY_LIST, var1, var2, var3);
   }

   public Options(Optional<DateConversionParams> var1, boolean var2, boolean var3, List<SoapHeader> var4, Schedule var5, boolean var6) {
      this(var1, var2, var3, var4, var5, var6, false);
   }

   private Options(Optional<DateConversionParams> var1, boolean var2, boolean var3, List<SoapHeader> var4, Schedule var5, boolean var6, boolean var7) {
      this.dateParams = Preconditions.checkNotNull(var1);
      this.insertNulls = var2;
      this.compress = var3;
      this.soapHeaders = ImmutableList.copyOf(var4);
      this.schedule = var5;
      this.useBuiltInResponse = var6;
      this.utf8 = var7;
   }

   public void applyTo(AbstractSalesforceUCU var1) {
      synchronized (var1.getDataLock()) {
         var1.setDateConversionParams(this.dateParams);
         var1.setInsertNullValues(this.insertNulls);
         var1.setCompressionEnabled(this.compress);
         String var3 = this.utf8 ? Charsets.UTF_8.name() : "";
         var1.setFileEncodingName(var3);
      }
   }

   public void applyTo(Operation var1) {
      synchronized (var1.getDataLock()) {
         var1.setSchedule(this.schedule);
      }
   }

   public SoapHeader[] getSoapHeaders() {
      return this.soapHeaders.toArray(new SoapHeader[this.soapHeaders.size()]);
   }

   public boolean useBuiltInResponse() {
      return this.useBuiltInResponse;
   }
}
