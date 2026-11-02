package org.etlforge.integration.data.test;

import com.google.common.base.Preconditions;

public class TestResultException implements TestResultError {
   private final Throwable cause;

   public TestResultException(Throwable var1) {
      Preconditions.checkNotNull(var1, "cause");
      this.cause = var1;
   }

   @Override
   public String toString() {
      return this.cause.getMessage();
   }

   @Override
   public Throwable getCause() {
      return this.cause;
   }
}
