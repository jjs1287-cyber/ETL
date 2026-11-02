package org.etlforge.integration.server.implementation.webservice.interchange.checkout.impl;

import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.id.ScheduleId;
import org.etlforge.integration.server.implementation.webservice.impl.common.property_keys.ClientServerDeployKeyMappings;
import org.etlforge.integration.server.implementation.webservice.impl.common.property_keys.DeployKeyMappings;
import org.etlforge.integration.server.implementation.webservice.impl.common.property_keys.ServerPropertyKey;

public final class ScheduleFactory {
   private ScheduleFactory() {
   }

   static ScheduleId getScheduleId(String var0) {
      return var0 != null && !var0.isEmpty() ? (ScheduleId)EntityType.Schedule.getIntegrationId(var0) : null;
   }

   public static Schedule getSchedule(StringKeyValuePairs var0) {
      DeployKeyMappings var1 = ClientServerDeployKeyMappings.getClientServerKeyMappings(EntityType.Schedule);
      String var2 = var1.get("schedule_id").getServerKeyName();
      ScheduleId var3 = getScheduleId(var0.getString(var2));
      var2 = var1.get("schedule_name").getServerKeyName();
      String var4 = var0.getString(var2);
      Schedule var5 = new Schedule(var3, var4);
      DeployKeyMappings var6 = ClientServerDeployKeyMappings.getServerClientKeyMappings(EntityType.Schedule);

      for (String var8 : var0.getKeys()) {
         ServerPropertyKey var9 = var6.get(var8);
         if (var9 != null) {
            String var10 = var9.getClientKeyName();
            var5.setStringProperty(var10, var0.get(var8));
         }
      }

      return var5;
   }
}
