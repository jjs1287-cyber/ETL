package com.etlforge.integration.client.ui.sfdc;

import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.integration.salesforce.ErrorCode;
import org.etlforge.integration.salesforce.SalesforceException;
import org.etlforge.lang.exception.ExceptionHandler;

public final class AttentionErrorHandler implements ExceptionHandler<Throwable> {
   private final LoggingExceptionHandler delegate;

   public AttentionErrorHandler(Object var1) {
      this.delegate = new LoggingExceptionHandler(var1, ErrorLevel.ATTENTION);
   }

   @Override
   public void caught(Throwable var1) {
      if (var1 instanceof SalesforceException) {
         ErrorCode var2 = ((SalesforceException)var1).getErrorCode();
         if (var2 == ErrorCode.REQUEST_CANCELED) {
            return;
         }
      }

      this.delegate.caught(var1);
   }
}
