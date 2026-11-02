package org.etlforge.integration.activity.ui.files;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.etlforge.integration.activity.client.files.SingleRunResult;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.common.server.file.OperationFile;

final class ResultListBuilder {
   private final StatusHistoryModel model;
   private final List<OperationLogEntry> logs;
   private final List<OperationFile> files;

   public ResultListBuilder(StatusHistoryModel var1, List<OperationLogEntry> var2, List<OperationFile> var3) {
      this.model = var1;
      this.logs = var2;
      this.files = var3;
   }

   public void build() {
      try {
         Map var1 = this.createLogMap();
         this.addFiles(var1);
         List var2 = this.buildResultList(var1);
         this.model.setResultList(var2);
      } finally {
         this.model.setBusy(false);
      }
   }

   private Map<Long, ResultListBuilder.Entry> createLogMap() {
      HashMap var1 = Maps.newHashMap();

      for (OperationLogEntry var3 : this.logs) {
         long var4 = var3.getOpInstanceId();
         var1.put(var4, new ResultListBuilder.Entry(var3));
      }

      return var1;
   }

   private void addFiles(Map<Long, ResultListBuilder.Entry> var1) {
      for (OperationFile var3 : this.files) {
         ResultListBuilder.Entry var4 = (ResultListBuilder.Entry)var1.get(var3.getOpInstanceId());
         if (var4 != null) {
            var4.update(var3);
         } else {
            String var5 = String.format(
               "Got an OperationFile for an operation instance that we do not have a log entry for. Operation GUID = %s, Operation Instance: %d",
               var3.getOperationId(),
               var3.getOpInstanceId()
            );
            System.err.println(var5);
         }
      }
   }

   private List<SingleRunResult> buildResultList(Map<Long, ResultListBuilder.Entry> var1) {
      Iterable var2 = Iterables.transform(var1.values(), ResultListBuilder.Entry.TRANSFORM);
      return Ordering.from(new DisplayOrder()).sortedCopy(var2);
   }

   private static class Entry {
      public static final Function<ResultListBuilder.Entry, SingleRunResult> TRANSFORM = new Function<ResultListBuilder.Entry, SingleRunResult>() {
         public SingleRunResult apply(ResultListBuilder.Entry var1) {
            return var1.toResult();
         }
      };
      private final OperationLogEntry log;
      private OperationFile success;
      private OperationFile failure;

      public Entry(OperationLogEntry var1) {
         this.log = var1;
      }

      public SingleRunResult toResult() {
         return new SingleRunResult(this.log, this.success, this.failure);
      }

      public void update(OperationFile var1) {
         switch (var1.getCategory()) {
            case SFDC_SUCCESS:
               this.success = var1;
               break;
            case SFDC_FAILURE:
               this.failure = var1;
         }
      }
   }
}
