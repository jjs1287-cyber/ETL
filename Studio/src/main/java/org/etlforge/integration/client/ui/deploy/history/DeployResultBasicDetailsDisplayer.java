package org.etlforge.integration.client.ui.deploy.history;

import javax.swing.JLabel;
import org.etlforge.application.ui.widget.BasicTwoColumnLabelDisplayer;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ui.ServerToolTip;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.resources.UiResourceBundle;

public final class DeployResultBasicDetailsDisplayer implements UiProvider {
   private final BasicTwoColumnLabelDisplayer ui = new BasicTwoColumnLabelDisplayer();

   public DeployResultBasicDetailsDisplayer(DeployResult var1) {
      this.ui.addRow(UiResourceBundle.getString("Date") + ":", var1.getTimestamp());
      this.ui.addRow(Strings.get("Deploy.Result.DeployedTo") + ":", this.getServerLabel(var1));
      this.addUserInfo(var1);
      this.ui.addRow(Strings.get("Deploy.Result.Outcome") + ":", this.getOutcomeLabel(var1));
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.ui.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   private JLabel getServerLabel(DeployResult var1) {
      ServerInfo var2 = var1.getServer();
      JLabel var3 = this.ui.makeValueLabel(var2.getName());
      var3.setIcon(ClientIcons.SERVER_16);
      var3.setToolTipText(new ServerToolTip(var2).get());
      return var3;
   }

   private JLabel getOutcomeLabel(DeployResult var1) {
      JLabel var2 = this.ui.makeValueLabel("");
      OutcomeDecorator var3 = OutcomeDecorator.fromResult(var1);
      var3.decorate(var2);
      return var2;
   }

   private void addUserInfo(DeployResult var1) {
      String var2 = var1.getUser();
      if (var2 == null || var2.length() == 0) {
         var2 = Strings.get("Deploy.Result.UnknownUser");
      }

      this.ui.addRow(Strings.get("Deploy.Result.DeployedAs") + ":", var2, ClientIcons.USER_16);
   }
}
