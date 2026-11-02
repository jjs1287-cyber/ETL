package org.etlforge.integration.client.ui.interchange.entity.message;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.system.ArgCheck;

public interface TestEmailSender {
   TestEmailSender.Result send(EmailMessage var1);

   final class Result {
      private final TestEmailSender.Result.State state;
      private final String errorMessage;
      private final Optional<Throwable> errorCause;

      public static TestEmailSender.Result forSuccess() {
         return new TestEmailSender.Result(TestEmailSender.Result.State.SUCCESS, null);
      }

      public static TestEmailSender.Result whenCanceled() {
         return new TestEmailSender.Result(TestEmailSender.Result.State.CANCELED, null);
      }

      public static TestEmailSender.Result onError(String var0) {
         return new TestEmailSender.Result(TestEmailSender.Result.State.ERROR, ArgCheck.checkNotEmpty(var0));
      }

      public static TestEmailSender.Result onError(String var0, Throwable var1) {
         return new TestEmailSender.Result(TestEmailSender.Result.State.ERROR, ArgCheck.checkNotEmpty(var0), Preconditions.checkNotNull(var1));
      }

      private Result(TestEmailSender.Result.State var1, String var2) {
         this(var1, var2, null);
      }

      private Result(TestEmailSender.Result.State var1, String var2, Throwable var3) {
         this.state = var1;
         this.errorMessage = var2;
         this.errorCause = Optional.fromNullable(var3);
      }

      public boolean succeeded() {
         return this.state == TestEmailSender.Result.State.SUCCESS;
      }

      public boolean canceled() {
         return this.state == TestEmailSender.Result.State.CANCELED;
      }

      public boolean failed() {
         return this.state == TestEmailSender.Result.State.ERROR;
      }

      public String getErrorMessage() {
         Preconditions.checkState(this.failed(), "getErrorMessage should not be called unless the test failed.");
         return this.errorMessage;
      }

      public Optional<Throwable> getErrorCause() {
         Preconditions.checkState(this.failed(), "getErrorCauseshould not be called unless the test failed.");
         return this.errorCause;
      }

      private enum State {
         SUCCESS,
         ERROR,
         CANCELED;
      }
   }
}
