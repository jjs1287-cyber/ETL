package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import java.util.Map;
import java.util.concurrent.Callable;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteSearchObjectSavedSearchLookup;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.wait.WaitService;

public class SaveSearchModelPreparer {
   public static void prepareModel(
      SaveSearchModel var0, NetSuiteEndpoint var1, NetSuiteConnectorObject var2, NetSuiteSearchObjectSavedSearchLookup var3, WaitService var4
   ) {
      var0.setObjectName(var2.getName());
      SaveSearchModelPreparer.ObjectProducer var5 = new SaveSearchModelPreparer.ObjectProducer(var1, var2, var3);
      SaveSearchModelPreparer.ObjectConsumer var6 = new SaveSearchModelPreparer.ObjectConsumer(var0);
      Computation.create(var5, var6)
         .swingWorkerBuilder()
         .withErrorLog(ErrorLevel.ATTENTION, SaveSearchModelPreparer.class)
         .withWaitMessage(var4, "Looking up saved search")
         .buildAndExecute();
   }

   private SaveSearchModelPreparer() {
   }

   private static class ObjectConsumer implements Receiver<Map<String, String>> {
      private final SaveSearchModel model;

      public ObjectConsumer(SaveSearchModel var1) {
         this.model = var1;
      }

      public void handle(Map<String, String> var1) {
         this.model.setSavedSearch(var1);
      }
   }

   private static class ObjectProducer implements Callable<Map<String, String>> {
      private final NetSuiteEndpoint endpoint;
      private final NetSuiteConnectorObject netsuiteObject;
      private final NetSuiteSearchObjectSavedSearchLookup savedSearchLookup;

      public ObjectProducer(NetSuiteEndpoint var1, NetSuiteConnectorObject var2, NetSuiteSearchObjectSavedSearchLookup var3) {
         this.endpoint = var1;
         this.netsuiteObject = var2;
         this.savedSearchLookup = var3;
      }

      public Map<String, String> call() throws Exception {
         return this.savedSearchLookup.savedSearchLookup(this.endpoint, this.netsuiteObject.getKey());
      }
   }
}
