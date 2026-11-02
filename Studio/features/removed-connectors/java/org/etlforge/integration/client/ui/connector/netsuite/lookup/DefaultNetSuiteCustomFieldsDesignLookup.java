package org.etlforge.integration.client.ui.connector.netsuite.lookup;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.util.logging.Logger;
import org.etlforge.integration.client.connector.netsuite.serverapi.GetNetSuiteCustomFieldsCallBack;
import org.etlforge.integration.client.connector.netsuite.serverapi.NetSuiteDesignInfoProvider;
import org.etlforge.integration.client.server.ResultProducingServerCallback;
import org.etlforge.integration.client.ui.connector.netsuite.NetSuiteCustomFieldCache;
import org.etlforge.integration.client.ui.connector.netsuite.NetSuiteObjectCache;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDesignRuntimeException;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteObjectCustomFields;
import org.etlforge.integration.design.netsuite.lookup.NetSuiteCustomFieldsLookup;

public class DefaultNetSuiteCustomFieldsDesignLookup implements NetSuiteCustomFieldsDesignLookup {
   private final Logger log = Logger.getAnonymousLogger();
   private NetSuiteEndpoint endpoint;
   private NetSuiteFunction.FunctionType functionType;
   private boolean isCustomObject;
   private String customFieldKey;
   private final NetSuiteDesignInfoProvider serverApi;
   private final NetSuiteCustomFieldCache cache;

   public DefaultNetSuiteCustomFieldsDesignLookup(NetSuiteDesignInfoProvider var1, NetSuiteObjectCache var2) {
      this(var1, var2.getNetSuiteCustomFieldCache());
   }

   public DefaultNetSuiteCustomFieldsDesignLookup(NetSuiteDesignInfoProvider var1, NetSuiteCustomFieldCache var2) {
      this.serverApi = Preconditions.checkNotNull(var1);
      this.cache = Preconditions.checkNotNull(var2);
   }

   @Override
   public NetSuiteObjectCustomFields getNetSuiteObjectsCustomFields() {
      Preconditions.checkNotNull(this.endpoint);

      try {
         return this.getNetSuiteObjectsCustomFieldsImpl();
      } catch (Exception var2) {
         Throwables.propagateIfInstanceOf(var2, ConnectorNetSuiteDesignRuntimeException.class);
         throw new ConnectorNetSuiteDesignRuntimeException(var2);
      }
   }

   private synchronized NetSuiteObjectCustomFields getNetSuiteObjectsCustomFieldsImpl() throws ConnectorNetSuiteDesignRuntimeException, Exception {
      NetSuiteCustomFieldCache.NetSuiteCacheInput var1 = new NetSuiteCustomFieldCache.NetSuiteCacheInput(
         this.endpoint, this.functionType, this.isCustomObject, this.customFieldKey
      );
      Preconditions.checkNotNull(this.endpoint);
      NetSuiteObjectCustomFields var2 = this.cache.getCache(var1).orNull();
      if (var2 != null) {
         return var2;
      }

      NetSuiteObjectCustomFields var3 = this.askServer();
      this.cache.storeCache(var1, var3);
      return var3;
   }

   @Override
   public void evict(boolean var1, String var2) {
      this.customFieldKey = null;
      NetSuiteCustomFieldCache.NetSuiteCacheInput var3 = new NetSuiteCustomFieldCache.NetSuiteCacheInput(this.endpoint, this.functionType, var1, var2);
      this.cache.evictCache(var3);
   }

   @Override
   public NetSuiteCustomFieldsLookup getNetSuiteCustomFieldsLookup(boolean var1, String var2) {
      this.isCustomObject = var1;
      this.customFieldKey = var2;
      return this;
   }

   @Override
   public void setDesignInput(NetSuiteEndpoint var1, NetSuiteFunction.FunctionType var2) {
      this.endpoint = var1;
      this.functionType = var2;
   }

   private NetSuiteObjectCustomFields askServer() throws Exception, ConnectorNetSuiteDesignRuntimeException {
      if (this.isCustomObject) {
         this.log.info("askServer for NetSuiteObjectCustomFields of custom object " + this.customFieldKey);
      } else {
         this.log.info("askServer for NetSuiteObjectCustomFields with " + this.customFieldKey);
      }

      DefaultNetSuiteCustomFieldsDesignLookup.Callback var1 = new DefaultNetSuiteCustomFieldsDesignLookup.Callback();
      this.serverApi.getNetSuiteCustomFieldsNew(this.endpoint, this.isCustomObject, this.customFieldKey, var1);
      NetSuiteObjectCustomFields var2 = var1.awaitResult().orNull();
      if (var2 == null) {
         throw new ConnectorNetSuiteDesignRuntimeException("The request was canceled.");
      } else {
         return var2;
      }
   }

   private static class Callback extends ResultProducingServerCallback<NetSuiteObjectCustomFields> implements GetNetSuiteCustomFieldsCallBack {
      private Callback() {
      }

      @Override
      public void gotNetSuiteCustomFields(NetSuiteObjectCustomFields var1) {
         this.set(var1);
      }
   }
}
