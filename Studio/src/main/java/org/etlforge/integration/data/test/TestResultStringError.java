package org.etlforge.integration.data.test;

import com.google.common.base.Preconditions;

public class TestResultStringError implements TestResultError {
   private final String error;

   public TestResultStringError(String var1) {
      Preconditions.checkNotNull(var1, "error");
      this.error = var1;
   }

   @Override
   public String toString() {
      return this.error;
   }

   @Override
   public Throwable getCause() {
      return null;
   }
}
