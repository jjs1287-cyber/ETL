package org.etlforge.integration.client.ui.interchange.entity.message;

import com.google.common.base.Optional;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.util.string.StringLimiter;

public final class TestEmailUiJob extends UiJob {
   private final EmailMessage email;
   private final TestEmailSender sender;

   public TestEmailUiJob(EmailMessage var1, TestEmailSender var2) {
      super("Testing sending the email \"" + StringLimiter.limit(var1.getName(), 40) + "\"");
      this.email = var1;
      this.sender = var2;
      this.setBlockUntilDone(true);
      this.setUncaughtExceptionHandler(LoggingExceptionHandler.attention(TestEmailUiJob.class, "An unexpected error occurred."));
      this.setMessage("Sending the email");
   }

   @Override
   protected void runImpl() throws InterruptedException {
      TestEmailSender.Result var1 = this.sender.send(this.email);
      if (var1.succeeded()) {
         NonBlockingAlert.info(ApplicationUi.getAppWinFrame(), "The email has been sent. Please check the recipient mail box.", "Email Sent");
      } else if (var1.failed()) {
         this.processError(var1);
      }
   }

   private void processError(TestEmailSender.Result var1) {
      Optional var2 = var1.getErrorCause();
      if (var2.isPresent()) {
         Throwable var3 = (Throwable)var2.get();
         ErrorLog.attention(this.getClass(), var1.getErrorMessage(), var3);
      } else {
         NonBlockingAlert.error(ApplicationUi.getAppWinFrame(), var1.getErrorMessage(), "Error");
      }
   }
}
