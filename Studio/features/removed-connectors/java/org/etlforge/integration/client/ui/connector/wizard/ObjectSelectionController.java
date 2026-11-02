package org.etlforge.integration.client.ui.connector.wizard;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.connector.serverapi.ListObjectsCallback;
import org.etlforge.integration.client.server.ResultProducingServerCallback;
import org.etlforge.integration.client.ui.connector.ConnectorObjectCache;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.system.callback.Receiver;
import org.etlforge.system.thread.PendingFutures;
import org.etlforge.ui.wait.WaitService;

public final class ObjectSelectionController {
   private final ObjectSelectionModel model;
   private final ConnectorCallInfoProvider infoProvider;
   private volatile ConnectorObjectCache cache = ConnectorObjectCache.EMPTY;
   private final PendingFutures runningJobs = new PendingFutures();
   private WaitService waitService = WaitService.NONE;
   private volatile String waitMessage;

   public ObjectSelectionController(ObjectSelectionModel var1, ConnectorCallInfoProvider var2) {
      this.model = Preconditions.checkNotNull(var1, "model");
      this.infoProvider = Preconditions.checkNotNull(var2, "infoProvider");
   }

   public final void setCache(ConnectorObjectCache var1) {
      this.cache = Preconditions.checkNotNull(var1, "cache");
   }

   public final void cancelRunningJobs() {
      this.runningJobs.cancelAll();
   }

   public final void setWaitService(WaitService var1) {
      this.waitService = Preconditions.checkNotNull(var1, "ws");
   }

   public final void setWaitMessage(String var1) {
      this.waitMessage = var1;
   }

   public final void refreshObjectList() {
      this.getObjectList(false);
   }

   public final void getObjectList() {
      this.getObjectList(true);
   }

   private void getObjectList(boolean var1) {
      ObjectSelectionController.Job var2 = this.createJob(var1);
      this.runComputation(var2);
   }

   private ObjectSelectionController.Job createJob(boolean var1) {
      ConnectorEndpoint var2 = this.model.getEndpoint();
      ImmutableList var3 = this.model.getSelectionPath();
      return new ObjectSelectionController.Job(var2, var3, var1);
   }

   private void runComputation(ObjectSelectionController.Job var1) {
      ObjectSelectionController.ObjectListConsumer var2 = new ObjectSelectionController.ObjectListConsumer();
      Future var3 = Computation.create(var1, var2)
         .swingWorkerBuilder()
         .withErrorHandler(new LoggingExceptionHandler(this.getClass(), ErrorLevel.ATTENTION))
         .withWaitMessage(this.waitService, this.waitMessage)
         .buildAndExecute();
      this.runningJobs.addJob(var3);
      var2.setJob(var3);
   }

   private final class Job implements Callable<List<ConnectorObject>> {
      private final ConnectorEndpoint endpoint;
      private final List<ConnectorObject> selectionPath;
      private final ConnectorObjectCache.Key cacheKey;
      private final boolean lookInCacheFirst;

      protected Job(ConnectorEndpoint var2, List<ConnectorObject> var3, boolean var4) {
         this.endpoint = var2;
         this.selectionPath = var3;
         this.cacheKey = new ConnectorObjectCache.Key(var2, var3);
         this.lookInCacheFirst = var4;
      }

      public final List<ConnectorObject> call() throws Exception {
         if (this.lookInCacheFirst) {
            List var1 = ObjectSelectionController.this.cache.getObjects(this.cacheKey);
            if (!var1.isEmpty()) {
               return var1;
            }
         }

         List var2 = this.getObjectListFromServer();
         if (var2 != null) {
            ObjectSelectionController.this.cache.storeObjects(this.cacheKey, var2);
         }

         return var2;
      }

      private List<ConnectorObject> getObjectListFromServer() throws Exception {
         ObjectSelectionController.ListObjectCallbackImpl var1 = new ObjectSelectionController.ListObjectCallbackImpl();
         ObjectSelectionController.this.infoProvider.getObjectList(this.endpoint, this.selectionPath, var1);
         return Thread.currentThread().isInterrupted() ? null : var1.awaitResult().orNull();
      }
   }

   private static class ListObjectCallbackImpl extends ResultProducingServerCallback<List<ConnectorObject>> implements ListObjectsCallback {
      private ListObjectCallbackImpl() {
      }

      @Override
      public void handleObjectList(Iterable<? extends ConnectorObject> var1) {
         ArrayList var2 = Lists.newArrayList(var1);
         this.set(var2);
      }
   }

   private class ObjectListConsumer implements Receiver<List<ConnectorObject>> {
      private Future<?> job;
      private boolean done;

      private ObjectListConsumer() {
      }

      public synchronized void setJob(Future<?> var1) {
         if (this.done) {
            ObjectSelectionController.this.runningJobs.jobDone(var1);
         } else {
            this.job = var1;
         }
      }

      public void handle(List<ConnectorObject> var1) {
         if (var1 != null) {
            ObjectSelectionController.this.model.setObjects(var1);
         }

         synchronized (this) {
            this.done = true;
            if (this.job != null) {
               ObjectSelectionController.this.runningJobs.jobDone(this.job);
            }
         }
      }
   }
}
