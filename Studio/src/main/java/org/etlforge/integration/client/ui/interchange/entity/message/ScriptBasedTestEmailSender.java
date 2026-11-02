package org.etlforge.integration.client.ui.interchange.entity.message;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import java.util.concurrent.CountDownLatch;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.script.StringLiterals;
import org.etlforge.integration.data.validation.ValidationContext;
import org.etlforge.integration.data.validation.ValidationResult;
import org.etlforge.integration.dataelement.DataElementResolver;
import org.etlforge.integration.debug.client.DebugController;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.DebuggingParameters;
import org.etlforge.integration.debug.client.DebuggingParametersBuilder;
import org.etlforge.integration.debug.client.DebuggingState;
import org.etlforge.integration.debug.client.TargetNode;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.debug.client.event.DebugSessionListener;
import org.etlforge.integration.debug.serverapi.ServerTestCallbackAdapter;
import org.etlforge.integration.debug.serverapi.TestScriptProvider;
import org.etlforge.util.id.KongaGuid;

public final class ScriptBasedTestEmailSender implements TestEmailSender {
   private final TestScriptProvider serverApi;
   private final Supplier<String> blankedOutPasswordSupplier;

   public ScriptBasedTestEmailSender(TestScriptProvider var1, Supplier<String> var2) {
      this.serverApi = Preconditions.checkNotNull(var1);
      this.blankedOutPasswordSupplier = Preconditions.checkNotNull(var2);
   }

   @Override
   public TestEmailSender.Result send(EmailMessage var1) {
      Optional var2 = this.validate(var1);
      return var2.isPresent() ? (TestEmailSender.Result)var2.get() : this.createAndTestExpression(var1);
   }

   private Optional<TestEmailSender.Result> validate(EmailMessage var1) {
      ValidationResult var2 = var1.validate(ValidationContext.EMPTY);
      if (var2.isFatal()) {
         String var3 = "The email definition is invalid: " + var2.concatenateMessages();
         return Optional.of(TestEmailSender.Result.onError(var3));
      } else {
         return Optional.absent();
      }
   }

   private TestEmailSender.Result createAndTestExpression(EmailMessage var1) {
      String var2 = this.constructScript(var1);
      if (var2 == null) {
         return TestEmailSender.Result.whenCanceled();
      }

      ScriptBasedTestEmailSender.Callback var3 = new ScriptBasedTestEmailSender.Callback();
      this.serverApi.testExpression(new ScriptBasedTestEmailSender.SessionImpl(var1), var2, var3);
      return var3.waitForResult();
   }

   private String constructScript(EmailMessage var1) {
      String var2 = Strings.nullToEmpty(var1.getAccountName());
      String var3 = this.getPassword(var1, var2);
      if (var3 == null) {
         return null;
      }

      StringBuilder var4 = new StringBuilder("<trans>");
      var4.append("SendEmail(");
      String var5 = String.format(
         "%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %b",
         resolveDataElementsAndEncode(var1.getSentFrom()),
         resolveDataElementsAndEncode(var1.getSendTo()),
         resolveDataElementsAndEncode(var1.getSubject()),
         resolveDataElementsAndEncode(var1.getText()),
         resolveDataElementsAndEncode(var1.getSmtpServers()),
         resolveDataElementsAndEncode(var2),
         resolveDataElementsAndEncode(var3),
         resolveDataElementsAndEncode(var1.getCC()),
         resolveDataElementsAndEncode(var1.getBCC()),
         resolveDataElementsAndEncode(var1.getReplyTo()),
         var1.getUseSSL()
      );
      var4.append(var5).append(")");
      var4.append("</trans>");
      return var4.toString();
   }

   private static String resolveDataElementsAndEncode(String var0) {
      if (Strings.isNullOrEmpty(var0)) {
         return StringLiterals.doubleQuoted("");
      }

      String var1 = DataElementResolver.resolveDefaultValues(var0);
      return StringLiterals.doubleQuoted(var1);
   }

   private String getPassword(EmailMessage var1, String var2) {
      if (var2.isEmpty()) {
         return "";
      }

      Optional var3 = var1.getAccountPassword();
      return var3.isPresent() ? ((Password)var3.get()).clearText() : this.blankedOutPasswordSupplier.get();
   }

   private static class Callback extends ServerTestCallbackAdapter {
      private volatile TestEmailSender.Result result;
      private final CountDownLatch latch = new CountDownLatch(1);

      private Callback() {
      }

      @Override
      public void completed(DebugSession var1, TestResult var2) {
         String var3 = this.lookForError(var2);
         if (Strings.isNullOrEmpty(var3)) {
            this.result = TestEmailSender.Result.forSuccess();
         } else {
            this.result = TestEmailSender.Result.onError(var3);
         }

         this.latch.countDown();
      }

      private String lookForError(TestResult var1) {
         String var2 = var1.getErrors();
         if (Strings.isNullOrEmpty(var2)) {
            var2 = var1.getResult();
         }

         return var2;
      }

      @Override
      public void caught(Throwable var1) {
         this.result = TestEmailSender.Result.onError("An error occurred while testing to send the email: " + var1.getMessage(), var1);
         this.latch.countDown();
      }

      @Override
      public void cancelled() {
         this.result = TestEmailSender.Result.whenCanceled();
         this.latch.countDown();
      }

      public TestEmailSender.Result waitForResult() {
         try {
            this.latch.await();
            return this.result;
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
            return TestEmailSender.Result.onError("The calling thread was interrupted before the test completed.");
         }
      }

      @Override
      public void aborted(DebugSession var1) {
         this.cancelled();
      }
   }

   private static class SessionImpl implements DebugSession {
      private final EmailMessage email;
      private final KongaGuid guid;
      private DebuggingParameters params;

      public SessionImpl(EmailMessage var1) {
         this.email = var1;
         this.guid = KongaGuid.createGuid();
         DebuggingParametersBuilder var2 = new DebuggingParametersBuilder();
         var2.setSessionId(this.guid).setCommand("Run");
         this.params = var2.build();
      }

      @Override
      public String getTestProviderId() {
         return TestScriptProvider.ID;
      }

      @Override
      public boolean isIsolatedTest() {
         return true;
      }

      @Override
      public IntegrationEntityId getDebuggeeId() {
         return this.email.getID();
      }

      @Override
      public KongaGuid getId() {
         return this.guid;
      }

      @Override
      public DebuggingParameters getParams() {
         return this.params;
      }

      @Override
      public void setParams(DebuggingParameters var1) {
         this.params = var1;
      }

      @Override
      public void aborted() {
      }

      @Override
      public void addListener(DebugSessionListener var1) {
      }

      @Override
      public void completed(TestResult var1) {
      }

      @Override
      public DebugController getController() {
         return null;
      }

      @Override
      public TestResult getResult() {
         return null;
      }

      @Override
      public DebuggingState getState() {
         return null;
      }

      @Override
      public void setTargetNode(TargetNode var1) {
      }

      @Override
      public TargetNode getTargetNode() {
         return null;
      }

      @Override
      public void setSourceDataGuid(KongaGuid var1) {
      }

      @Override
      public KongaGuid getSourceDataGuid() {
         return null;
      }

      @Override
      public void paused(TestResult var1) {
      }

      @Override
      public void removeListener(DebugSessionListener var1) {
      }

      @Override
      public void resumed() {
      }
   }
}
