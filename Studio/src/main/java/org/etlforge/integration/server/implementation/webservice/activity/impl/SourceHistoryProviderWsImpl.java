package org.etlforge.integration.server.implementation.webservice.activity.impl;

import java.rmi.RemoteException;
import java.util.List;
import org.etlforge.integration.activity.client.history.SourceHistoryEntry;
import org.etlforge.integration.activity.serverapi.history.DeleteSourceHistoryCallback;
import org.etlforge.integration.activity.serverapi.history.GetSourceHistoryCallback;
import org.etlforge.integration.activity.serverapi.history.HistoryRequest;
import org.etlforge.integration.activity.serverapi.history.SourceHistoryProvider;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.server.implementation.webservice.activity.client.ActivityInfoProvider;
import org.etlforge.integration.server.implementation.webservice.activity.client.WsOperationInstanceFilter;

public final class SourceHistoryProviderWsImpl implements SourceHistoryProvider {
   private final CppWebServiceConfiguration config;

   public SourceHistoryProviderWsImpl(CppWebServiceConfiguration var1) {
      this.config = var1;
   }

   @Override
   public void getSourceHistory(HistoryRequest var1, GetSourceHistoryCallback var2) {
      SourceHistoryProviderWsImpl.Worker var3 = new SourceHistoryProviderWsImpl.Worker(this.config);
      var3.getSourceHistory(var1, var2);
   }

   @Override
   public void deleteSourceHistoryEntries(long[] var1, DeleteSourceHistoryCallback var2) {
      SourceHistoryProviderWsImpl.Worker var3 = new SourceHistoryProviderWsImpl.Worker(this.config);
      var3.deleteSourceHistoryEntries(var1, var2);
   }

   private static class Worker extends AbstractActivityInfoProviderWsImpl {
      public Worker(CppWebServiceConfiguration var1) {
         super(var1);
      }

      public void getSourceHistory(HistoryRequest var1, GetSourceHistoryCallback var2) {
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

            String[] var5 = var3.getSourceFileHistoryLog(this.userName, this.password, var4);
            if (Thread.currentThread().isInterrupted()) {
               var2.cancelled();
               return;
            }

            List var6 = getSourceFileHistoryEntryList(var5);
            var2.historyWasRetrieved(var6);
         } catch (RemoteException var7) {
            var2.caught(this.convert(var7));
         }
      }

      private static WsOperationInstanceFilter createRequestFilter(HistoryRequest var0) {
         String[] var1 = getStartAndEndDates(var0.timeFilter);
         WsOperationInstanceFilter var2 = startFilter(var0);
         var2.setSearchString("");
         var2.setSortType(getSortOrder(var0.sortOrder));
         var2.setStartDate(var1[0]);
         var2.setEndDate(var1[1]);
         return var2;
      }

      private static List<SourceHistoryEntry> getSourceFileHistoryEntryList(String[] var0) {
         SourceHistoryResponseParser var1 = new SourceHistoryResponseParser();
         return var1.parse(var0);
      }

      public void deleteSourceHistoryEntries(long[] var1, DeleteSourceHistoryCallback var2) {
         try {
            ActivityInfoProvider var3 = this.getServerProxy(var2);
            if (var3 == null) {
               return;
            }

            long[] var4 = var3.deleteSourceFileHistory(this.userName, this.password, var1);
            var2.entriesWereDeleted(var4);
         } catch (RemoteException var5) {
            var2.caught(this.convert(var5));
         }
      }
   }
}
