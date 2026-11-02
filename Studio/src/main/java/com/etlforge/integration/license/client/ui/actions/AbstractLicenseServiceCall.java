package com.etlforge.integration.license.client.ui.actions;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.etlforge.integration.license.server.LicenseService;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.ui.login.LoginDialog;
import org.etlforge.ui.UiThreadSupplier;

public abstract class AbstractLicenseServiceCall {
   private final LicenseService service;
   private final ServerManager serverManager;
   private LoginCredentials serverCredentials;

   protected AbstractLicenseServiceCall(LicenseService var1, ServerManager var2) {
      Preconditions.checkNotNull(var1, "service");
      Preconditions.checkNotNull(var2, "serverManager");
      this.service = var1;
      this.serverManager = var2;
   }

   public void setServerCredentials(LoginCredentials var1) {
      this.serverCredentials = var1;
   }

   protected final LicenseService getLicenseService() {
      return this.service;
   }

   protected final LoginCredentials getCredentials() {
      return this.serverCredentials != null ? this.serverCredentials : UiThreadSupplier.call(new AbstractLicenseServiceCall.LoginCredentialsSupplier());
   }

   private class LoginCredentialsSupplier implements Supplier<LoginCredentials> {
      private LoginCredentialsSupplier() {
      }

      public LoginCredentials get() {
         LoginCredentials var1 = AbstractLicenseServiceCall.this.serverManager.getCurrentLoginCredentials();
         if (var1 == null) {
            LoginDialog var2 = new LoginDialog(AbstractLicenseServiceCall.this.serverManager);
            var2.setVisible(true);
            return var2.getCredentials();
         } else {
            return var1;
         }
      }
   }
}
