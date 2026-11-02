package org.etlforge.integration.server.engine.connector.netsuite;

import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.server.engine.connector.exception.ConnectorInvalidInputRuntimeException;

public class ConnectorNetSuiteOperation {
   static ConnectorNetSuiteOperation adOperation = new ConnectorNetSuiteOperation();
   Map<String, EntityType> adOperationMap = Maps.newConcurrentMap();

   public static EntityType getOperation(String var0) {
      return adOperation.newValidOperation(var0);
   }

   private void AddMap() {
      this.AddMap(EntityType.NetSuiteCreate, "Create");
      this.AddMap(EntityType.NetSuiteUpdate, "Update");
      this.AddMap(EntityType.NetSuiteUpsert, "Upsert");
      this.AddMap(EntityType.NetSuiteDelete, "Delete");
      this.AddMap(EntityType.NetSuiteQuery, "Query");
   }

   private void AddMap(EntityType var1, String var2) {
      String var3 = String.format("%d", var1.getId());
      this.adOperationMap.put(var3, var1);
      this.adOperationMap.put(var2.toUpperCase(), var1);
   }

   private EntityType newValidOperation(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         EntityType var2 = this.adOperationMap.get(var1.toUpperCase());
         if (var2 != null) {
            return var2;
         } else {
            throw new ConnectorInvalidInputRuntimeException(var1 + " operation is not supported");
         }
      } else {
         throw new ConnectorInvalidInputRuntimeException(" operation is not specified");
      }
   }

   private ConnectorNetSuiteOperation() {
      this.AddMap();
   }
}
