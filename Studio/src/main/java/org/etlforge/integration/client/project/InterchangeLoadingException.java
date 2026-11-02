package org.etlforge.integration.client.project;

public class InterchangeLoadingException extends Exception {
   public InterchangeLoadingException(String var1) {
      super(var1);
   }

   public InterchangeLoadingException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public InterchangeLoadingException(Throwable var1) {
      super(var1.getMessage(), var1);
   }
}
