package org.etlforge.integration.server.engine.connector.msgp;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.server.engine.connector.exception.ConnectorInvalidInputRuntimeException;

public final class ConnectorMsGPOperation {
   static ConnectorMsGPOperation gpOperation = new ConnectorMsGPOperation();
   Map<String, String> gpOperationMap = Maps.newConcurrentMap();

   public static String getOperation(String var0) {
      return gpOperation.newValidOperation(var0);
   }

   private void AddMap() {
      this.AddMap(EntityType.MSGPCreate, "Create");
      this.AddMap(EntityType.MSGPUpdate, "Update");
      this.AddMap(EntityType.MSGPDelete, "Delete");
   }

   private void AddMap(EntityType var1, String var2) {
      String var3 = String.format("%d", var1.getId());
      this.gpOperationMap.put(var3, var2);
      this.gpOperationMap.put(var2.toUpperCase(), var2);
   }

   private String newValidOperation(String var1) {
      if (Strings.isNullOrEmpty(var1)) {
         throw new ConnectorInvalidInputRuntimeException(" operation is not specified");
      } else {
         String var2 = this.gpOperationMap.get(var1.toUpperCase());
         if (var2 != null) {
            return var2;
         } else {
            throw new ConnectorInvalidInputRuntimeException(var1 + " operation is not supported");
         }
      }
   }

   private ConnectorMsGPOperation() {
      this.AddMap();
   }
}
