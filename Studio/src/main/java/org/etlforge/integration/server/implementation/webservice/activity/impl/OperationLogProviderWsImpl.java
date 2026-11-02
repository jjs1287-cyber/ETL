package org.etlforge.integration.server.implementation.webservice.activity.impl;

import java.rmi.RemoteException;
import java.util.EnumSet;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.serverapi.log.GetOperationLogCallback;
import org.etlforge.integration.activity.serverapi.log.OperationLogProvider;
import org.etlforge.integration.activity.serverapi.log.OperationLogRequest;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.server.implementation.webservice.activity.client.ActivityInfoProvider;
import org.etlforge.integration.server.implementation.webservice.activity.client.WsOperationInstanceFilter;
import org.etlforge.integration.server.implementation.webservice.activity.client.WsOperationLogInstance;

public final class OperationLogProviderWsImpl implements OperationLogProvider {
   private final CppWebServiceConfiguration config;

   public OperationLogProviderWsImpl(CppWebServiceConfiguration var1) {
      this.config = var1;
   }

   @Override
   public void getOperationLog(OperationLogRequest var1, GetOperationLogCallback var2) {
      OperationLogProviderWsImpl.Worker var3 = new OperationLogProviderWsImpl.Worker(this.config);
      var3.getOperationLog(var1, var2);
   }

   private static class Worker extends AbstractActivityInfoProviderWsImpl {
      public Worker(CppWebServiceConfiguration var1) {
         super(var1);
      }

      public void getOperationLog(OperationLogRequest var1, GetOperationLogCallback var2) {
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

            var2.logWasRetrieved(createClientSideResponse(var5, OperationLogEntry.class, new Functions.LogEntry()));
         } catch (RemoteException var6) {
            var2.caught(this.convert(var6));
         }
      }

      private static WsOperationInstanceFilter createRequestFilter(OperationLogRequest var0) {
         String[] var1 = getStartAndEndDates(var0.timeFilter);
         String var2 = var0.messageFilter == null ? "" : var0.messageFilter.getFilterText();
         WsOperationInstanceFilter var3 = startFilter(var0);
         var3.setLogStatuses(translateLogStatuses(var0));
         var3.setSearchString(var2);
         var3.setSortType(getSortOrder(var0.sortOrder));
         var3.setStartDate(var1[0]);
         var3.setEndDate(var1[1]);
         return var3;
      }

      private static int[] translateLogStatuses(OperationLogRequest var0) {
         if (var0.statusFilter == null) {
            return null;
         }

         EnumSet var1 = var0.statusFilter.getLogStatus();
         return translateLogStatuses(var1);
      }
   }
}
