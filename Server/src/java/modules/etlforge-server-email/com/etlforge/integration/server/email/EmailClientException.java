package com.etlforge.integration.server.email;

public class EmailClientException extends Exception {
   private static final long serialVersionUID = 1L;

   public EmailClientException(String var1) {
      super(var1);
   }

   public EmailClientException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
