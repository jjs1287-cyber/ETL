package org.etlforge.integration.server.implementation.webservice.activity.impl;

import com.google.common.base.Function;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.activity.client.log.LogStatus;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.client.log.OperationLogMessage;
import org.etlforge.integration.activity.client.log.OperationStats;
import org.etlforge.integration.activity.client.progress.OperationProgressMessage;
import org.etlforge.integration.activity.client.progress.ProgressSnapshot;
import org.etlforge.integration.activity.client.queue.QueueStatus;
import org.etlforge.integration.common.server.ServerConversionUtils;
import org.etlforge.integration.common.server.file.FileCategory;
import org.etlforge.integration.common.server.file.OperationFile;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.server.implementation.webservice.activity.client.WsOperationFileStatus;
import org.etlforge.integration.server.implementation.webservice.activity.client.WsOperationLogInstance;
import org.etlforge.integration.server.implementation.webservice.activity.client.WsOperationMessage;
import org.etlforge.integration.server.implementation.webservice.activity.client.WsOperationProgress;
import org.etlforge.lang.Arrays2;
import org.etlforge.system.EnumWithIdLookup;
import org.etlforge.util.id.KongaGuid;

final class Functions {
   private static Long getOptionalLong(long var0) {
      return var0 < 0L ? null : var0;
   }

   private Functions() {
   }

   public static final class FromWsOperationFile implements Function<WsOperationFileStatus, OperationFile> {
      public OperationFile apply(WsOperationFileStatus var1) {
         return new OperationFile(
            new OperationId(var1.getOperationGUID()),
            var1.getOperationInstanceId(),
            EnumWithIdLookup.lookup(FileCategory.class, var1.getCategory()),
            KongaGuid.createGuid(var1.getGUID()),
            var1.getFilename(),
            var1.getFileSize(),
            this.getEncoding(var1),
            var1.getNumberOfRecords(),
            var1.getFileStatusMessage()
         );
      }

      private Charset getEncoding(WsOperationFileStatus var1) {
         String var2 = var1.getFileCharEncode();
         return StringUtils.isEmpty(var2) ? Charset.defaultCharset() : Charset.forName(var2);
      }
   }

   public static final class LogEntry implements Function<WsOperationLogInstance, OperationLogEntry> {
      private final Function<WsOperationMessage, OperationLogMessage> messageFunction = new Functions.LogMessage();

      public OperationLogEntry apply(WsOperationLogInstance var1) {
         OperationLogEntry var2 = new OperationLogEntry(
            new OperationId(var1.getOperationGuid()),
            var1.getOperationName(),
            var1.getOperationInstanceId(),
            LogStatus.fromServerId(var1.getStatus()),
            ServerConversionUtils.fromServerDateFormat(var1.getStartTimeStamp()),
            ServerConversionUtils.fromServerDateFormat(var1.getStatusTimeStamp()),
            var1.getMessage(),
            this.getMessages(var1)
         );
         var2.setMessageId(Functions.getOptionalLong(var1.getMsgId()));
         var2.setParentOpInstanceId(Functions.getOptionalLong(var1.getParentOperationInstanceId()));
         var2.setStats(buildOpStats(var1));
         var2.setFrom(var1.getFrom() == null ? "" : var1.getFrom());
         var2.setTimeReceived(var1.getReceivedTimeStamp() == null ? null : ServerConversionUtils.fromServerDateFormat(var1.getReceivedTimeStamp()));
         return var2;
      }

      private List<OperationLogMessage> getMessages(WsOperationLogInstance var1) {
         WsOperationMessage[] var2 = var1.getOperationMessages();
         return var2 == null ? Collections.emptyList() : Arrays2.toList(var1.getOperationMessages(), OperationLogMessage.class, this.messageFunction);
      }

      private static OperationStats buildOpStats(WsOperationLogInstance var0) {
         return OperationStats.build()
            .sourceFiles(var0.getNumberOfSourceFiles())
            .targetFiles(var0.getNumberOfTargetFiles())
            .sourceTransferBytes(var0.getSourceTransferBytes())
            .targetTransferBytes(var0.getTargetTransferBytes())
            .sourceRecords(var0.getNumberOfSourceRecords())
            .targetRecords(var0.getNumberOfTargetRecords())
            .done();
      }
   }

   private static final class LogMessage implements Function<WsOperationMessage, OperationLogMessage> {
      private LogMessage() {
      }

      public OperationLogMessage apply(WsOperationMessage var1) {
         return new OperationLogMessage(var1.getFieldName(), var1.getMessage());
      }
   }

   public static final class Progress implements Function<WsOperationProgress, OperationProgressMessage> {
      public OperationProgressMessage apply(WsOperationProgress var1) {
         ProgressSnapshot var2 = new ProgressSnapshot(var1.getSection(), var1.getMessage(), var1.getPercentageDone(), this.getEndTime(var1));
         return new OperationProgressMessage(new OperationId(var1.getOperationGuid()), var1.getOperationInstanceId(), var2);
      }

      private Date getEndTime(WsOperationProgress var1) {
         String var2 = var1.getEstimatedEndTimeUtc();
         return var2 == null ? null : ServerConversionUtils.fromServerDateFormat(var2);
      }
   }

   public static final class QueueEntry implements Function<WsOperationLogInstance, OperationLogEntry> {
      public OperationLogEntry apply(WsOperationLogInstance var1) {
         OperationLogEntry var2 = new OperationLogEntry(
            new OperationId(var1.getOperationGuid()),
            var1.getOperationName(),
            var1.getOperationInstanceId(),
            getStatus(var1).toLogStatus(),
            null,
            null,
            var1.getMessage(),
            Collections.emptyList()
         );
         var2.setMessageId(var1.getMsgId() >= 0L ? var1.getMsgId() : null);
         var2.setTimeReceived(ServerConversionUtils.fromServerDateFormat(var1.getReceivedTimeStamp()));
         var2.setFrom(var1.getFrom());
         var2.setParentOpInstanceId(Functions.getOptionalLong(var1.getParentOperationInstanceId()));
         return var2;
      }

      private static QueueStatus getStatus(WsOperationLogInstance var0) {
         LogStatus var1 = LogStatus.fromServerId(var0.getStatus());
         switch (var1) {
            case PENDING:
               return QueueStatus.PENDING;
            case RUNNING:
               return QueueStatus.RUNNING;
            default:
               throw new RuntimeException("Unexpected status for entry in queue: " + var1);
         }
      }
   }
}
