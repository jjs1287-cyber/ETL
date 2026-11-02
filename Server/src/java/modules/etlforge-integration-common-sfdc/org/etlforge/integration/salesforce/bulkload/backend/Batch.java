package org.etlforge.integration.salesforce.bulkload.backend;

import com.google.common.base.Preconditions;
import org.etlforge.system.ArgCheck;

public final class Batch {
   public final String salesforceId;
   private final SectionOfFile sectionOfFile;
   private final RecordCounts recordCounts;
   private final BatchStatus status;

   public Batch(String var1, BatchStatus var2, SectionOfFile var3, RecordCounts var4) {
      this.salesforceId = ArgCheck.checkNotEmpty(var1);
      this.status = Preconditions.checkNotNull(var2);
      this.sectionOfFile = Preconditions.checkNotNull(var3);
      this.recordCounts = Preconditions.checkNotNull(var4);
   }

   public BatchStatus getStatus() {
      return this.status;
   }

   public long getStartPosition() {
      return this.sectionOfFile.startPos;
   }

   public long getEndPosition() {
      return this.sectionOfFile.endPos;
   }

   public long getBytesSent() {
      return this.sectionOfFile.sizeInBytes();
   }

   public int getNumberOfRecordsSent() {
      return this.recordCounts.sent;
   }

   public int getNumberOfProcessedRecords() {
      return this.recordCounts.processed;
   }

   public int getNumberOfSuccessRecords() {
      return this.recordCounts.success;
   }

   public int getNumberOfFailedRecords() {
      return this.recordCounts.failure;
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof Batch && ((Batch)var1).salesforceId.equals(this.salesforceId);
   }

   @Override
   public int hashCode() {
      return this.salesforceId.hashCode();
   }

   @Override
   public String toString() {
      return String.format("Batch ID: %s, file section: %s, record counts: %s", this.salesforceId, this.sectionOfFile, this.recordCounts);
   }
}
