package org.etlforge.integration.client.server.ui.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.IntegerInputField;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.property.BooleanBinding;
import org.etlforge.ui.property.EnablableBinding;
import org.etlforge.ui.property.TextBinding;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextField;

final class PortPanel {
   private final LoginModel model;
   private final JCheckBox useDefaultPortBox;
   private final IntegerInputField portNumberField;

   public PortPanel(LoginModel var1) {
      this.model = var1;
      this.useDefaultPortBox = new KongaCheckBox();
      this.portNumberField = this.createPortField();
      this.useDefaultPortBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            if (!PortPanel.this.useDefaultPortBox.isSelected()) {
               PortPanel.this.portNumberField.requestFocus();
            }
         }
      });
      this.bindModel();
   }

   private void bindModel() {
      BooleanBinding.bindAndSyncUi(this.model, "useDefaultPort", this.useDefaultPortBox);
      TextBinding.bindAndSyncUi(this.model, "port", this.portNumberField.getInputComponent());
      EnablableBinding var1 = EnablableBinding.bind(this.model, "useDefaultPort", this.portNumberField);
      var1.reverse();
   }

   private IntegerInputField createPortField() {
      KongaTextField var1 = new KongaTextField(6);
      var1.setFixedSize(true);
      return new IntegerInputField("Port", var1, 1, 65536, new JLabel(PackageResources.PortPanel.MANUAL));
   }

   public JComponent getDisplayer() {
      return this.createDisplayer();
   }

   private JComponent createDisplayer() {
      JLabel var1 = new JLabel(PackageResources.PortPanel.DEFAULT);
      CheckBoxInputField var2 = new CheckBoxInputField("", this.useDefaultPortBox, var1);
      TwoColumnPanel var3 = new TwoColumnPanel();
      var3.addRow(var2.getLabel(), var2.getInputComponent());
      var3.addRow(this.portNumberField.getLabel(), this.portNumberField.getInputComponent());
      JPanel var4 = InvisiblePanel.of(var3);
      String var5 = PackageResources.PortPanel.TITLE;
      var4.setBorder(StyleSheet.getDefaultTitleBorder(var5));
      return var4;
   }
}
