package org.etlforge.integration.data.entity.webservicecall;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.CallRestrictions;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;

public final class WebServiceCallValidator {
   private final WebServiceCall wsCall;
   private final ValidationMessageCollector collector;

   public WebServiceCallValidator(WebServiceCall var1, ValidationMessageCollector var2) {
      Preconditions.checkNotNull(var1, "wsCall");
      Preconditions.checkNotNull(var2, "collector");
      this.wsCall = var1;
      this.collector = var2;
   }

   public void run() {
      this.validateUrl();
      this.validateCallRestrictions();
   }

   private void validateUrl() {
      String var1 = this.wsCall.getUrl().trim();
      if (var1 == null || var1.length() == 0) {
         this.fatal(WebServiceCallMessages.getString("WebServiceCallValidator.UrlMissing"));
      }
   }

   private void validateCallRestrictions() {
      CallRestrictions var1 = this.wsCall.getCallRestrictions();
      this.validateCallDelay(var1);
      this.validatePostingSize(var1);
      this.validateRetries(var1);
      this.validateRetryDelay(var1);
   }

   private void validateCallDelay(CallRestrictions var1) {
      this.validate(WebServiceCallMessages.getString("WebServiceCallValidator.CallDelay"), var1.getMinimumCallDelayInMilliSecs());
   }

   private void validatePostingSize(CallRestrictions var1) {
      this.validate(WebServiceCallMessages.getString("WebServiceCallValidator.MaxSize"), var1.getMaximumSizeInBytes());
   }

   private void validateRetries(CallRestrictions var1) {
      this.validate(WebServiceCallMessages.getString("WebServiceCallValidator.Retries"), var1.getRetries());
   }

   private void validateRetryDelay(CallRestrictions var1) {
      this.validate(WebServiceCallMessages.getString("WebServiceCallValidator.RetryDelay"), var1.getRetryDelayInMilliSecs());
   }

   private void validate(String var1, long var2) {
      if (var2 < 0L || var2 > 2147483647L) {
         this.fatal(WebServiceCallMessages.getFormattedMessage("WebServiceCallValidator.OutOfRange", new Object[]{var1, Integer.MAX_VALUE}));
      }
   }

   private void fatal(String var1) {
      this.collector.addMessage(var1, ValidationStatus.INVALID);
   }
}
