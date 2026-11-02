package com.etlforge.integration.server.engine.bulkload.batch;

import com.google.common.collect.Lists;
import java.util.List;

final class Bundle {
   private final long pos;
   private volatile long next;
   private final List<String> records = Lists.newArrayList();

   public Bundle(long var1) {
      this.pos = var1;
   }

   public void addRecord(String var1) {
      this.records.add(var1);
   }

   public void setNextPos(long var1) {
      this.next = var1;
   }

   public long getStartPos() {
      return this.pos;
   }

   public long getNextPos() {
      return this.next;
   }

   public Iterable<String> getRecords() {
      return this.records;
   }

   public int getNumberOfRecords() {
      return this.records.size();
   }

   @Override
   public String toString() {
      return String.format("Starts at: %d, ends at: %d, number of records: %d", this.pos, this.next, this.records.size());
   }
}
