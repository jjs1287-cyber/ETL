package com.etlforge.integration.client.ui.sfdc.ucu.page;

import com.etlforge.integration.client.ui.sfdc.DateOptionsEditor;
import com.etlforge.integration.client.ui.sfdc.page.OptionsPanelSection;
import org.etlforge.integration.data.entity.sfdc.AbstractSalesforceUCU;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnPanel;

final class SourceDatabaseDateOptions implements OptionsPanelSection {
   private final AbstractSalesforceUCU wizardItem;
   private final DateOptionsEditor dateOptionsEditor;

   public SourceDatabaseDateOptions(InputPanel var1, AbstractSalesforceUCU var2, DateOptionsEditor var3) {
      this.wizardItem = var2;
      this.dateOptionsEditor = var3;
      this.dateOptionsEditor.registerAsChangeSource(var1);
   }

   @Override
   public String getTitle() {
      return "Date Options";
   }

   @Override
   public void apply() {
      this.wizardItem.setDateConversionParams(this.dateOptionsEditor.getParams());
   }

   @Override
   public void layoutIn(TwoColumnPanel var1) {
      this.dateOptionsEditor.layoutInPage(var1);
   }

   @Override
   public void requestFocus() {
   }
}
