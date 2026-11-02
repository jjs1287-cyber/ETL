package com.etlforge.integration.license.client.ui;

import com.etlforge.integration.license.client.ui.actions.DownloadLicenseKeyGenerationInfoAction;
import com.etlforge.integration.license.server.LicenseService;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.ui.wait.WaitService;

final class RequestNewLicenseAction extends AbstractAction {
   private final DownloadLicenseKeyGenerationInfoAction impl;
   private JDialog dialog;

   public RequestNewLicenseAction(ServerManager var1, WaitService var2) {
      LicenseService var3 = IntegrationServer.getInstance().getServerCall(LicenseService.class);
      this.impl = new DownloadLicenseKeyGenerationInfoAction(var1, var3, var2);
   }

   @Override
   public Object getValue(String var1) {
      return this.impl.getValue(var1);
   }

   public void setServerCredentials(LoginCredentials var1) {
      this.impl.setServerCredentials(var1);
   }

   public void setDialogToClose(JDialog var1) {
      this.dialog = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      if (this.dialog != null) {
         this.dialog.dispose();
      }

      this.impl.actionPerformed(var1);
   }
}
