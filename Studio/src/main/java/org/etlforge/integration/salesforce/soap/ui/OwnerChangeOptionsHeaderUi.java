package org.etlforge.integration.salesforce.soap.ui;

import javax.swing.JLabel;
import org.etlforge.integration.salesforce.soap.header.OwnerChangeOptionsHeader;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.widget.button.KongaCheckBox;

public final class OwnerChangeOptionsHeaderUi implements SoapHeaderUi {
   private final CheckBoxInputField attachments;
   private final CheckBoxInputField openActivities;

   public OwnerChangeOptionsHeaderUi(OwnerChangeOptionsHeader var1) {
      this.attachments = createCheckBox("Transfer attachments", var1.isTransferAttachments());
      this.openActivities = createCheckBox("Transfer open activities", var1.isTransferOpenActivities());
   }

   private static CheckBoxInputField createCheckBox(String var0, boolean var1) {
      KongaCheckBox var2 = new KongaCheckBox(var0, var1);
      return new CheckBoxInputField(var0, var2);
   }

   @Override
   public SoapHeader getDefinedSoapHeader() {
      return new OwnerChangeOptionsHeader(this.attachments.isSelected(), this.openActivities.isSelected());
   }

   @Override
   public void layoutIn(TwoColumnLayout var1) {
      JLabel var2 = new JLabel("Ownership Options");
      var2.setVerticalAlignment(1);
      var1.addRow(var2, this.attachments.getInputComponent());
      var1.addRow(new JLabel(" "), this.openActivities.getInputComponent());
   }

   @Override
   public void requestFocus() {
      this.attachments.requestFocus();
   }

   @Override
   public void addToParent(InputPanel var1) {
      var1.addInputField(this.attachments);
      var1.addInputField(this.openActivities);
   }
}
