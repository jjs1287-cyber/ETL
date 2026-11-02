package org.etlforge.integration.client.ui.connector.netsuite;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.etlforge.integration.client.ui.connector.ConnectorObjectCache;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteObjectCustomFields;
import org.etlforge.integration.etlforgeconnector.SimpleConnectorObject;

public interface NetSuiteCustomFieldCache {
   Optional<NetSuiteObjectCustomFields> getCache(NetSuiteCustomFieldCache.NetSuiteCacheInput var1);

   void storeCache(NetSuiteCustomFieldCache.NetSuiteCacheInput var1, NetSuiteObjectCustomFields var2);

   void evictCache(NetSuiteCustomFieldCache.NetSuiteCacheInput var1);

   class NetSuiteCacheInput {
      private final NetSuiteEndpoint endpoint;
      private final NetSuiteFunction.FunctionType functionType;
      private final boolean isCustomobject;
      private final String key;

      public NetSuiteCacheInput(NetSuiteEndpoint var1, NetSuiteFunction.FunctionType var2, boolean var3, String var4) {
         Preconditions.checkNotNull(var1);
         Preconditions.checkNotNull(var2);
         Preconditions.checkNotNull(var4);
         this.endpoint = var1;
         this.isCustomobject = var3;
         this.key = var4;
         this.functionType = var2;
      }

      public ConnectorObjectCache.Key getKey() {
         String var1 = this.key + this.isCustomobject;
         return new ConnectorObjectCache.Key(this.endpoint, this.functionType, new SimpleConnectorObject(var1));
      }

      public NetSuiteEndpoint getNetSuiteEndpoint() {
         return this.endpoint;
      }
   }
}
