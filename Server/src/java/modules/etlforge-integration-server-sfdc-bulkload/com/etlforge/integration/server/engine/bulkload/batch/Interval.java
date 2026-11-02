package com.etlforge.integration.server.engine.bulkload.batch;

final class Interval {
   final String id;
   long start;
   long end;
   String ret;

   public Interval(String var1, long var2, long var4) {
      this.id = var1;
      this.start = var2;
      this.end = var4;
   }

   public Interval(String var1) throws Exception {
      String[] var2 = var1.split(",");
      if (var2.length != 4) {
         throw new Exception("wrong input " + var1);
      }

      this.id = var2[0];
      this.ret = var2[1];
      this.start = Long.parseLong(var2[2]);
      this.end = Long.parseLong(var2[3]);
   }

   public boolean isError() {
      return this.ret.equals("ERROR");
   }

   public boolean overlap(Interval var1) {
      return this.end > var1.start && this.start < var1.end;
   }

   @Override
   public String toString() {
      return this.id + "(" + this.start + "," + this.end + ")";
   }
}
