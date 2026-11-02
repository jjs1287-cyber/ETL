package org.etlforge.integration.client.ui.interchange.location.ftp;

import javax.swing.JPanel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.data.location.FtpLocation;
import org.etlforge.integration.data.location.ftp.FtpsAuthenticationMode;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.RadioButtonGroupPanel;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

class FtpsAuthenticationPanel extends InputPanel {
   private final RadioButtonGroupPanel<FtpsAuthenticationMode> buttons = this.createButtons();

   public FtpsAuthenticationPanel() {
      this.setSendInputChangeEvents(true);
      this.setSendUndoEvents(true);
   }

   public void reset(FtpLocation var1) {
      this.setSelectedOption(var1.getFtpsAuthenticationMode());
   }

   public FtpsAuthenticationMode getSelectedOption() {
      FtpsAuthenticationMode var1 = this.buttons.getSelection();
      if (var1 == null) {
         var1 = FtpsAuthenticationMode.defaultValue();
      }

      return var1;
   }

   public void setSelectedOption(FtpsAuthenticationMode var1) {
      this.buttons.setSelectedValue(var1);
   }

   @Override
   public JPanel getUi() {
      KongaLabel var1 = TextStyles.DefaultBold.makeLabel(Strings.get("Ftp.Auth"));
      UiProvider var2 = this.buttons.getUi(var1, RadioButtonGroupPanel.Layout.ONE_COLUMN);
      return InvisiblePanel.of(var2);
   }

   private RadioButtonGroupPanel<FtpsAuthenticationMode> createButtons() {
      RadioButtonGroupPanel var1 = new RadioButtonGroupPanel(this);
      var1.setUndoRedoPresentationName(Strings.get("Ftp.Auth.Edit"));

      for (FtpsAuthenticationMode var5 : FtpsAuthenticationMode.values()) {
         var1.addOption(var5, var5.toString(), false);
      }

      return var1;
   }
}
