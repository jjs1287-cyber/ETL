package org.etlforge.integration.data.project;

public class IntegrationProjectAccessException extends RuntimeException {
   public IntegrationProjectAccessException() {
      super("The current thread does not hold the IntegrationProjectLock.");
   }
}
