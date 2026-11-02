package org.etlforge.integration.client.ui.interchange.location.http;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JLabel;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.button.KongaCheckBox;

final class UseBasicHttpAuthenticationPart implements HttpLocationPanelPart {
   private final CheckBoxInputField checkBox = new CheckBoxInputField("Use Basic HTTP Authentication", new KongaCheckBox("Use &Basic HTTP Authentication"));
   private final JLabel securityWarning;
   private boolean isHttps;

   public UseBasicHttpAuthenticationPart(InputPanel var1) {
      var1.addInputField(this.checkBox);
      this.securityWarning = new JLabel(
         "<html>Using Basic Authentication without SSL encryption is not safe.<br>Your password will be sent in clear text.</html>", CommonIcons.WARNING_24, 10
      );
      this.securityWarning.setVerticalTextPosition(1);
      this.securityWarning.setVisible(false);
      this.checkBox.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent var1) {
            UseBasicHttpAuthenticationPart.this.updateSecurityWarning();
         }
      });
   }

   private void updateSecurityWarning() {
      boolean var1 = this.checkBox.isSelected() && !this.isHttps;
      this.securityWarning.setVisible(var1);
   }

   public void setHttps(boolean var1) {
      this.isHttps = var1;
      this.updateSecurityWarning();
   }

   public void setNonHttpsWarningVisible(boolean var1) {
      this.securityWarning.setVisible(var1);
   }

   public void applyTo(HttpLocation var1) {
      var1.setBasicHttpAuthentication(this.checkBox.isSelected());
   }

   public void reset(HttpLocation var1) {
      this.checkBox.setSelected(var1.getBasicHttpAuthentication());
   }

   @Override
   public void dispose() {
      this.checkBox.dispose();
   }

   @Override
   public void addToLayout(DataLocationPanelLayout var1) {
      OneColumnPanel var2 = Vertical.column().stretchHorizontally().withInsets(0, 0, 6, 0).of(this.checkBox.getInputComponent(), this.securityWarning);
      var1.add(null, var2.container(), true);
   }

   @Override
   public void layout(TwoColumnLayout var1) {
      throw new UnsupportedOperationException();
   }
}
