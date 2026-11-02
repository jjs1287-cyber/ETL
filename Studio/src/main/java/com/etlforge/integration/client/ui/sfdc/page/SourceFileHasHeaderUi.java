package com.etlforge.integration.client.ui.sfdc.page;

import org.etlforge.integration.data.binding.ui.BooleanCheckBox;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceModifier;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.sfdc.WizardItemInfo;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnLayout;

public final class SourceFileHasHeaderUi<T extends IntegrationEntity & SalesforceModifier> {
   private final BooleanCheckBox checkBox;

   public SourceFileHasHeaderUi(T var1, InputPanel var2) {
      this.checkBox = BooleanCheckBox.builder(var1, "source_file_has_header").withName("Source file has header").inPanel(var2).build();
      if (!this.isApplicable((T)var1)) {
         this.checkBox.setEnabled(false);
         this.checkBox.setToolTip("Applicable only when reading from a simple text file");
      }
   }

   private boolean isApplicable(T var1) {
      WizardItemInfo var2 = new WizardItemInfo((SalesforceWizardEntity)var1);
      return var2.readsFromFlatTextFile();
   }

   public void apply() {
      this.checkBox.apply();
   }

   public void layoutIn(TwoColumnLayout var1) {
      this.checkBox.layoutIn(var1);
   }

   public void requestFocus() {
      this.checkBox.requestFocus();
   }

   public boolean isEnabled() {
      return this.checkBox.isEnabled();
   }
}
