package com.etlforge.integration.client.ui.sfdc.wizard.prefs;

import com.google.common.base.Optional;
import org.etlforge.integration.salesforce.DateConversionParams;
import org.etlforge.integration.salesforce.bulkload.TimeZoneId;

public final class DefaultDateConversionParams {
   public static Optional<DateConversionParams> defaultParams() {
      TimeZoneId var0 = TimeZonePreference.INSTANCE.getTimeZoneId();
      boolean var1 = EuropeanDateFormatPreference.INSTANCE.get();
      DateConversionParams var2 = var1 ? DateConversionParams.europeanFormat(var0) : DateConversionParams.usFormat(var0);
      return Optional.of(var2);
   }

   private DefaultDateConversionParams() {
   }
}
