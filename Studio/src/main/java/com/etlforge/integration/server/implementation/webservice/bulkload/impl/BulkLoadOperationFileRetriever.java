package com.etlforge.integration.server.implementation.webservice.bulkload.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Longs;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.etlforge.integration.activity.serverapi.files.ListAvailableFilesCallback;
import org.etlforge.integration.activity.serverapi.files.OperationFileProvider;
import org.etlforge.integration.activity.serverapi.files.OperationFileRequest;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.common.server.file.OperationFile;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadInstance;

final class BulkLoadOperationFileRetriever {
   private final OperationFileProvider opFileProvider;

   public BulkLoadOperationFileRetriever(OperationFileProvider var1) {
      this.opFileProvider = var1;
   }

   public void retrieveOperationFiles(List<BulkLoadInstance> var1, ServerCallback var2) {
      OperationFileRequest var3 = this.createRequest(var1);
      BulkLoadOperationFileRetriever.Callback var4 = new BulkLoadOperationFileRetriever.Callback(var1, var2);
      this.opFileProvider.listAvailableFiles(var3, var4);
   }

   private OperationFileRequest createRequest(List<BulkLoadInstance> var1) {
      ArrayList var2 = Lists.newArrayList();

      for (BulkLoadInstance var4 : var1) {
         var2.add(var4.getOperationInstanceId());
      }

      return OperationFileRequest.forOpInstances(Longs.toArray(var2));
   }

   private static class Callback implements ListAvailableFilesCallback {
      private final Map<Long, BulkLoadInstance> instanceMap = Maps.newHashMap();
      private final ServerCallback caller;

      public Callback(List<BulkLoadInstance> var1, ServerCallback var2) {
         this.caller = var2;

         for (BulkLoadInstance var4 : var1) {
            this.instanceMap.put(var4.getOperationInstanceId(), var4);
         }
      }

      @Override
      public void gotFileList(ServerInfo var1, List<OperationFile> var2) {
         for (OperationFile var4 : var2) {
            long var5 = var4.getOpInstanceId();
            BulkLoadInstance var7 = this.instanceMap.get(var5);
            if (var7 == null) {
               System.err.println("Got an OperationFile for an OperationInstanceId I didn't ask for: " + var5);
            } else {
               switch (var4.getCategory()) {
                  case SFDC_SUCCESS:
                     var7.setSuccessFile(var4);
                     break;
                  case SFDC_FAILURE:
                     var7.setFailureFile(var4);
               }
            }
         }
      }

      @Override
      public void caught(Throwable var1) {
         this.caller.caught(var1);
      }

      @Override
      public void cancelled() {
         this.caller.cancelled();
      }
   }
}
