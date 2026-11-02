package org.etlforge.integration.server.implementation.webservice.activity.impl;

import java.rmi.RemoteException;
import java.util.EnumSet;
import java.util.List;
import org.etlforge.integration.activity.client.log.LogStatus;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.client.queue.QueueStatus;
import org.etlforge.integration.activity.client.queue.QueueStatusFilter;
import org.etlforge.integration.activity.serverapi.queue.DeleteQueueEntriesCallback;
import org.etlforge.integration.activity.serverapi.queue.DeleteSystemQueueCallback;
import org.etlforge.integration.activity.serverapi.queue.GetOperationQueueCallback;
import org.etlforge.integration.activity.serverapi.queue.OperationQueueProvider;
import org.etlforge.integration.activity.serverapi.queue.OperationQueueRequest;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.server.implementation.webservice.activity.client.ActivityInfoProvider;
import org.etlforge.integration.server.implementation.webservice.activity.client.WsOperationInstanceFilter;
import org.etlforge.integration.server.implementation.webservice.activity.client.WsOperationLogInstance;

public final class OperationQueueProviderWsImpl implements OperationQueueProvider {
   private final CppWebServiceConfiguration config;

   public OperationQueueProviderWsImpl(CppWebServiceConfiguration var1) {
      this.config = var1;
   }

   @Override
   public void getOperationQueue(OperationQueueRequest var1, GetOperationQueueCallback var2) {
      OperationQueueProviderWsImpl.Worker var3 = new OperationQueueProviderWsImpl.Worker(this.config);
      var3.getOperationQueue(var1, var2);
   }

   @Override
   public void deleteQueueEntries(long[] var1, DeleteQueueEntriesCallback var2) {
      OperationQueueProviderWsImpl.Worker var3 = new OperationQueueProviderWsImpl.Worker(this.config);
      var3.deleteQueueEntries(var1, var2);
   }

   @Override
   public void deleteSystemQueue(DeleteSystemQueueCallback var1) {
      OperationQueueProviderWsImpl.Worker var2 = new OperationQueueProviderWsImpl.Worker(this.config);
      var2.deleteSystemQueue(var1);
   }

   private static class Worker extends AbstractActivityInfoProviderWsImpl {
      public Worker(CppWebServiceConfiguration var1) {
         super(var1);
      }

      public void getOperationQueue(OperationQueueRequest var1, GetOperationQueueCallback var2) {
         try {
            ActivityInfoProvider var3 = this.getServerProxy(var2);
            if (var3 == null) {
               return;
            }

            WsOperationInstanceFilter var4 = createRequestFilter(var1);
            if (Thread.currentThread().isInterrupted()) {
               var2.cancelled();
               return;
            }

            WsOperationLogInstance[] var5 = var3.getOperationLog(this.userName, this.password, var4);
            if (Thread.currentThread().isInterrupted()) {
               var2.cancelled();
               return;
            }

            List var6 = createClientSideResponse(var5, OperationLogEntry.class, new Functions.QueueEntry());
            var2.queueWasRetrieved(var6);
         } catch (RemoteException var7) {
            var2.caught(this.convert(var7));
         }
      }

      private static WsOperationInstanceFilter createRequestFilter(OperationQueueRequest var0) {
         WsOperationInstanceFilter var1 = startFilter(var0);
         var1.setLogStatuses(createLogStatiiToAskFor(var0));
         var1.setSearchString("");
         return var1;
      }

      private static int[] createLogStatiiToAskFor(OperationQueueRequest var0) {
         QueueStatus var2 = getQueueStatusFromRequest(var0);
         EnumSet var1;
         if (var2 == null) {
            var1 = LogStatus.stillInQueue();
         } else {
            var1 = EnumSet.of(var2.toLogStatus());
         }

         return translateLogStatuses(var1);
      }

      private static QueueStatus getQueueStatusFromRequest(OperationQueueRequest var0) {
         QueueStatusFilter var1 = var0.statusFilter;
         return var1 == null ? null : var1.getQueueStatus();
      }

      public void deleteSystemQueue(DeleteSystemQueueCallback var1) {
         try {
            ActivityInfoProvider var2 = this.getServerProxy(var1);
            if (var2 == null) {
               return;
            }

            var2.cancelOperations(this.userName, this.password, new long[]{-1L});
            var1.systemQueueWasDeleted();
         } catch (RemoteException var3) {
            var1.caught(this.convert(var3));
         }
      }

      public void deleteQueueEntries(long[] var1, DeleteQueueEntriesCallback var2) {
         try {
            ActivityInfoProvider var3 = this.getServerProxy(var2);
            if (var3 == null) {
               return;
            }

            long[] var4 = var3.cancelOperations(this.userName, this.password, var1);
            var2.entriesWereDeleted(var4);
         } catch (RemoteException var5) {
            var2.caught(this.convert(var5));
         }
      }
   }
}
