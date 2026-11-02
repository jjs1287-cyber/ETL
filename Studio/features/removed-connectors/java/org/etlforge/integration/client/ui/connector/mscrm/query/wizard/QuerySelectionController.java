package org.etlforge.integration.client.ui.connector.mscrm.query.wizard;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.client.connector.mscrm.serverapi.GetSavedQueriesCallback;
import org.etlforge.integration.client.connector.mscrm.serverapi.MSCrmDesignInfoProvider;
import org.etlforge.integration.client.server.ResultProducingServerCallback;
import org.etlforge.integration.data.connector.FetchXmlValidator;
import org.etlforge.integration.data.connector.MSCrmEndpoint;
import org.etlforge.integration.mscrm.query.FetchXml;
import org.etlforge.system.callback.Receiver;
import org.etlforge.system.thread.PendingFutures;
import org.etlforge.ui.wait.WaitService;

final class QuerySelectionController {
   private final QuerySelectionModel model;
   private final MSCrmDesignInfoProvider infoProvider;
   private final PendingFutures runningJobs = new PendingFutures();
   private WaitService waitService = WaitService.NONE;

   public QuerySelectionController(QuerySelectionModel var1, MSCrmDesignInfoProvider var2) {
      this.model = Preconditions.checkNotNull(var1, "model");
      this.infoProvider = Preconditions.checkNotNull(var2, "infoProvider");
   }

   public final void cancelRunningJobs() {
      this.runningJobs.cancelAll();
   }

   public final void setWaitService(WaitService var1) {
      this.waitService = Preconditions.checkNotNull(var1, "ws");
   }

   public void getQueryList() {
      QuerySelectionController.Job var1 = new QuerySelectionController.Job(this.model.getEndpoint(), this.model.getObjectName(), this.infoProvider);
      QuerySelectionController.QueryListConsumer var2 = new QuerySelectionController.QueryListConsumer();
      Future var3 = Computation.create(var1, var2)
         .swingWorkerBuilder()
         .withErrorHandler(new LoggingExceptionHandler(this.getClass(), ErrorLevel.ATTENTION))
         .withWaitMessage(this.waitService, "Downloading list of available queries")
         .buildAndExecute();
      this.runningJobs.addJob(var3);
      var2.setJob(var3);
   }

   public void validateQueryXml(String var1) throws Exception {
      FetchXmlValidator.Result var2 = FetchXmlValidator.validate(var1);
      if (!var2.isValid()) {
         throw new Exception(var2.getError());
      }

      String var3 = var2.getObjectName();
      String var4 = this.model.getObjectName();
      if (!var3.equalsIgnoreCase(var4)) {
         throw new Exception("The " + var4 + " object was selected, but the entered fetch XML queries the " + var3 + " object.");
      }
   }

   private static final class Job implements Callable<ImmutableList<FetchXml>> {
      private final MSCrmEndpoint endpoint;
      private final String objectName;
      private final MSCrmDesignInfoProvider infoProvider;

      public Job(MSCrmEndpoint var1, String var2, MSCrmDesignInfoProvider var3) {
         this.endpoint = var1;
         this.objectName = var2;
         this.infoProvider = var3;
      }

      public ImmutableList<FetchXml> call() throws Exception {
         QuerySelectionController.Job.Callback var1 = new QuerySelectionController.Job.Callback();
         this.infoProvider.getSavedQueries(this.endpoint, this.objectName, var1);
         return Thread.currentThread().isInterrupted() ? null : var1.awaitResult().orNull();
      }

      private static class Callback extends ResultProducingServerCallback<ImmutableList<FetchXml>> implements GetSavedQueriesCallback {
         private Callback() {
         }

         @Override
         public void gotQueries(ImmutableList<FetchXml> var1) {
            this.set(var1);
         }
      }
   }

   private class QueryListConsumer implements Receiver<ImmutableList<FetchXml>> {
      private Future<?> job;
      private boolean done;

      private QueryListConsumer() {
      }

      public synchronized void setJob(Future<?> var1) {
         if (this.done) {
            QuerySelectionController.this.runningJobs.jobDone(var1);
         } else {
            this.job = var1;
         }
      }

      public void handle(ImmutableList<FetchXml> var1) {
         this.updateModel(var1);
         synchronized (this) {
            this.done = true;
            if (this.job != null) {
               QuerySelectionController.this.runningJobs.jobDone(this.job);
            }
         }
      }

      private void updateModel(ImmutableList<FetchXml> var1) {
         if (var1 != null) {
            QuerySelectionController.this.model.setAvailableQueries(var1);
            if (!var1.isEmpty()) {
               QuerySelectionController.this.model.setSelectedQuery((FetchXml)var1.get(0));
            }
         }
      }
   }
}
