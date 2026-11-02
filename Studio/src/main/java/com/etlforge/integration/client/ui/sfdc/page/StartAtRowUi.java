package com.etlforge.integration.client.ui.sfdc.page;

import org.etlforge.integration.data.binding.ui.LongTextField;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceModifier;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.sfdc.WizardItemInfo;
import org.etlforge.integration.salesforce.StartAtRowProperty;
import org.etlforge.ui.Size;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnLayout;

public final class StartAtRowUi<T extends IntegrationEntity & SalesforceModifier> {
   private final LongTextField field;

   public StartAtRowUi(T var1, InputPanel var2) {
      this.field = LongTextField.builder(var1, "start_at_row")
         .withDefaultValue(StartAtRowProperty.firstRow())
         .withMinValue(StartAtRowProperty.firstRow())
         .withName("Start at data row")
         .withSize(Size.columns(6))
         .withFixedWidth()
         .inPanel(var2)
         .build();
      if (this.isApplicable((T)var1)) {
         this.field.setToolTip("<html>1 = the first data record, 2 = the second data record, etc.<br>The header is not considered part of the data.</html>");
      } else {
         this.field.setEnabled(false);
         this.field.setToolTip("Applicable only when reading from a simple text file");
      }
   }

   private boolean isApplicable(T var1) {
      WizardItemInfo var2 = new WizardItemInfo((SalesforceWizardEntity)var1);
      return var2.readsFromFlatTextFile();
   }

   public void apply() {
      this.field.apply();
   }

   public void layoutIn(TwoColumnLayout var1) {
      this.field.layoutIn(var1);
   }

   public void requestFocus() {
      this.field.requestFocus();
   }

   public boolean isEnabled() {
      return this.field.isEnabled();
   }
}
