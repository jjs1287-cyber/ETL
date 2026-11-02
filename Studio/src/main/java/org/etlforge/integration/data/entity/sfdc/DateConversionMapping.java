package org.etlforge.integration.data.entity.sfdc;

import com.google.common.base.Optional;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.integration.salesforce.DateConversionParams;

public final class DateConversionMapping {
   public static final String FROM_DATA_LOADER_DATE = "GetUTCFormattedDate";
   public static final String FROM_DATA_LOADER_DATE_TIME = "GetUTCFormattedDateTime";

   public static String forDate(String var0, Optional<DateConversionParams> var1) {
      return createExpression(var0, "GetUTCFormattedDate", var1);
   }

   private static String createExpression(String var0, String var1, Optional<DateConversionParams> var2) {
      String var3;
      if (var2.isPresent()) {
         var3 = String.format(
            "%s%s(%s, \"%s\", %s)",
            ScriptModificationWarning.forMapping(),
            var1,
            var0,
            ((DateConversionParams)var2.get()).timeZoneId.getId(),
            ((DateConversionParams)var2.get()).isEuropeanFormat() ? "true" : "false"
         );
      } else {
         var3 = var0;
      }

      return ScriptConstants.wrapInTags(var3);
   }

   public static String forDateTime(String var0, Optional<DateConversionParams> var1) {
      return createExpression(var0, "GetUTCFormattedDateTime", var1);
   }

   private DateConversionMapping() {
   }
}
