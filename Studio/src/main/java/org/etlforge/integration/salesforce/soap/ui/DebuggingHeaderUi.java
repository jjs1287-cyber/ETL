package org.etlforge.integration.salesforce.soap.ui;

import org.etlforge.integration.salesforce.soap.header.DebuggingHeader;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.widget.combobox.EnumComboBox;
import org.etlforge.ui.widget.label.KongaLabel;

public final class DebuggingHeaderUi implements SoapHeaderUi {
   private final ComboBoxInputField dropdown = new ComboBoxInputField("Debug level", EnumComboBox.of(DebuggingHeader.Level.class));

   public DebuggingHeaderUi(DebuggingHeader var1) {
      this.dropdown.setSelectedItem(var1.getLevel());
   }

   @Override
   public SoapHeader getDefinedSoapHeader() {
      return new DebuggingHeader((DebuggingHeader.Level)this.dropdown.getSelectedItem());
   }

   @Override
   public void layoutIn(TwoColumnLayout var1) {
      var1.addRow(new KongaLabel(this.dropdown.getName()), this.dropdown.getInputComponent());
   }

   @Override
   public void addToParent(InputPanel var1) {
      var1.addInputField(this.dropdown);
   }

   @Override
   public void requestFocus() {
      this.dropdown.requestFocus();
   }
}
