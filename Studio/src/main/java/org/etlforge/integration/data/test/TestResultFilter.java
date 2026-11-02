package org.etlforge.integration.data.test;

public interface TestResultFilter {
   TestResultFilter ACCEPTS_ALL = new TestResultFilter() {
      @Override
      public boolean accepts(TestResult var1) {
         return true;
      }
   };

   boolean accepts(TestResult var1);
}
