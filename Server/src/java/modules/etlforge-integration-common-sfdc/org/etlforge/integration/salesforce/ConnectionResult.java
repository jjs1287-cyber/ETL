package org.etlforge.integration.salesforce;

import org.etlforge.system.ArgCheck;

public final class ConnectionResult {
   private final boolean success;
   private final ErrorCode errorCode;
   private final String errorMessage;

   public static ConnectionResult success() {
      return new ConnectionResult(true, null, null);
   }

   public static ConnectionResult error(SalesforceException var0) {
      return new ConnectionResult(false, var0.getErrorCode(), var0.getMessage());
   }

   public static ConnectionResult error(ErrorCode var0, String var1) {
      ArgCheck.checkNotEmpty(var1);
      return new ConnectionResult(false, var0, var1);
   }

   private ConnectionResult(boolean var1, ErrorCode var2, String var3) {
      this.success = var1;
      this.errorCode = var2;
      this.errorMessage = var3;
   }

   public boolean isSuccess() {
      return this.success;
   }

   public boolean isError() {
      return !this.success;
   }

   public String getMessage() {
      return this.isSuccess() ? "Connected succesfully." : this.errorMessage;
   }

   public ErrorCode getErrorCode() {
      return this.isSuccess() ? null : this.errorCode;
   }

   public boolean wasCanceled() {
      return this.getErrorCode() == ErrorCode.REQUEST_CANCELED;
   }
}
