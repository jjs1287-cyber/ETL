package org.etlforge.util.arithmetic;

public enum ComparisonOperator {
   LT("<") {
      @Override
      public boolean isTrue(long var1, long var3) {
         return var1 < var3;
      }
   },
   LE("<=") {
      @Override
      public boolean isTrue(long var1, long var3) {
         return var1 <= var3;
      }
   },
   EQ("=") {
      @Override
      public boolean isTrue(long var1, long var3) {
         return var1 == var3;
      }
   },
   GE(">=") {
      @Override
      public boolean isTrue(long var1, long var3) {
         return var1 >= var3;
      }
   },
   GT(">") {
      @Override
      public boolean isTrue(long var1, long var3) {
         return var1 > var3;
      }
   };

   private String m_string;

   public abstract boolean isTrue(long var1, long var3);

   @Override
   public String toString() {
      return this.m_string;
   }

   ComparisonOperator(String var3) {
      this.m_string = var3;
   }
}
