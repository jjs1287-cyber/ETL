package org.etlforge.integration.salesforce.soap.ui;

import org.etlforge.integration.salesforce.soap.header.QueryOptions;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.ui.Size;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.IntegerInputField;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextField;

public final class QueryOptionsUi implements SoapHeaderUi {
   private final IntegerInputField field;

   public QueryOptionsUi(QueryOptions var1) {
      String var2 = "Batch size";
      this.field = new IntegerInputField(var2, new KongaTextField(Size.columns(6)), 200, 2000, "&" + var2 + ":");
      this.field.setSelectAllWhenFocused(true);
      this.field.setValue(var1.getBatchSize());
   }

   @Override
   public SoapHeader getDefinedSoapHeader() {
      int var1 = this.field.getIntValue(2000);
      return new QueryOptions(var1);
   }

   @Override
   public void layoutIn(TwoColumnLayout var1) {
      var1.addRow(this.field.getLabel(), InvisiblePanel.of(this.field.getInputComponent()));
   }

   @Override
   public void addToParent(InputPanel var1) {
      var1.addInputField(this.field, this.field, this.field);
   }

   public boolean isValid() {
      return this.field.isFieldValid();
   }

   @Override
   public void requestFocus() {
      this.field.requestFocus();
   }
}
