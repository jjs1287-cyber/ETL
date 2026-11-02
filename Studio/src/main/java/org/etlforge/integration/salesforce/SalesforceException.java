package org.etlforge.integration.salesforce;

public class SalesforceException extends Exception {
   private final String errorCode;

   public SalesforceException(String var1) {
      super(var1);
      this.errorCode = null;
   }

   public SalesforceException(String var1, String var2) {
      super(var1);
      this.errorCode = var2;
   }

   public SalesforceException(String var1, ErrorCode var2) {
      super(var1);
      this.errorCode = var2 != null ? var2.name() : null;
   }

   public SalesforceException(Throwable var1) {
      super(var1);
      this.errorCode = null;
   }

   public SalesforceException(String var1, Throwable var2) {
      super(var1, var2);
      this.errorCode = null;
   }

   public SalesforceException(String var1, Throwable var2, String var3) {
      super(var1, var2);
      this.errorCode = var3;
   }

   public ErrorCode getErrorCode() {
      if (this.errorCode != null) {
         try {
            return ErrorCode.valueOf(this.errorCode);
         } catch (Exception var2) {
         }
      }

      return ErrorCode.OTHER;
   }

   public String getErrorCodeString() {
      return this.errorCode;
   }

   @Override
   public String getMessage() {
      String var1 = super.getMessage();
      if (var1 == null) {
         var1 = "";
      }

      if (this.errorCode != null) {
         String var2 = " [" + this.errorCode + "]";
         if (!var1.endsWith(var2)) {
            var1 = var1 + var2;
         }
      }

      return var1;
   }

   public boolean isInvalidSessionId() {
      return this.getErrorCode() == ErrorCode.INVALID_SESSION_ID;
   }
}
