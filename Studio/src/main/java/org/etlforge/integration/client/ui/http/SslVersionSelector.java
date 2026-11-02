package org.etlforge.integration.client.ui.http;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.integration.http.SslVersion;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.combobox.EnumComboBox;

final class SslVersionSelector implements UiProvider {
   private final EnumComboBox<SslVersion> comboBox = EnumComboBox.of(SslVersion.getSupportedVersions());

   public SslVersionSelector(SslVersion var1) {
      ImmutableMap var2 = ImmutableMap.of(SslVersion.TLSV1_SSLV3, "Negotiate", SslVersion.TLSV1_ONLY, "Use TLSv1", SslVersion.SSLV3_ONLY, "Use SSLv3");
      this.comboBox.setDisplayStrings(var2);
      this.setSelectedVersion(var1);
   }

   public SslVersion getSelectedVersion() {
      return this.comboBox.getSelectedItem();
   }

   public void setSelectedVersion(SslVersion var1) {
      Preconditions.checkArgument(var1 != null && var1.isSupported(), "Unsupported version: " + var1);
      this.comboBox.setSelectedItem(var1);
   }

   public void registerAsChangeSource(InputPanel var1) {
      ComboBoxInputField var2 = new ComboBoxInputField("SSL Version", this.comboBox);
      var1.addInputField(var2);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 5)
         .north("Only applicable when using HTTPS")
         .south(this.comboBox)
         .withBorder(StyleSheet.getDefaultTitleBorder("SSL Version"));
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.comboBox.requestFocusInWindow();
   }
}
