package com.etlforge.integration.client.ui.sfdc.query.page;

import com.google.common.collect.ImmutableList;
import com.etlforge.integration.client.ui.sfdc.RetryPolicyEditor;
import com.etlforge.integration.client.ui.sfdc.page.OptionsPanelSection;
import com.etlforge.integration.client.ui.sfdc.page.SoapHeaderEditor;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.binding.ui.BoundPropertyFactory;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.salesforce.soap.ui.SoapHeaderUi;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.button.KongaCheckBox;

final class QueryOperationOptions implements OptionsPanelSection {
   private final InputPanel parent;
   private final CheckBoxInputField queryAllOption;
   private SoapHeaderEditor soapHeaderEditor;
   private RetryPolicyEditor retryPolicyEditor;

   public QueryOperationOptions(SalesforceQuery var1, InputPanel var2) {
      this.parent = var2;
      this.queryAllOption = this.createQueryAllOption(var1, var2);
   }

   private CheckBoxInputField createQueryAllOption(SalesforceQuery var1, InputPanel var2) {
      String var3 = "Include deleted rows";
      CheckBoxInputField var4 = new CheckBoxInputField(var3, new KongaCheckBox(var1.isQueryAll()), var3 + ":");
      var2.addInputField(var4);
      return var4;
   }

   public void setSoapHeaderEditor(SoapHeaderEditor var1) {
      this.soapHeaderEditor = var1;

      for (SoapHeaderUi var3 : var1.getEditors()) {
         var3.addToParent(this.parent);
      }
   }

   public void setRetryPolicyEditor(RetryPolicyEditor var1) {
      this.retryPolicyEditor = var1;
   }

   @Override
   public String getTitle() {
      return "Operation Options";
   }

   @Override
   public void apply() {
   }

   public boolean isQueryAll() {
      return this.queryAllOption.isSelected();
   }

   @Override
   public void layoutIn(TwoColumnPanel var1) {
      var1.addRow(this.queryAllOption.getLabel(), this.queryAllOption.getInputComponent());

      for (SoapHeaderUi var3 : this.soapHeaderEditor.getEditors()) {
         var3.layoutIn(var1);
      }

      if (this.retryPolicyEditor != null) {
         this.retryPolicyEditor.layoutIn(var1);
      }
   }

   @Override
   public void requestFocus() {
      this.queryAllOption.requestFocus();
   }

   public Iterable<BoundProperty> getBoundProperties() {
      BoundProperty var1 = BoundPropertyFactory.toggleProperty("query_all", this.queryAllOption);
      return ImmutableList.of(var1);
   }
}
