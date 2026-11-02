package org.etlforge.integration.client.ui.connector.netsuite.lookup;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.util.Map;
import org.etlforge.integration.client.connector.netsuite.serverapi.GetNetSuiteSearchObjectSavedSearchCallBack;
import org.etlforge.integration.client.connector.netsuite.serverapi.NetSuiteDesignInfoProvider;
import org.etlforge.integration.client.server.ResultProducingServerCallback;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchDesignException;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;

public class DefaultNetSuiteSearchObjectSavedSearchLookup implements NetSuiteSearchObjectSavedSearchLookup {
   private final NetSuiteDesignInfoProvider serverApi;

   public DefaultNetSuiteSearchObjectSavedSearchLookup(NetSuiteDesignInfoProvider var1) {
      this.serverApi = Preconditions.checkNotNull(var1);
   }

   @Override
   public Map<String, String> savedSearchLookup(NetSuiteEndpoint var1, String var2) throws NetSuiteSearchDesignException {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);

      try {
         return this.askServer(var1, var2);
      } catch (Exception var4) {
         Throwables.propagateIfInstanceOf(var4, NetSuiteSearchDesignException.class);
         throw new NetSuiteSearchDesignException(var4);
      }
   }

   private Map<String, String> askServer(NetSuiteEndpoint var1, String var2) throws Exception {
      DefaultNetSuiteSearchObjectSavedSearchLookup.Callback var3 = new DefaultNetSuiteSearchObjectSavedSearchLookup.Callback();
      this.serverApi.getNetSuiteSearchObjectSavedSearch(var1, var2, var3);
      Map var4 = var3.awaitResult().orNull();
      if (var4 == null) {
         throw new NetSuiteSearchDesignException("The request was canceled.");
      } else {
         return var4;
      }
   }

   private static class Callback extends ResultProducingServerCallback<Map<String, String>> implements GetNetSuiteSearchObjectSavedSearchCallBack {
      private Callback() {
      }

      @Override
      public void gotNetSuiteSearchObjectSavedSearch(Map<String, String> var1) {
         this.set(var1);
      }
   }
}
