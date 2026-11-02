package org.etlforge.integration.salesforce.soap.ui;

import javax.swing.JLabel;
import org.etlforge.integration.salesforce.soap.header.EmailHeader;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.widget.button.KongaCheckBox;

public final class EmailHeaderUi implements SoapHeaderUi {
   private final CheckBoxInputField autoResponse;
   private final CheckBoxInputField other;
   private final CheckBoxInputField user;

   public EmailHeaderUi(EmailHeader var1) {
      this.autoResponse = createCheckBox("Trigger Auto Response Email", "Trigger auto response email", var1.isTriggerAutoResponseEmail());
      this.other = createCheckBox("Trigger Other Email", "Trigger other email", var1.isTriggerOtherEmail());
      this.user = createCheckBox("Trigger User Email", "Trigger user email", var1.isTriggerUserEmail());
   }

   private static CheckBoxInputField createCheckBox(String var0, String var1, boolean var2) {
      KongaCheckBox var3 = new KongaCheckBox(var1, var2);
      return new CheckBoxInputField(var0, var3);
   }

   @Override
   public SoapHeader getDefinedSoapHeader() {
      return new EmailHeader(this.autoResponse.isSelected(), this.other.isSelected(), this.user.isSelected());
   }

   @Override
   public void layoutIn(TwoColumnLayout var1) {
      JLabel var2 = new JLabel("Email triggers");
      var2.setVerticalAlignment(1);
      var1.addRow(var2, this.autoResponse.getInputComponent());
      var1.addRow(new JLabel(" "), this.other.getInputComponent());
      var1.addRow(new JLabel(" "), this.user.getInputComponent());
   }

   @Override
   public void addToParent(InputPanel var1) {
      var1.addInputField(this.autoResponse);
      var1.addInputField(this.other);
      var1.addInputField(this.user);
   }

   @Override
   public void requestFocus() {
      this.autoResponse.requestFocus();
   }
}
