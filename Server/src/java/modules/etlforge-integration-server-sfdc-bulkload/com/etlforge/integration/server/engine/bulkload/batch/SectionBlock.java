package com.etlforge.integration.server.engine.bulkload.batch;

import java.util.List;

final class SectionBlock {
   final long sectionEnd;
   private final Interval[] gap_array;
   long currentPos;
   private long gapEnd;
   private int gapIndex;

   public SectionBlock(List<Interval> var1) {
      int var2 = var1.size();
      this.gap_array = var1.toArray(new Interval[var2]);
      this.gapIndex = 0;
      if (var2 == 0) {
         this.currentPos = 0L;
         this.gapEnd = 0L;
         this.sectionEnd = 0L;
      } else {
         this.currentPos = this.gap_array[0].start;
         this.gapEnd = this.gap_array[0].end;
         this.sectionEnd = this.gap_array[var2 - 1].end;
      }
   }

   public SectionBlock(Interval var1) {
      this.gap_array = new Interval[1];
      this.gap_array[0] = var1;
      this.gapIndex = 0;
      this.currentPos = var1.start;
      this.gapEnd = var1.end;
      this.sectionEnd = var1.end;
   }

   public void updateCurrentPosition() {
      if (this.currentPos < this.sectionEnd) {
         if (this.currentPos >= this.gapEnd) {
            this.gapIndex++;
            assert this.gapIndex < this.gap_array.length;
            Interval var1 = this.gap_array[this.gapIndex];
            this.currentPos = var1.start;
            this.gapEnd = var1.end;
         }
      }
   }

   public boolean done() {
      return this.currentPos >= this.sectionEnd;
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[");
      boolean var2 = true;

      for (Interval var6 : this.gap_array) {
         if (var2) {
            var2 = false;
         } else {
            var1.append(", ");
         }

         var1.append(var6);
      }

      var1.append("]");
      return var1.toString();
   }
}
