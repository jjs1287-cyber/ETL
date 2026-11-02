package org.etlforge.integration.data.test;

public interface TestResultHistoryListener {
   void onResultAdded(TestResult var1);

   void onResultRemoved(TestResult var1);

   void onHistoryRebuilt();
}
