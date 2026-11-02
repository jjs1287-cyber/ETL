package org.etlforge.application.update;

import com.google.common.base.Preconditions;

public final class UpdateServiceException extends Exception {
   private final ErrorCode errorCode;

   static UpdateServiceException timeout() {
      return new UpdateServiceException(ErrorCode.TIMEOUT, "The update service did not respond in time. Please try again later.");
   }

   static UpdateServiceException unknownLatestVersion() {
      return new UpdateServiceException(ErrorCode.UNKNOWN_LATEST_VERSION, "Cannot find information about the latest version. Please contact support.");
   }

   public UpdateServiceException(ErrorCode var1, String var2) {
      super(var2);
      this.errorCode = Preconditions.checkNotNull(var1);
   }

   public UpdateServiceException(ErrorCode var1, Throwable var2) {
      super(var2.getMessage(), var2);
      this.errorCode = Preconditions.checkNotNull(var1);
   }

   public UpdateServiceException(ErrorCode var1, String var2, Throwable var3) {
      super(var2, var3);
      this.errorCode = Preconditions.checkNotNull(var1);
   }

   public ErrorCode getErrorCode() {
      return this.errorCode;
   }
}
