package org.etlforge.integration.data.test;

import java.util.Collections;
import java.util.List;

public class EmptyTestResultHistory<T extends TestResult> implements TestResultHistory<T> {
   @Override
   public final void add(T var1) {
   }

   @Override
   public void remove(T var1) {
   }

   @Override
   public List<T> getStoredResults(TestResultFilter var1) {
      return Collections.emptyList();
   }

   @Override
   public void addTestResultStoreListener(TestResultHistoryListener var1) {
   }

   @Override
   public void removeTestResultStoreListener(TestResultHistoryListener var1) {
   }

   @Override
   public void setMaximumResultAge(int var1) {
   }
}
