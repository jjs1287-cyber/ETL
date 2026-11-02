package org.etlforge.integration.server.implementation.webservice.interchange.checkout.impl;

import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.ScheduleId;
import org.etlforge.integration.server.implementation.webservice.impl.common.property_keys.ClientServerDeployKeyMappings;
import org.etlforge.integration.server.implementation.webservice.impl.common.property_keys.DeployKeyMappings;
import org.etlforge.integration.server.implementation.webservice.impl.common.property_keys.ServerPropertyKey;

final class OperationFactory {
   private static final OperationFactory INSTANCE = new OperationFactory();

   private OperationFactory() {
   }

   public static OperationFactory getFactory() {
      return INSTANCE;
   }

   public Operation getInstance(String var1, String var2) {
      return new Operation(new OperationId(var2), var1);
   }

   public Operation getInstance(String var1, IntegrationEntityId var2) {
      return new Operation((OperationId)var2, var1);
   }

   public IntegrationEntity getInstance(StringKeyValuePairs var1) {
      OperationId var2 = getOperationId(var1.get("operation_id"));
      String var3 = var1.get("operation_name");
      String var4 = var1.get("description");
      Operation var5 = this.getInstance(var3, var2);
      var5.setDescription(var4);
      var5.setPropertyValues(var1);
      return var5;
   }

   public Operation getInstance(String var1) {
      return new Operation(null, var1);
   }

   public static Operation getOperation(StringKeyValuePairs var0) {
      return INSTANCE.getOperationImpl(var0);
   }

   private static OperationId getOperationId(String var0) {
      return (OperationId)EntityType.Operation.getIntegrationId(var0);
   }

   private Operation getOperationImpl(StringKeyValuePairs var1) {
      DeployKeyMappings var2 = ClientServerDeployKeyMappings.getClientServerKeyMappings(EntityType.Operation);
      String var3 = var2.get("operation_id").getServerKeyName();
      OperationId var4 = getOperationId(var1.get(var3));
      var3 = var2.get("operation_name").getServerKeyName();
      String var5 = var1.get(var3);
      Operation var6 = new Operation(var4, var5);
      var3 = var2.get("schedule_id").getServerKeyName();
      ScheduleId var7 = ScheduleFactory.getScheduleId(var1.get(var3));
      if (var7 != null) {
         var6.setScheduleId(var7);
      }

      DeployKeyMappings var8 = ClientServerDeployKeyMappings.getServerClientKeyMappings(EntityType.Operation);

      for (String var10 : var1.getKeys()) {
         ServerPropertyKey var11 = var8.get(var10);
         if (var11 != null) {
            String var12 = var11.getClientKeyName();
            var6.setStringProperty(var12, var1.get(var10));
         }
      }

      return var6;
   }
}
