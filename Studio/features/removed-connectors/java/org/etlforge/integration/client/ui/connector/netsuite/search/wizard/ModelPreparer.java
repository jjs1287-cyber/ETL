package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import java.util.concurrent.Callable;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteCustomFieldsQueryBuilderLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionQueryBuilderLookup;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchAdvancedFactory;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchRecord;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.wait.WaitService;

final class ModelPreparer {
   public static void prepareModel(
      QueryBuilderModel var0,
      NetSuiteEndpoint var1,
      NetSuiteConnectorObject var2,
      NetSuiteDefinitionQueryBuilderLookup var3,
      NetSuiteCustomFieldsQueryBuilderLookup var4,
      WaitService var5
   ) {
      ModelPreparer.ObjectProducer var6 = new ModelPreparer.ObjectProducer(var1, var2, var3, var4);
      ModelPreparer.ObjectConsumer var7 = new ModelPreparer.ObjectConsumer(var0);
      Computation.create(var6, var7)
         .swingWorkerBuilder()
         .withErrorLog(ErrorLevel.ATTENTION, ModelPreparer.class)
         .withWaitMessage(var5, "Looking up search definitions")
         .buildAndExecute();
   }

   private ModelPreparer() {
   }

   private static class ObjectConsumer implements Receiver<NetSuiteSearchRecord> {
      private final QueryBuilderModel model;

      public ObjectConsumer(QueryBuilderModel var1) {
         this.model = var1;
      }

      public void handle(NetSuiteSearchRecord var1) {
         this.model.setSearchObject(var1);
      }
   }

   private static class ObjectProducer implements Callable<NetSuiteSearchRecord> {
      private final NetSuiteEndpoint endpoint;
      private final NetSuiteConnectorObject netsuiteObject;
      private final NetSuiteDefinitionQueryBuilderLookup searchDefLookup;
      private final NetSuiteCustomFieldsQueryBuilderLookup customFieldsLookup;

      public ObjectProducer(
         NetSuiteEndpoint var1, NetSuiteConnectorObject var2, NetSuiteDefinitionQueryBuilderLookup var3, NetSuiteCustomFieldsQueryBuilderLookup var4
      ) {
         this.endpoint = var1;
         this.netsuiteObject = var2;
         this.searchDefLookup = var3;
         this.customFieldsLookup = var4;
      }

      public NetSuiteSearchRecord call() throws Exception {
         return NetSuiteSearchAdvancedFactory.newSearchRecord(this.endpoint, this.netsuiteObject, this.searchDefLookup, this.customFieldsLookup);
      }
   }
}
