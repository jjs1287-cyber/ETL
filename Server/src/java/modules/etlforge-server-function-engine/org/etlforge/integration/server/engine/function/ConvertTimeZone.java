package org.etlforge.integration.server.engine.function;

import com.etlforge.integration.server.sfdc.dataloader.DateTransform;

public class ConvertTimeZone {
   public String Exec(ConvertTimeZoneParams var1) throws Exception {
      String var2 = var1.getFromTimeZoneId();
      String var3 = var1.getToTimeZoneId();
      boolean var4 = var1.isFromIsDst();
      if (!var4) {
         var2 = this.convertDSTTZ(var2);
         var3 = this.convertDSTTZ(var3);
      }

      DateTransform var5 = new DateTransform(var2, var3, this.getFormat(var1.isToIsEuropeanFormat()));
      return var5.convertDateTime(var1.getInputValue());
   }

   private String convertDSTTZ(String var1) {
      String var2 = var1.toUpperCase();
      String var3 = var1;
      if (var2.equals("EST") || var2.equals("EDT")) {
         var3 = "EST5EDT";
      } else if (var2.equals("CST") || var2.equals("CDT")) {
         var3 = "CST6CDT";
      } else if (var2.equals("MST") || var2.equals("MDT")) {
         var3 = "MST7MDT";
      } else if (var2.equals("PST") || var2.equals("PDT")) {
         var3 = "PST8PDT";
      }

      return var3;
   }

   private String getFormat(boolean var1) {
      String var2 = " HH:mm:ss";
      return var1 ? "dd/MM/yyyy" : "MM/dd/yyyy";
   }
}
