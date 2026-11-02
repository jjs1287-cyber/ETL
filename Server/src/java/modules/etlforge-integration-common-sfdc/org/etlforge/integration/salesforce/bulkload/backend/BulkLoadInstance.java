package org.etlforge.integration.salesforce.bulkload.backend;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import org.etlforge.integration.common.server.file.FileCategory;
import org.etlforge.integration.common.server.file.OperationFile;
import org.etlforge.integration.common.server.file.OperationFileOwner;
import org.etlforge.integration.data.entity.id.SalesforceBulkLoadId;

public final class BulkLoadInstance implements OperationFileOwner {
   private final BulkLoadInstanceId id;
   private final long startTime;
   private Long endTime;
   private Long lastOperationInstanceId;
   private String jobId;
   private String filePath;
   private FileStats fileStats;
   private List<RecordOfSentBatch> batches;
   private SuccessAndFailureCounts successAndFailureCounts = SuccessAndFailureCounts.ZERO;
   private final EnumMap<FileCategory, OperationFile> files = Maps.newEnumMap(FileCategory.class);

   public BulkLoadInstance(BulkLoadInstanceId var1, Date var2) {
      this.id = Preconditions.checkNotNull(var1);
      this.startTime = var2.getTime();
   }

   public synchronized BulkLoadInstanceId getId() {
      return this.id;
   }

   public synchronized SalesforceBulkLoadId getBulkloadGuid() {
      return this.id.getBulkloadGuid();
   }

   public synchronized long getOperationInstanceId() {
      return this.id.getOperationInstanceId();
   }

   public synchronized Date getStartTime() {
      return new Date(this.startTime);
   }

   public synchronized Long getLastOperationInstanceId() {
      return this.lastOperationInstanceId;
   }

   public synchronized void setLastOperationInstanceId(Long var1) {
      this.lastOperationInstanceId = var1;
   }

   public synchronized String getJobId() {
      return this.jobId;
   }

   public synchronized void setJobId(String var1) {
      Preconditions.checkArgument(var1 == null || !var1.isEmpty(), "jobId must be either null, or not empty");
      this.jobId = var1;
   }

   public synchronized Date getEndTime() {
      return this.endTime == null ? null : new Date(this.endTime);
   }

   public synchronized void setEndTime(Date var1) {
      this.endTime = var1 == null ? null : var1.getTime();
   }

   public synchronized String getFilePath() {
      return this.filePath;
   }

   public synchronized void setFilePath(String var1) {
      Preconditions.checkArgument(var1 == null || !var1.isEmpty(), "filePath must be either null, or not empty");
      this.filePath = var1;
   }

   public synchronized FileStats getFileStats() {
      return this.fileStats;
   }

   public synchronized void setFileStats(FileStats var1) {
      this.fileStats = var1;
   }

   public synchronized List<RecordOfSentBatch> getBatches() {
      ArrayList var1 = Lists.newArrayList();
      if (this.batches != null) {
         var1.addAll(this.batches);
      }

      return var1;
   }

   public synchronized void setBatches(List<RecordOfSentBatch> var1) {
      Preconditions.checkNotNull(var1, "batches");
      this.batches = Lists.newArrayList(var1);
   }

   public synchronized Double getPercentageSent() {
      if (this.fileStats != null && this.batches != null) {
         long var1 = this.fileStats.fileSize - this.fileStats.startPos;
         return var1 == 0L ? 100.0 : Math.min(100.0, 100.0 * this.countBytesSent() / var1);
      } else {
         return null;
      }
   }

   private long countBytesSent() {
      long var1 = 0L;

      for (RecordOfSentBatch var4 : this.batches) {
         var1 += var4.getBytesSent();
      }

      return var1;
   }

   public synchronized SuccessAndFailureCounts getSuccessAndFailureCounts() {
      return this.successAndFailureCounts;
   }

   public synchronized void setSuccessAndFailureCounts(SuccessAndFailureCounts var1) {
      this.successAndFailureCounts = Preconditions.checkNotNull(var1);
   }

   public synchronized void setSuccessFile(OperationFile var1) {
      this.storeFile(FileCategory.SFDC_SUCCESS, var1);
   }

   private void storeFile(FileCategory var1, OperationFile var2) {
      if (var2 != null) {
         assert var2.getCategory() == var1;
         this.files.put(var1, var2);
      } else {
         this.files.remove(var1);
      }
   }

   public synchronized void setFailureFile(OperationFile var1) {
      this.storeFile(FileCategory.SFDC_FAILURE, var1);
   }

   @Override
   public synchronized boolean hasFile(FileCategory var1) {
      return this.files.containsKey(var1);
   }

   @Override
   public OperationFile getFile(FileCategory var1) {
      return this.files.get(var1);
   }
}
