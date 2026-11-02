package org.etlforge.integration.client.ui.connector.mscrm;

import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.integration.client.connector.mscrm.serverapi.GetDetailedMetaDataCallback;
import org.etlforge.integration.client.connector.mscrm.serverapi.MSCrmDesignInfoProvider;
import org.etlforge.integration.client.server.ResultProducingServerCallback;
import org.etlforge.integration.data.connector.MSCrmEndpoint;
import org.etlforge.integration.mscrm.metadata.MSCrmMetaDataException;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.integration.mscrm.metadata.MSCrmObjectLookup;

final class ClientSideMSCrmObjectLookup implements MSCrmObjectLookup {
   private final MSCrmObjectCache cache;
   private final ClientSideMSCrmObjectLookup.ServerSideLookup serverSideLookup;
   private final MSCrmEndpoint endpoint;
   private final Map<String, MSCrmObject> lookedUpFromServer = Maps.newHashMap();

   public ClientSideMSCrmObjectLookup(MSCrmObjectCache var1, MSCrmDesignInfoProvider var2, MSCrmEndpoint var3) {
      this.cache = var1;
      this.endpoint = var3;
      this.serverSideLookup = new ClientSideMSCrmObjectLookup.ServerSideLookup(var3, var2);
   }

   @Override
   public MSCrmObject describeObject(String var1) throws MSCrmMetaDataException {
      MSCrmObject var2 = this.lookedUpFromServer.get(var1);
      if (var2 != null) {
         return var2;
      }

      var2 = this.cache.getDetailedMetaData(this.endpoint, var1).orNull();
      if (var2 != null) {
         return var2;
      }

      var2 = this.serverSideLookup.describeObject(var1);
      this.lookedUpFromServer.put(var1, var2);
      this.cache.storeDetailedMetaData(this.endpoint, var2);
      return var2;
   }

   private static class ServerSideLookup implements MSCrmObjectLookup {
      private final MSCrmEndpoint endpoint;
      private final MSCrmDesignInfoProvider infoProvider;

      public ServerSideLookup(MSCrmEndpoint var1, MSCrmDesignInfoProvider var2) {
         this.endpoint = var1;
         this.infoProvider = var2;
      }

      @Override
      public MSCrmObject describeObject(String var1) throws MSCrmMetaDataException {
         try {
            ClientSideMSCrmObjectLookup.ServerSideLookup.Callback var2 = new ClientSideMSCrmObjectLookup.ServerSideLookup.Callback();
            this.infoProvider.getDetailedMetaData(this.endpoint, var1, var2);
            MSCrmObject var3 = var2.awaitResult().orNull();
            if (var3 != null) {
               return var3;
            }
         } catch (Exception var4) {
            throw new MSCrmMetaDataException(var4);
         }

         throw new MSCrmMetaDataException("The request was canceled.");
      }

      private static class Callback extends ResultProducingServerCallback<MSCrmObject> implements GetDetailedMetaDataCallback {
         private Callback() {
         }

         @Override
         public void gotMetaData(MSCrmObject var1) {
            this.set(var1);
         }
      }
   }
}
