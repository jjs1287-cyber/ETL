package com.etlforge.integration.server.implementation.webservice.bulkload.impl;

import com.google.common.base.Function;
import com.etlforge.integration.client.bulkload.serverapi.BulkLoadInfoProvider;
import com.etlforge.integration.client.bulkload.serverapi.ListBatchesCallback;
import com.etlforge.integration.client.bulkload.serverapi.ListInstancesCallback;
import com.etlforge.integration.server.implementation.webservice.bulkloadinfo.client.BulkLoadInfoProviderServiceLocator;
import com.etlforge.integration.server.implementation.webservice.bulkloadinfo.client.WsBatch;
import com.etlforge.integration.server.implementation.webservice.bulkloadinfo.client.WsBulkLoadInstance;
import com.etlforge.integration.server.implementation.webservice.bulkloadinfo.client.WsBulkLoadInstanceId;
import com.etlforge.integration.server.implementation.webservice.bulkloadinfo.client.WsInstanceFilter;
import com.etlforge.integration.server.implementation.webservice.bulkloadinfo.client.WsRecordOfSentBatch;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.xml.rpc.ServiceException;
import org.etlforge.integration.activity.serverapi.files.OperationFileProvider;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.webservice.JavaWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.JavaWebServiceServerCall;
import org.etlforge.integration.common.server.ServerConversionUtils;
import org.etlforge.integration.data.entity.id.SalesforceBulkLoadId;
import org.etlforge.integration.salesforce.bulkload.backend.Batch;
import org.etlforge.integration.salesforce.bulkload.backend.BatchStatus;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadInstance;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadInstanceFilter;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadInstanceId;
import org.etlforge.integration.salesforce.bulkload.backend.FileStats;
import org.etlforge.integration.salesforce.bulkload.backend.RecordCounts;
import org.etlforge.integration.salesforce.bulkload.backend.RecordOfSentBatch;
import org.etlforge.integration.salesforce.bulkload.backend.SectionOfFile;
import org.etlforge.integration.salesforce.bulkload.backend.SuccessAndFailureCounts;
import org.etlforge.lang.Arrays2;

public final class BulkLoadInfoProviderWsImpl implements BulkLoadInfoProvider {
   private final JavaWebServiceConfiguration config;
   private final OperationFileProvider opFileProvider;

   public BulkLoadInfoProviderWsImpl(JavaWebServiceConfiguration var1, OperationFileProvider var2) {
      this.config = var1;
      this.opFileProvider = var2;
   }

   @Override
   public void listInstances(BulkLoadInstanceFilter var1, ListInstancesCallback var2) {
      BulkLoadInfoProviderWsImpl.Worker var3 = new BulkLoadInfoProviderWsImpl.Worker(this.config);
      var3.listInstances(var1, this.opFileProvider, var2);
   }

   @Override
   public void listBatches(BulkLoadInstanceId var1, ListBatchesCallback var2) {
      BulkLoadInfoProviderWsImpl.Worker var3 = new BulkLoadInfoProviderWsImpl.Worker(this.config);
      var3.listBatches(var1, var2);
   }

   private static class Worker extends JavaWebServiceServerCall {
      private static final String SERVICE_NAME = "konga_bulkload_infoprovider";
      private String user;
      private String password;

      private Worker(JavaWebServiceConfiguration var1) {
         super(var1);
      }

      public void listInstances(BulkLoadInstanceFilter var1, OperationFileProvider var2, ListInstancesCallback var3) {
         try {
            com.etlforge.integration.server.implementation.webservice.bulkloadinfo.client.BulkLoadInfoProvider var4 = this.getProvider(var3);
            if (var4 == null) {
               return;
            }

            if (Thread.currentThread().isInterrupted()) {
               var3.cancelled();
               return;
            }

            WsBulkLoadInstance[] var5 = var4.listInstances(this.user, this.password, createWsFilter(var1));
            if (Thread.currentThread().isInterrupted()) {
               var3.cancelled();
               return;
            }

            List var6 = createClientSideResponse(var5, BulkLoadInstance.class, new BulkLoadInfoProviderWsImpl.Worker.InstanceConverter());
            this.listResultFiles(var2, var3, var6);
            var3.gotInstances(var6);
         } catch (RemoteException var7) {
            var3.caught(this.convert(var7));
         } catch (IntegrationServerException var8) {
            var3.caught(new IntegrationServerException("Server exception: " + var8.getMessage(), var8));
         }
      }

      private com.etlforge.integration.server.implementation.webservice.bulkloadinfo.client.BulkLoadInfoProvider getProvider(ServerCallback var1) throws IntegrationServerException {
         if (Thread.currentThread().isInterrupted()) {
            var1.cancelled();
            return null;
         }

         try {
            BulkLoadInfoProviderServiceLocator var2 = new BulkLoadInfoProviderServiceLocator();
            StringBuilder var3 = new StringBuilder();
            if (!this.getConfiguration().configureWebservice(var2, "konga_bulkload_infoprovider", var3, var1)) {
               return null;
            }

            this.user = this.getConfiguration().getUserName();
            this.password = this.getConfiguration().getPasswordHash();
            var2.setkonga_bulkload_infoproviderEndpointAddress(var3.toString());
            return var2.getkonga_bulkload_infoprovider();
         } catch (ServiceException var4) {
            throw new IntegrationServerException("Failed to call web service: " + var4.getMessage(), var4);
         }
      }

      private static WsInstanceFilter createWsFilter(BulkLoadInstanceFilter var0) {
         WsInstanceFilter var1 = new WsInstanceFilter();
         var1.setBulkLoadGuid(var0.getBulkloadGuid().toString());
         Integer var2 = var0.getMaxRecordsToGet();
         var1.setMaxRecords(var2 == null ? -1 : var2);
         Date var3 = var0.getTimeWindowFilter().getStartCutoff();
         if (var3 != null) {
            var1.setStartDate(ServerConversionUtils.toServerDateFormat(var3));
         }

         Date var4 = var0.getTimeWindowFilter().getEndCutoff();
         if (var4 != null) {
            var1.setEndDate(ServerConversionUtils.toServerDateFormat(var4));
         }

         return var1;
      }

      private static <F, T> List<T> createClientSideResponse(F[] var0, Class<T> var1, Function<F, T> var2) {
         return var0 == null ? Collections.emptyList() : Arrays2.toList(var0, var1, var2);
      }

      private void listResultFiles(OperationFileProvider var1, ListInstancesCallback var2, List<BulkLoadInstance> var3) {
         if (!var3.isEmpty()) {
            BulkLoadOperationFileRetriever var4 = new BulkLoadOperationFileRetriever(var1);
            var4.retrieveOperationFiles(var3, var2);
         }
      }

      public void listBatches(BulkLoadInstanceId var1, ListBatchesCallback var2) {
         try {
            com.etlforge.integration.server.implementation.webservice.bulkloadinfo.client.BulkLoadInfoProvider var3 = this.getProvider(var2);
            if (var3 == null) {
               return;
            }

            if (Thread.currentThread().isInterrupted()) {
               var2.cancelled();
               return;
            }

            WsRecordOfSentBatch[] var4 = var3.listBatches(this.user, this.password, createWsInstanceId(var1));
            if (Thread.currentThread().isInterrupted()) {
               var2.cancelled();
               return;
            }

            List var5 = createClientSideResponse(var4, RecordOfSentBatch.class, new BulkLoadInfoProviderWsImpl.Worker.BatchConverter());
            var2.gotBatches(var5);
         } catch (RemoteException var6) {
            var2.caught(this.convert(var6));
         } catch (IntegrationServerException var7) {
            var2.caught(new IntegrationServerException("Server exception: " + var7.getMessage(), var7));
         }
      }

      private static WsBulkLoadInstanceId createWsInstanceId(BulkLoadInstanceId var0) {
         return new WsBulkLoadInstanceId(var0.getBulkloadGuid().toString(), var0.getOperationInstanceId());
      }

      private static BulkLoadInstanceId convertId(WsBulkLoadInstanceId var0) {
         return new BulkLoadInstanceId(new SalesforceBulkLoadId(var0.getBulkLoadGuid()), var0.getOperationInstanceId());
      }

      private static class BatchConverter implements Function<WsRecordOfSentBatch, RecordOfSentBatch> {
         private BatchConverter() {
         }

         public RecordOfSentBatch apply(WsRecordOfSentBatch var1) {
            return new RecordOfSentBatch(
               BulkLoadInfoProviderWsImpl.Worker.convertId(var1.getInstanceId()),
               convertBatch(var1.getBatch()),
               ServerConversionUtils.fromServerDateFormat(var1.getTimeSent())
            );
         }

         private static Batch convertBatch(WsBatch var0) {
            BatchStatus var1 = BatchStatus.ConvertToEnum(var0.getStatus());
            SectionOfFile var2 = new SectionOfFile(var0.getStartPos(), var0.getEndPos());
            RecordCounts var3 = new RecordCounts(var0.getNumberOfRecords(), var0.getProcessedRecords(), var0.getSuccessRecords(), var0.getFailureRecords());
            return new Batch(var0.getSalesforceId(), var1, var2, var3);
         }
      }

      private static class InstanceConverter implements Function<WsBulkLoadInstance, BulkLoadInstance> {
         private InstanceConverter() {
         }

         public BulkLoadInstance apply(WsBulkLoadInstance var1) {
            BulkLoadInstance var2 = new BulkLoadInstance(
               BulkLoadInfoProviderWsImpl.Worker.convertId(var1.getId()), ServerConversionUtils.fromServerDateFormat(var1.getStartTime())
            );
            if (var1.getLastOperationInstanceId() > 0L) {
               var2.setLastOperationInstanceId(var1.getLastOperationInstanceId());
            }

            var2.setJobId(var1.getJobId());
            var2.setFilePath(var1.getFilePath());
            long var3 = var1.getFileSize();
            long var5 = var1.getStartPos();
            if (var3 >= 0L && var5 >= 0L) {
               var2.setFileStats(new FileStats(var3, var5));
            }

            String var7 = var1.getEndTime();
            if (var7 != null) {
               var2.setEndTime(ServerConversionUtils.fromServerDateFormat(var7));
            }

            SuccessAndFailureCounts var8 = new SuccessAndFailureCounts(var1.getSuccessRecords(), var1.getFailureRecords());
            var2.setSuccessAndFailureCounts(var8);
            return var2;
         }
      }
   }
}
