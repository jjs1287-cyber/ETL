package org.etlforge.integration.server.implementation.webservice.activity.impl;

import java.rmi.RemoteException;
import java.util.EnumSet;
import java.util.List;
import org.etlforge.integration.activity.client.log.LogStatus;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.serverapi.history.GetOperationHistoryCallback;
import org.etlforge.integration.activity.serverapi.history.HistoryRequest;
import org.etlforge.integration.activity.serverapi.history.OperationHistoryProvider;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.server.implementation.webservice.activity.client.ActivityInfoProvider;
import org.etlforge.integration.server.implementation.webservice.activity.client.WsOperationInstanceFilter;
import org.etlforge.integration.server.implementation.webservice.activity.client.WsOperationLogInstance;

public final class OperationHistoryProviderWsImpl implements OperationHistoryProvider {
   private final CppWebServiceConfiguration config;

   public OperationHistoryProviderWsImpl(CppWebServiceConfiguration var1) {
      this.config = var1;
   }

   @Override
   public void getOperationHistory(HistoryRequest var1, GetOperationHistoryCallback var2) {
      OperationHistoryProviderWsImpl.Worker var3 = new OperationHistoryProviderWsImpl.Worker(this.config);
      var3.getOperationHistory(var1, var2);
   }

   private static class Worker extends AbstractActivityInfoProviderWsImpl {
      public Worker(CppWebServiceConfiguration var1) {
         super(var1);
      }

      public void getOperationHistory(HistoryRequest var1, GetOperationHistoryCallback var2) {
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

            List var6 = createClientSideResponse(var5, OperationLogEntry.class, new Functions.LogEntry());
            var2.historyWasRetrieved(var6);
         } catch (RemoteException var7) {
            var2.caught(this.convert(var7));
         }
      }

      private static WsOperationInstanceFilter createRequestFilter(HistoryRequest var0) {
         String[] var1 = getStartAndEndDates(var0.timeFilter);
         WsOperationInstanceFilter var2 = startFilter(var0);
         EnumSet var3 = LogStatus.completed();
         var3.remove(LogStatus.CANCELED);
         var2.setLogStatuses(translateLogStatuses(var3));
         var2.setSearchString("");
         var2.setSortType(getSortOrder(var0.sortOrder));
         var2.setStartDate(var1[0]);
         var2.setEndDate(var1[1]);
         return var2;
      }
   }
}
