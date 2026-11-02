package org.etlforge.integration.data.location.driver;

import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.driver.JdbcDriverDesriptorBuilder;
import org.etlforge.integration.database.driver.OdbcDriverDescriptorFactory;

public final class DriverDescriptorFactory {
   public static DriverDescriptor createDriver(DatabaseLocation var0) {
      String var1 = var0.getDriverName();
      if (StringUtils.isEmpty(var1)) {
         return null;
      }

      String var2 = var0.getProperty("driver_begin_quote");
      String var3 = var0.getProperty("driver_end_quote");
      BeginEndQuote var4 = var2 != null && var3 != null ? new BeginEndQuote(var2, var3) : null;
      String var5 = var0.getProperty("driver_class");
      DriverDescriptor var6 = null;
      if (StringUtils.isEmpty(var5)) {
         var6 = OdbcDriverDescriptorFactory.createDescriptor(var1, var4);
      } else {
         String var7 = var0.getProperty("jdbc_subprotocol");
         var6 = new JdbcDriverDesriptorBuilder().name(var1).subprotocol(var7).driverClass(var5).quotes(var4).build();
      }

      return var6;
   }

   private DriverDescriptorFactory() {
   }
}
