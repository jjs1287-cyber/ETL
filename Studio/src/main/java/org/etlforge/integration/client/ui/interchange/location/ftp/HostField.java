package org.etlforge.integration.client.ui.interchange.location.ftp;

import com.google.common.base.Preconditions;
import javax.swing.JComponent;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.application.ui.widget.TextFieldCompletionSupport;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.data.location.FtpLocation;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.text.contentassist.StringValueHistory;
import org.etlforge.ui.text.contentassist.TokenListCompletionSupport;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class HostField implements UiProvider {
   private final TextInputField2 field;
   private TokenListCompletionSupport autoCompleteSupport;

   public HostField(InputPanel var1, int var2) {
      KongaLabel var3 = KongaLabel.withMnemonic(Strings.get("Ftp.Host.Label"));
      this.field = InputFieldFactory.getNonEmptyTextInputField(Strings.get("Ftp.Host.Name"), var3, var2, true);
      this.field.setTextWhenEmpty(Strings.get("Ftp.Host.Example"));
      var1.addInputField(this.field, this.field, this.field);
   }

   public void installAutoCompletion(StringValueHistory var1) {
      Preconditions.checkNotNull(var1, "history");
      this.autoCompleteSupport = new TextFieldCompletionSupport(this.field, var1);
   }

   public void dispose() {
      if (this.autoCompleteSupport != null) {
         this.autoCompleteSupport.dispose();
      }

      this.field.dispose();
   }

   public void applyTo(FtpLocation var1) {
      var1.setHost(this.field.getValue().trim());
   }

   public void reset(FtpLocation var1) {
      this.field.setValue(var1.getHost());
   }

   @Override
   public void decorate(UiCanvas var1) {
      JComponent var2 = InputFieldFactory.getCapsule(this.field);
      var1.display(InvisiblePanel.of(var2));
   }

   @Override
   public void requestFocus() {
      this.field.requestFocus();
   }

   public void addToLayout(DataLocationPanelLayout var1) {
      var1.add(this.field);
   }
}
