package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import java.util.concurrent.Callable;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionQueryBuilderLookup;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteDefinitionQueryBuilder;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObjectCategory;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteCustomSearch;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteCustomSearchDefinition;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.wait.WaitService;

public class AdvancedSearchModelPreparer {
   public static void prepareModel(
      AdvancedSearchPropertyModel var0, NetSuiteEndpoint var1, NetSuiteConnectorObject var2, NetSuiteDefinitionQueryBuilderLookup var3, WaitService var4
   ) {
      AdvancedSearchModelPreparer.ObjectProducer var5 = new AdvancedSearchModelPreparer.ObjectProducer(var1, var2, var3);
      AdvancedSearchModelPreparer.ObjectConsumer var6 = new AdvancedSearchModelPreparer.ObjectConsumer(var0);
      Computation.create(var5, var6)
         .swingWorkerBuilder()
         .withErrorLog(ErrorLevel.ATTENTION, AdvancedSearchModelPreparer.class)
         .withWaitMessage(var4, "Looking up search definitions")
         .buildAndExecute();
   }

   private AdvancedSearchModelPreparer() {
   }

   private static class ObjectConsumer implements Receiver<AdvancedSearchOption> {
      private final AdvancedSearchPropertyModel model;

      public ObjectConsumer(AdvancedSearchPropertyModel var1) {
         this.model = var1;
      }

      public void handle(AdvancedSearchOption var1) {
         this.model.setSearchOption(var1.selectedObject, var1.option);
         this.model.setSubtypes(var1.subTypes);
      }
   }

   private static class ObjectProducer implements Callable<AdvancedSearchOption> {
      private final NetSuiteEndpoint endpoint;
      private final NetSuiteConnectorObject netsuiteObject;
      private final NetSuiteDefinitionQueryBuilderLookup searchDefLookup;

      public ObjectProducer(NetSuiteEndpoint var1, NetSuiteConnectorObject var2, NetSuiteDefinitionQueryBuilderLookup var3) {
         this.endpoint = var1;
         this.netsuiteObject = var2;
         this.searchDefLookup = var3;
      }

      public AdvancedSearchOption call() throws Exception {
         NetSuiteDefinitionQueryBuilder var1 = this.searchDefLookup.lookupDefinition(this.endpoint);
         String var2 = this.netsuiteObject.getName();
         if (this.netsuiteObject.getCategory() == NetSuiteConnectorObjectCategory.CustomObject) {
            return new AdvancedSearchOption(var2, AdvancedSearchOption.OptionType.CustomObjectSearch);
         }

         if (this.netsuiteObject.getCategory() != NetSuiteConnectorObjectCategory.ItemSearchObject
            && this.netsuiteObject.getCategory() != NetSuiteConnectorObjectCategory.TranSearchObject) {
            return new AdvancedSearchOption(var2, AdvancedSearchOption.OptionType.All);
         }

         NetSuiteCustomSearchDefinition var3 = var1.getNetSuiteCustomSearchDefinition();
         if (var3.isGenericCustomSearch(this.netsuiteObject.getKey())) {
            NetSuiteCustomSearch var4 = var3.getNetSuiteCustomSearch(this.netsuiteObject);
            AdvancedSearchOption var5 = new AdvancedSearchOption(var2, AdvancedSearchOption.OptionType.All);
            if (var4 == null) {
               throw new RuntimeException("No custom search defition for " + this.netsuiteObject.getKey());
            }

            var5.setSubtypes(var4.getSubTypeNames());
            return var5;
         } else {
            return new AdvancedSearchOption(var2, AdvancedSearchOption.OptionType.SearchBasicOnly);
         }
      }
   }
}
