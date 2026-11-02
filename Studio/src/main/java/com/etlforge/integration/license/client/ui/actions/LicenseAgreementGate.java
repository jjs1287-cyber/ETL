package com.etlforge.integration.license.client.ui.actions;

import com.google.common.base.Supplier;
import com.etlforge.integration.license.server.GetLicenseAgreementCallback;
import com.etlforge.integration.license.server.LicenseService;
import java.awt.EventQueue;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.integration.client.server.login.InvalidCredentialsException;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.ui.Size;
import org.etlforge.ui.widget.text.KongaTextArea;

final class LicenseAgreementGate {
   private final LicenseService service;
   private final LicenseAgreementQuestion question;

   public LicenseAgreementGate(LicenseService var1, LicenseAgreementQuestion var2) {
      this.service = var1;
      this.question = var2;
   }

   public LicenseAgreementAnswer isAgreementAccepted(String var1, LoginCredentials var2) {
      LicenseAgreementGate.AgreementFetcher var3 = new LicenseAgreementGate.AgreementFetcher(var1, var2);
      String var4 = var3.get();
      if (var4 == null) {
         return var3.failed
            ? LicenseAgreementAnswer.ERROR
            : (var3.invalidCredentials ? LicenseAgreementAnswer.INVALID_CREDENTIALS : LicenseAgreementAnswer.DID_NOT_AGREE);
      } else {
         return this.question.doYouAgree(var4) ? LicenseAgreementAnswer.AGREED : LicenseAgreementAnswer.DID_NOT_AGREE;
      }
   }

   private class AgreementFetcher implements Supplier<String> {
      private final String licenseKey;
      private final LoginCredentials credentials;
      private final GetLicenseAgreementCallback callback;
      private String agreement;
      private final CountDownLatch latch;
      public boolean failed;
      public boolean invalidCredentials;

      public AgreementFetcher(String var2, LoginCredentials var3) {
         this.licenseKey = var2;
         this.credentials = var3;
         this.callback = new LicenseAgreementGate.AgreementFetcher.Callback();
         this.latch = new CountDownLatch(1);
      }

      public String get() {
         this.startDownload();
         return this.returnDownloadedAgreement();
      }

      private void startDownload() {
         LicenseAgreementGate.this.service.getLicenseAgreement(this.licenseKey, this.credentials, this.callback);
      }

      private String returnDownloadedAgreement() {
         try {
            this.latch.await();
            return this.agreement;
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
            return null;
         }
      }

      private class Callback implements GetLicenseAgreementCallback {
         private Callback() {
         }

         @Override
         public void gotLicenseAgreement(String var1) {
            AgreementFetcher.this.agreement = var1;
            AgreementFetcher.this.latch.countDown();
         }

         @Override
         public void invalidCredentials(InvalidCredentialsException var1) {
            AgreementFetcher.this.invalidCredentials = true;
            NonBlockingAlert.error(ApplicationUi.getAppWinFrame(), var1.getMessage(), "Invalid credentials");
            AgreementFetcher.this.latch.countDown();
         }

         @Override
         public void cancelled() {
            AgreementFetcher.this.latch.countDown();
         }

         @Override
         public void caught(Throwable var1) {
            AgreementFetcher.this.failed = true;
            ErrorLog.attention(
               LicenseService.class,
               "An error occurred while downloading the license agreement from the ETLForge server. Please contact ETLForge support.",
               var1
            );
            AgreementFetcher.this.latch.countDown();
         }

         @Override
         public void licenseError(final String var1) {
            AgreementFetcher.this.failed = true;
            AgreementFetcher.this.latch.countDown();
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  KongaTextArea var1x = KongaTextArea.viewer(var1, Size.rows(10), Size.columns(60));
                  NonBlockingAlert.message(null, var1x, "License Agreement Error", Alert.Type.ERROR);
               }
            });
         }
      }
   }
}
