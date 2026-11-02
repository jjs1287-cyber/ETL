package org.etlforge.integration.data.test;

public interface TestResultError {
   @Override
   String toString();

   Throwable getCause();
}
