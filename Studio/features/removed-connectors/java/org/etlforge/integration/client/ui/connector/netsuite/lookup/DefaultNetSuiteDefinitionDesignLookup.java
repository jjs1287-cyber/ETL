package org.etlforge.integration.client.ui.connector.netsuite.lookup;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.util.logging.Logger;
import org.etlforge.integration.client.connector.netsuite.serverapi.GetNetSuiteDefinitionContentsCallback;
import org.etlforge.integration.client.connector.netsuite.serverapi.NetSuiteDesignInfoProvider;
import org.etlforge.integration.client.server.ResultProducingServerCallback;
import org.etlforge.integration.client.ui.connector.netsuite.NetSuiteDefinitionDesignCache;
import org.etlforge.integration.client.ui.connector.netsuite.NetSuiteObjectCache;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDesignRuntimeException;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;
import org.etlforge.integration.server.netsuite.factory.NetSuiteDefinitionDesignFactory;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteDefinitionContents;

public class DefaultNetSuiteDefinitionDesignLookup implements NetSuiteDefinitionDesignLookup {
   private final Logger log = Logger.getAnonymousLogger();
   private final NetSuiteDesignInfoProvider serverApi;
   private final NetSuiteDefinitionDesignCache cache;

   public DefaultNetSuiteDefinitionDesignLookup(NetSuiteDesignInfoProvider var1, NetSuiteObjectCache var2) {
      this(var1, var2.getNetSuiteDefinitionDesignFileCache());
   }

   public DefaultNetSuiteDefinitionDesignLookup(NetSuiteDesignInfoProvider var1, NetSuiteDefinitionDesignCache var2) {
      this.serverApi = Preconditions.checkNotNull(var1);
      this.cache = Preconditions.checkNotNull(var2);
   }

   @Override
   public NetSuiteDefinitionDesign lookupDefinition(NetSuiteEndpoint var1) throws ConnectorNetSuiteDesignRuntimeException {
      Preconditions.checkNotNull(var1);

      try {
         return this.lookupDefinitionImpl(var1);
      } catch (Exception var3) {
         Throwables.propagateIfInstanceOf(var3, ConnectorNetSuiteDesignRuntimeException.class);
         throw new ConnectorNetSuiteDesignRuntimeException(var3);
      }
   }

   private synchronized NetSuiteDefinitionDesign lookupDefinitionImpl(NetSuiteEndpoint var1) throws ConnectorNetSuiteDesignRuntimeException, Exception {
      NetSuiteDefinitionDesign var2 = this.cache.getNetSuiteDefinitionDesign(var1).orNull();
      if (var2 != null) {
         return var2;
      }

      NetSuiteDefinitionDesign var3 = this.askServer(var1);
      this.cache.storeNetSuiteDefinitionDesign(var1, var3);
      return var3;
   }

   private NetSuiteDefinitionDesign askServer(NetSuiteEndpoint var1) throws Exception, ConnectorNetSuiteDesignRuntimeException {
      this.log.info("askServer for NetSuiteDefinitionDesign");
      DefaultNetSuiteDefinitionDesignLookup.Callback var2 = new DefaultNetSuiteDefinitionDesignLookup.Callback();
      this.serverApi.getNetSuiteDefinitionContents(var1, var2);
      NetSuiteDefinitionContents var3 = var2.awaitResult().orNull();
      if (var3 == null) {
         throw new ConnectorNetSuiteDesignRuntimeException("The request was canceled.");
      } else {
         return NetSuiteDefinitionDesignFactory.build(var3);
      }
   }

   private static class Callback extends ResultProducingServerCallback<NetSuiteDefinitionContents> implements GetNetSuiteDefinitionContentsCallback {
      private Callback() {
      }

      @Override
      public void gotNetSuiteDefinitionContents(NetSuiteDefinitionContents var1) {
         this.set(var1);
      }
   }
}
