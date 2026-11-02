package org.etlforge.integration.client.ui.interchange.location.ftp;

import com.google.common.base.Preconditions;
import javax.swing.JComponent;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.application.ui.widget.TextFieldCompletionSupport;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.FtpLocation;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.text.contentassist.StringValueHistory;
import org.etlforge.ui.text.contentassist.TokenListCompletionSupport;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class HostPathField implements UiProvider {
   private final TextInputField2 field;
   private TokenListCompletionSupport autoCompleteSupport;

   public HostPathField(InputPanel var1, SourceTarget var2, int var3) {
      KongaLabel var4 = KongaLabel.withMnemonic(Strings.get("Ftp.Path.Label"));
      this.field = InputFieldFactory.getTextInputField2(Strings.get("Ftp.Path.Name"), var4, var3, true);
      this.field.setTextWhenEmpty(getTextWhenEmpty(var2));
      var1.addInputField(this.field, this.field, this.field);
   }

   public void installAutoCompletion(StringValueHistory var1) {
      Preconditions.checkNotNull(var1, "history");
      if (this.autoCompleteSupport != null) {
         this.autoCompleteSupport.dispose();
      }

      this.autoCompleteSupport = new TextFieldCompletionSupport(this.field, var1);
   }

   public void dispose() {
      if (this.autoCompleteSupport != null) {
         this.autoCompleteSupport.dispose();
      }

      this.field.dispose();
   }

   public void applyTo(FtpLocation var1) {
      var1.setHostPath(this.field.getValue().trim());
   }

   public void reset(FtpLocation var1) {
      this.field.setValue(var1.getHostPath());
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

   private static String getTextWhenEmpty(SourceTarget var0) {
      return var0 == SourceTarget.Source ? Strings.get("Ftp.Path.Source.Example") : Strings.get("Ftp.Path.Target.Example");
   }
}
