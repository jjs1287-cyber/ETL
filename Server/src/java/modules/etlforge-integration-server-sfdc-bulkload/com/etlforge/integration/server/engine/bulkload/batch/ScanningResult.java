package com.etlforge.integration.server.engine.bulkload.batch;

public final class ScanningResult {
   private final int totalNumberOfRecords;
   private final long fileSize;
   private final long[] sectionBreaks;

   public ScanningResult(int var1, long var2, long[] var4) {
      this.totalNumberOfRecords = var1;
      this.fileSize = var2;
      this.sectionBreaks = (long[])var4.clone();
   }

   public int getTotalNumberOfRecords() {
      return this.totalNumberOfRecords;
   }

   public long getFileSize() {
      return this.fileSize;
   }

   public long[] getSectionBreaks() {
      return (long[])this.sectionBreaks.clone();
   }

   public long getStartOfFirstRecord() {
      return this.sectionBreaks.length == 0 ? -1L : this.sectionBreaks[0];
   }

   @Override
   public String toString() {
      return String.format("Number of records: %d, File size: %d bytes", this.totalNumberOfRecords, this.fileSize);
   }
}
