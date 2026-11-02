package org.etlforge.integration.salesforce.soap.ui;

import javax.swing.JLabel;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.widget.text.KongaTextField;

abstract class StringValueHeaderUi implements SoapHeaderUi {
   private final TextInputField field;

   protected StringValueHeaderUi(String var1, String var2) {
      this.field = new TextInputField(var1, new KongaTextField(var2, 20), var1);
      this.field.setSelectAllWhenFocused(true);
      this.field.setAutoTrim(true);
   }

   @Override
   public final SoapHeader getDefinedSoapHeader() {
      return this.createHeader(this.field.getValue());
   }

   protected abstract SoapHeader createHeader(String var1);

   @Override
   public final void layoutIn(TwoColumnLayout var1) {
      var1.addRow(this.field.getLabel(), this.field.getInputComponent());
   }

   public final void layoutVerticallly(BoxBuilder var1) {
      var1.addAllWithSpace(2, this.field.getLabel(), this.field.getInputComponent());
   }

   protected final JLabel getLabel() {
      return this.field.getLabel();
   }

   protected final KongaTextField getTextField() {
      return (KongaTextField)this.field.getInputComponent();
   }

   @Override
   public final void addToParent(InputPanel var1) {
      var1.addInputField(this.field);
   }

   @Override
   public final void requestFocus() {
      this.field.requestFocus();
   }

   public final void setEnabled(boolean var1) {
      this.field.setEnabled(var1);
   }

   public final boolean isEnabled() {
      return this.field.isEnabled();
   }
}
