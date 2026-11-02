package org.etlforge.integration.activity.ui.log;

import java.util.Date;
import org.etlforge.integration.activity.client.log.LogStatus;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.ui.TransferSize;
import org.etlforge.integration.activity.ui.resources.ActivityResources;
import org.etlforge.ui.widget.table.TableColumnMetaData2;
import org.etlforge.util.time.Duration;

public abstract class OperationLogTableColumn extends TableColumnMetaData2<OperationLogEntry> {
   public static final OperationLogTableColumn SELECT_CHECK = new OperationLogTableColumn(" ", Boolean.class, true, 32, false) {
      public Object createColumnValue(OperationLogEntry var1) {
         return Boolean.FALSE;
      }
   };
   public static final OperationLogTableColumn OPERATION = new OperationLogTableColumn(
      ActivityResources.STRINGS.getString("Log.TableModel.Operation"), String.class, false, 175
   ) {
      public Object createColumnValue(OperationLogEntry var1) {
         return var1 == null ? null : var1.getOperationName();
      }
   };
   public static final OperationLogTableColumn STATUS = new OperationLogTableColumn(
      ActivityResources.STRINGS.getString("Log.TableModel.Status"), LogStatus.class, false, 75, false
   ) {
      public Object createColumnValue(OperationLogEntry var1) {
         return var1 == null ? null : var1.getLogStatus();
      }
   };
   public static final OperationLogTableColumn TIME_RECEIVED = new OperationLogTableColumn(
      ActivityResources.STRINGS.getString("Log.TableModel.TimeReceived"), Date.class, false, 100
   ) {
      public Object createColumnValue(OperationLogEntry var1) {
         return var1 == null ? null : var1.getTimeReceived();
      }
   };
   public static final OperationLogTableColumn TIME_STARTED = new OperationLogTableColumn(
      ActivityResources.STRINGS.getString("Log.TableModel.TimeStarted"), Date.class, false, 100
   ) {
      public Object createColumnValue(OperationLogEntry var1) {
         return var1 == null ? null : var1.getStartTime();
      }
   };
   public static final OperationLogTableColumn TIME_DONE = new OperationLogTableColumn(
      ActivityResources.STRINGS.getString("Log.TableModel.TimeDone"), Date.class, false, 100
   ) {
      public Object createColumnValue(OperationLogEntry var1) {
         return var1 == null ? null : var1.getTimeCompleted();
      }
   };
   public static final OperationLogTableColumn DURATION = new OperationLogTableColumn(
      ActivityResources.STRINGS.getString("Log.TableModel.Duration"), Duration.class, false, 100
   ) {
      public Object createColumnValue(OperationLogEntry var1) {
         return var1 == null ? null : var1.getDuration();
      }
   };
   public static final OperationLogTableColumn MESSAGE = new OperationLogTableColumn(
      ActivityResources.STRINGS.getString("Log.TableModel.Message"), String.class, false, 120
   ) {
      public Object createColumnValue(OperationLogEntry var1) {
         return var1 == null ? null : var1.getShortMessage();
      }
   };
   public static final OperationLogTableColumn SOURCE_FILES = new OperationLogTableColumn(
      ActivityResources.STRINGS.getString("Log.TableModel.SourceFiles"), Integer.class, false, 50
   ) {
      public Object createColumnValue(OperationLogEntry var1) {
         return var1 == null ? null : var1.getStats().numberOfSourceFiles;
      }
   };
   public static final OperationLogTableColumn TARGET_FILES = new OperationLogTableColumn(
      ActivityResources.STRINGS.getString("Log.TableModel.TargetFiles"), Integer.class, false, 50
   ) {
      public Object createColumnValue(OperationLogEntry var1) {
         return var1 == null ? null : var1.getStats().numberOfTargetFiles;
      }
   };
   public static final OperationLogTableColumn SOURCE_BYTES = new OperationLogTableColumn(
      ActivityResources.STRINGS.getString("Log.TableModel.SourceBytes"), TransferSize.class, false, 75
   ) {
      public Object createColumnValue(OperationLogEntry var1) {
         return var1 == null ? null : new TransferSize(var1.getStats().sourceTransferBytes);
      }
   };
   public static final OperationLogTableColumn TARGET_BYTES = new OperationLogTableColumn(
      ActivityResources.STRINGS.getString("Log.TableModel.TargetBytes"), TransferSize.class, false, 75
   ) {
      public Object createColumnValue(OperationLogEntry var1) {
         return var1 == null ? null : new TransferSize(var1.getStats().targetTransferBytes);
      }
   };
   public static final OperationLogTableColumn TRANSFER_SIZE = new OperationLogTableColumn(
      ActivityResources.STRINGS.getString("Log.TableModel.TransferSize"), TransferSize.class, false, 75
   ) {
      public Object createColumnValue(OperationLogEntry var1) {
         return var1 == null ? null : new TransferSize(var1.getTotalTransferSizeInBytes());
      }
   };
   public static final OperationLogTableColumn SOURCE_RECORDS = new OperationLogTableColumn(
      ActivityResources.STRINGS.getString("Log.TableModel.SourceRecords"), Long.class, false, 75
   ) {
      public Object createColumnValue(OperationLogEntry var1) {
         return var1 == null ? null : var1.getStats().numberOfSourceRecords;
      }
   };
   public static final OperationLogTableColumn TARGET_RECORDS = new OperationLogTableColumn(
      ActivityResources.STRINGS.getString("Log.TableModel.TargetRecords"), Long.class, false, 75
   ) {
      public Object createColumnValue(OperationLogEntry var1) {
         return var1 == null ? null : var1.getStats().numberOfTargetRecords;
      }
   };
   public static final OperationLogTableColumn FROM = new OperationLogTableColumn(
      ActivityResources.STRINGS.getString("Log.TableModel.From"), String.class, false, 75
   ) {
      public Object createColumnValue(OperationLogEntry var1) {
         return var1 == null ? null : var1.getFrom();
      }
   };

   public static OperationLogTableColumn[] allColumns() {
      return new OperationLogTableColumn[]{
         OPERATION,
         STATUS,
         TIME_RECEIVED,
         FROM,
         TIME_STARTED,
         TIME_DONE,
         DURATION,
         MESSAGE,
         SOURCE_FILES,
         TARGET_FILES,
         SOURCE_BYTES,
         TARGET_BYTES,
         SOURCE_RECORDS,
         TARGET_RECORDS
      };
   }

   private OperationLogTableColumn(String var1, Class<?> var2, boolean var3, int var4, boolean var5) {
      super(var1, var2, var3, var4, var5);
   }

   private OperationLogTableColumn(String var1, Class<?> var2, boolean var3, int var4) {
      super(var1, var2, var3, var4);
   }
}
