package org.etlforge.integration.data.test;

import java.util.List;

public interface TestResultHistory<T extends TestResult> {
   void add(T var1);

   void remove(T var1);

   List<T> getStoredResults(TestResultFilter var1);

   void addTestResultStoreListener(TestResultHistoryListener var1);

   void removeTestResultStoreListener(TestResultHistoryListener var1);

   void setMaximumResultAge(int var1);
}
