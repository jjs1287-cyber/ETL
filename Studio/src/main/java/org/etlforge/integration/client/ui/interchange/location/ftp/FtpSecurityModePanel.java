package org.etlforge.integration.client.ui.interchange.location.ftp;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.data.location.FtpLocation;
import org.etlforge.integration.data.location.ftp.FtpMode;
import org.etlforge.integration.data.location.ftp.FtpSecurityOption;
import org.etlforge.integration.data.location.ftp.FtpsAuthenticationMode;
import org.etlforge.integration.data.location.ftp.FtpsExplicitness;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.CardPanel;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.border.BorderBuilder;

public final class FtpSecurityModePanel extends InputPanel {
   private static final String HIDE_EXTRA_OPTIONS = "hide";
   private static final String SHOW_EXTRA_OPTIONS = "show";
   private final FtpLocation location;
   private final ComboBoxInputField modeSelector;
   private final SslOptionPanel sslOptionsPanel;
   private final FtpsAuthenticationPanel authenticationPanel;
   private final CardPanel extraOptionsContainer;

   public FtpSecurityModePanel(FtpLocation var1) {
      this.location = var1;
      this.modeSelector = this.createModeSelector();
      this.sslOptionsPanel = new SslOptionPanel();
      this.authenticationPanel = new FtpsAuthenticationPanel();
      this.extraOptionsContainer = new CardPanel();
      this.extraOptionsContainer.add(new JPanel(), "hide");
      this.layoutExtraOptions();
      this.addInputPanel(this.sslOptionsPanel);
      this.addInputPanel(this.authenticationPanel);
      this.layout();
      this.setSendInputChangeEvents(true);
      this.setSendUndoEvents(true);
      this.handleNewMode(var1.getFtpMode());
   }

   public void apply(FtpLocation var1) {
      switch (this.getSelectedMode()) {
         case UNSECURE:
            var1.setSecurityOption(FtpSecurityOption.NONE);
            break;
         case SFTP:
            var1.setSecurityOption(FtpSecurityOption.SFTP);
            break;
         case IMPLICIT_FTPS:
            var1.setFtpsExplicitness(FtpsExplicitness.IMPLICIT);
            var1.setFtpsAuthenticationMode(FtpsAuthenticationMode.PREFER_SSL);
            var1.setSecurityOption(FtpSecurityOption.REQUIRE_SSL_FOR_ALL);
            break;
         case EXPLICIT_FTPS:
            var1.setFtpsExplicitness(FtpsExplicitness.EXPLICIT);
            var1.setSecurityOption(this.sslOptionsPanel.getSelectedOption());
            var1.setFtpsAuthenticationMode(this.authenticationPanel.getSelectedOption());
      }
   }

   public boolean isSFTP() {
      return this.getSelectedMode() == FtpMode.SFTP;
   }

   public void reset(FtpLocation var1) {
      FtpMode var2 = var1.getFtpMode();
      this.modeSelector.setSelectedItem(var2);
      this.sslOptionsPanel.reset(var1);
      this.authenticationPanel.reset(var1);
   }

   private ComboBoxInputField createModeSelector() {
      JComboBox var1 = new JComboBox<>(FtpMode.values());
      var1.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent var1) {
            FtpSecurityModePanel.this.handleNewMode(FtpSecurityModePanel.this.getSelectedMode());
         }
      });
      ComboBoxInputField var2 = new ComboBoxInputField(Strings.get("Ftp.Security.Selector"), var1);
      this.addInputField(var2);
      return var2;
   }

   private FtpMode getSelectedMode() {
      return (FtpMode)this.modeSelector.getSelectedItem();
   }

   private void handleNewMode(FtpMode var1) {
      String var2;
      if (var1 == FtpMode.EXPLICIT_FTPS) {
         var2 = "show";
         if (this.sslOptionsPanel.getSelectedOption() == null) {
            FtpSecurityOption var3 = this.location.getSecurityOption();
            if (var3 == FtpSecurityOption.NONE || var3 == FtpSecurityOption.SFTP) {
               var3 = FtpSecurityOption.REQUIRE_SSL_FOR_ALL;
            }

            this.sslOptionsPanel.setSelectedOption(var3);
         }

         if (this.authenticationPanel.getSelectedOption() == null) {
            this.authenticationPanel.setSelectedOption(this.location.getFtpsAuthenticationMode());
         }
      } else {
         var2 = "hide";
      }

      this.extraOptionsContainer.show(var2);
   }

   private void layout() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.withInsets(0, 0, 10, 0);
      var1.add(TextStyles.SmallBoldCaption.makeLabel(Strings.get("Ftp.Security")));
      var1.noFill(this.modeSelector.getInputComponent());
      var1.fillOut(this.extraOptionsContainer);
      this.wrap(var1);
   }

   private void layoutExtraOptions() {
      BoxBuilder var1 = Horizontal.box()
         .topAligned()
         .addAllWithSpace(20, this.sslOptionsPanel.getUi(), this.authenticationPanel.getUi())
         .withBorder(new BorderBuilder().etched().empty(5));
      this.extraOptionsContainer.add(var1.invisible(), "show");
   }
}
