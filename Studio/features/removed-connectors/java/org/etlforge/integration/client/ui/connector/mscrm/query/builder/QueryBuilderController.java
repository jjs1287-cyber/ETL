package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Preconditions;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.integration.mscrm.metadata.MSCrmObjectLookup;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.wait.WaitService;

public final class QueryBuilderController {
   private final MSCrmObjectLookup objectLookup;
   private WaitService waitService = WaitService.NONE;

   public QueryBuilderController(MSCrmObjectLookup var1) {
      this.objectLookup = Preconditions.checkNotNull(var1, "objectLookup");
   }

   public QueryBuilderController clone() {
      QueryBuilderController var1 = new QueryBuilderController(this.objectLookup);
      var1.setWaitService(this.waitService);
      return var1;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = Preconditions.checkNotNull(var1, "waitService");
   }

   public Future<MSCrmObject> retrieveMetaData(String var1, Receiver<MSCrmObject> var2) {
      Preconditions.checkNotNull(var1, "objectName");
      Preconditions.checkNotNull(var2, "callback");
      QueryBuilderController.MetaDataProducer var3 = new QueryBuilderController.MetaDataProducer(this.objectLookup, var1);
      return Computation.create(var3, var2)
         .swingWorkerBuilder()
         .withErrorLog(ErrorLevel.ATTENTION, this.getClass())
         .withWaitService(this.waitService)
         .withWaitMessage(this.waitService, "Retrieving object metadata")
         .buildAndExecute();
   }

   private static class MetaDataProducer implements Callable<MSCrmObject> {
      private final MSCrmObjectLookup lookup;
      private final String name;

      public MetaDataProducer(MSCrmObjectLookup var1, String var2) {
         this.lookup = var1;
         this.name = var2;
      }

      public MSCrmObject call() throws Exception {
         return this.lookup.describeObject(this.name);
      }
   }
}
