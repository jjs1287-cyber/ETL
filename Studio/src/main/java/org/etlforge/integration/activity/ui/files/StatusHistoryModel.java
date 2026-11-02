package org.etlforge.integration.activity.ui.files;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.util.List;
import org.etlforge.integration.activity.ActivitySubjectType;
import org.etlforge.integration.activity.client.files.SingleRunResult;
import org.etlforge.integration.activity.client.filter.MaxEntriesToGetFilter;
import org.etlforge.integration.activity.client.log.LogStatus;
import org.etlforge.integration.activity.client.log.LogStatusFilter;
import org.etlforge.integration.activity.serverapi.ActivitySubjectId;
import org.etlforge.integration.activity.serverapi.log.OperationLogRequest;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.property.BooleanPropertyWaitService;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;
import org.etlforge.util.csv.CSVParams;

public final class StatusHistoryModel extends AbstractPropertyBasedModel {
   public static final String BUSY = "busy";
   public static final String RESULT_LIST = "resultList";
   private final Property<Boolean> busy = this.newProperty("busy", Boolean.FALSE);
   private final ListProperty<SingleRunResult> resultList = this.newListProperty("resultList");
   private final OperationId opGuid;
   private final long opInstanceId;
   private final CSVParams csvParams = CSVParams.DEFAULT;
   private final boolean singleRunMode;

   public StatusHistoryModel(OperationId var1) {
      this(Preconditions.checkNotNull(var1, "opGuid"), -1L);
   }

   public StatusHistoryModel(long var1) {
      this(null, ArgCheck.checkPositive(var1, "opInstanceId"));
   }

   private StatusHistoryModel(OperationId var1, long var2) {
      this.opGuid = var1;
      this.opInstanceId = var2;
      this.singleRunMode = var2 >= 0L;
   }

   @VisibleForTesting
   OperationId getOperationGuid() {
      return this.opGuid;
   }

   public CSVParams getCsvParams() {
      return this.csvParams;
   }

   public OperationLogRequest getLogRequest() {
      OperationLogRequest var1 = new OperationLogRequest();
      if (this.opGuid != null) {
         var1.subjectId = new ActivitySubjectId(ActivitySubjectType.OPERATION, this.opGuid);
         var1.maxNumberOfEntriesFilter = new MaxEntriesToGetFilter(100);
      } else {
         var1.opInstanceId = this.opInstanceId;
      }

      var1.statusFilter = new LogStatusFilter(LogStatus.completed());
      return var1;
   }

   public void setResultList(List<SingleRunResult> var1) {
      this.resultList.set(var1);
   }

   public List<SingleRunResult> getResultList() {
      return this.resultList.get();
   }

   public boolean isSingleRunMode() {
      return this.singleRunMode;
   }

   public void setBusy(boolean var1) {
      this.busy.set(var1);
   }

   @VisibleForTesting
   boolean isBusy() {
      return this.busy.get();
   }

   public WaitService asWaitService() {
      return new BooleanPropertyWaitService(this.busy);
   }
}
