package org.etlforge.integration.data.entity.sfdc;

import com.google.common.base.Optional;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.salesforce.DateConversionParams;
import org.etlforge.integration.salesforce.bulkload.TimeZoneId;

public final class DateConversionProperties {
   public static final String DATE_CONVERSION_PARAMS = "date_conversion_params";
   public static final String TIME_ZONE = "timezone";
   public static final String DATE_FORMAT = "date_fmt";
   public static final String DATE_TIME_FORMAT = "datetime_fmt";

   public static <T extends IntegrationEntity & SalesforceWizardEntity> Optional<DateConversionParams> store(T var0, Optional<DateConversionParams> var1) {
      synchronized (var0.getDataLock()) {
         Optional var2 = restore((T)var0);
         if (var1.isPresent()) {
            var0.setProperty("timezone", ((DateConversionParams)var1.get()).timeZoneId.getId());
            var0.setProperty("date_fmt", ((DateConversionParams)var1.get()).dateFormat);
            var0.setProperty("datetime_fmt", ((DateConversionParams)var1.get()).dateTimeFormat);
         } else {
            var0.setProperty("timezone", null);
            var0.setProperty("date_fmt", null);
            var0.setProperty("datetime_fmt", null);
         }

         return var2;
      }
   }

   public static <T extends IntegrationEntity & SalesforceWizardEntity> Optional<DateConversionParams> restore(T var0) {
      synchronized (var0.getDataLock()) {
         String var2 = var0.getProperty("timezone");
         if (var2 != null) {
            DateConversionParams var3 = new DateConversionParams(TimeZoneId.valueOf(var2), var0.getProperty("date_fmt"), var0.getProperty("datetime_fmt"));
            return Optional.of(var3);
         }
      }

      return Optional.absent();
   }

   private DateConversionProperties() {
   }
}
