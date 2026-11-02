package org.etlforge.integration.data.test;

import com.google.common.primitives.Longs;
import java.util.Comparator;

public enum TestResultComparators implements Comparator<TestResult> {
   BY_TIMESTAMP {
      public int compare(TestResult var1, TestResult var2) {
         long var3 = var1.getTimestamp();
         long var5 = var2.getTimestamp();
         return Longs.compare(var3, var5);
      }
   },
   BY_OUTCOME {
      public int compare(TestResult var1, TestResult var2) {
         return var1.getOutcome().compareTo(var2.getOutcome());
      }
   },
   BY_DETAILS {
      public int compare(TestResult var1, TestResult var2) {
         return var1.getDetails().compareTo(var2.getDetails());
      }
   };

   TestResultComparators() {
   }
}
