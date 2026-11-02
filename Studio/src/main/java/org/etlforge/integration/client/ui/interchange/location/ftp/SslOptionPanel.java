package org.etlforge.integration.client.ui.interchange.location.ftp;

import javax.swing.JPanel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.data.location.FtpLocation;
import org.etlforge.integration.data.location.ftp.FtpSecurityOption;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.RadioButtonGroupPanel;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class SslOptionPanel extends InputPanel {
   private final RadioButtonGroupPanel<FtpSecurityOption> buttons = this.createButtons();

   public SslOptionPanel() {
      this.setSendInputChangeEvents(true);
      this.setSendUndoEvents(true);
   }

   public void reset(FtpLocation var1) {
      this.setSelectedOption(var1.getSecurityOption());
   }

   public FtpSecurityOption getSelectedOption() {
      return this.buttons.getSelection();
   }

   public void setSelectedOption(FtpSecurityOption var1) {
      this.buttons.setSelectedValue(var1);
   }

   @Override
   public JPanel getUi() {
      KongaLabel var1 = TextStyles.DefaultBold.makeLabel(Strings.get("Ftp.SSL"));
      UiProvider var2 = this.buttons.getUi(var1, RadioButtonGroupPanel.Layout.ONE_COLUMN);
      return InvisiblePanel.of(var2);
   }

   private RadioButtonGroupPanel<FtpSecurityOption> createButtons() {
      RadioButtonGroupPanel var1 = new RadioButtonGroupPanel(this);
      var1.setUndoRedoPresentationName(Strings.get("Ftp.SSL.Edit"));

      for (FtpSecurityOption var3 : FtpSecurityOption.getSslOptions()) {
         var1.addOption(var3, var3.toString(), false);
      }

      return var1;
   }
}
