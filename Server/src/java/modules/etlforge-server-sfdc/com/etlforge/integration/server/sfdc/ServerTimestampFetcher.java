package com.etlforge.integration.server.sfdc;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.sforce.soap.partner.GetServerTimestampResult;
import com.sforce.soap.partner.GetUserInfoResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.etlforge.integration.salesforce.wsdl.partner.wsc.TraceSettings;
import org.etlforge.integration.salesforce.wsdl.partner.wsc.WscUtils;
import org.etlforge.util.net.ProxySettings;

public final class ServerTimestampFetcher {
   private Supplier<ProxySettings> proxySettingsSupplier = Suppliers.ofInstance(ProxySettings.NONE);
   private Supplier<TraceSettings> traceSettingsSupplier = Suppliers.ofInstance(TraceSettings.noTracing());

   public void setProxySettingsSupplier(Supplier<ProxySettings> var1) {
      this.proxySettingsSupplier = Preconditions.checkNotNull(var1);
   }

   public void setTraceSettingsSupplier(Supplier<TraceSettings> var1) {
      this.traceSettingsSupplier = Preconditions.checkNotNull(var1);
   }

   public String getServerTimestamp(GetServerTimestampInput var1) throws Exception {
      TraceSettings var2 = this.traceSettingsSupplier.get();
      PartnerConnection var3 = WscUtils.createConnectionObjectForEstablishedConnection(var1.serviceEndpoint, var1.sessionId, this.proxySettingsSupplier, var2);
      GetServerTimestampResult var4 = var3.getServerTimestamp();
      Date var5 = var4.getTimestamp().getTime();
      return this.formatTimestamp(var5, var1, var3);
   }

   private String formatTimestamp(Date var1, GetServerTimestampInput var2, PartnerConnection var3) throws ConnectionException {
      TimeZone var4 = this.getTimeZone(var2, var3);
      SimpleDateFormat var5 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      var5.setTimeZone(var4);
      return var5.format(var1);
   }

   private TimeZone getTimeZone(GetServerTimestampInput var1, PartnerConnection var2) throws ConnectionException {
      String var3;
      if (var1.timeZoneId.isPresent()) {
         var3 = var1.timeZoneId.get();
      } else {
         GetUserInfoResult var4 = var2.getUserInfo();
         var3 = var4.getUserTimeZone();
      }

      return TimeZone.getTimeZone(var3);
   }
}
