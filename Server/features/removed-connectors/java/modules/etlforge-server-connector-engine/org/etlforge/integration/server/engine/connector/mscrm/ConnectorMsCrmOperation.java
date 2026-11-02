package org.etlforge.integration.server.engine.connector.mscrm;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.mscrm.FunctionType;
import org.etlforge.integration.server.engine.connector.exception.ConnectorInvalidInputRuntimeException;

public final class ConnectorMsCrmOperation {
   static Map<String, FunctionType> crmOperationMap = Maps.newConcurrentMap();

   private static void AddMap(EntityType var0, FunctionType var1) {
      String var2 = String.format("%d", var0.getId());
      crmOperationMap.put(var2, var1);
      crmOperationMap.put(var1.name().toUpperCase(), var1);
   }

   FunctionType newValidFunctionType(String var1) {
      if (Strings.isNullOrEmpty(var1)) {
         throw new ConnectorInvalidInputRuntimeException(" operation is not specified");
      } else {
         FunctionType var2 = crmOperationMap.get(var1.toUpperCase());
         if (var2 != null) {
            return var2;
         } else {
            throw new ConnectorInvalidInputRuntimeException(var1 + " operation is not supported");
         }
      }
   }

   static {
      AddMap(EntityType.MSCrmCreate, FunctionType.CREATE);
      AddMap(EntityType.MSCrmUpdate, FunctionType.UPDATE);
      AddMap(EntityType.MSCrmDelete, FunctionType.DELETE);
      AddMap(EntityType.MSCrmQuery, FunctionType.RETRIEVE);
      AddMap(EntityType.MSCrmUpsert, FunctionType.UPSERT);
   }
}
