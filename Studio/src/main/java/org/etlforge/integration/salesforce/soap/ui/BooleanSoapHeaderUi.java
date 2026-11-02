package org.etlforge.integration.salesforce.soap.ui;

import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.label.KongaLabel;

abstract class BooleanSoapHeaderUi implements SoapHeaderUi {
   private final String name;
   private final CheckBoxInputField checkBox;

   public BooleanSoapHeaderUi(String var1, boolean var2) {
      this.name = var1;
      this.checkBox = new CheckBoxInputField(var1, new KongaCheckBox(var2));
   }

   @Override
   public final SoapHeader getDefinedSoapHeader() {
      return this.createHeader(this.checkBox.isSelected());
   }

   protected abstract SoapHeader createHeader(boolean var1);

   @Override
   public final void layoutIn(TwoColumnLayout var1) {
      var1.addRow(new KongaLabel(this.name), this.checkBox.getInputComponent());
   }

   @Override
   public final void addToParent(InputPanel var1) {
      var1.addInputField(this.checkBox);
   }

   @Override
   public final void requestFocus() {
      this.checkBox.requestFocus();
   }
}
