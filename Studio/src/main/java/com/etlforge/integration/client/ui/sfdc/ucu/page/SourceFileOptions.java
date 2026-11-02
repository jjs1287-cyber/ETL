package com.etlforge.integration.client.ui.sfdc.ucu.page;

import com.etlforge.integration.client.ui.sfdc.DateOptionsEditor;
import com.etlforge.integration.client.ui.sfdc.page.OptionsPanelSection;
import com.etlforge.integration.client.ui.sfdc.page.SourceFileHasHeaderUi;
import com.etlforge.integration.client.ui.sfdc.page.StartAtRowUi;
import com.etlforge.integration.client.ui.sfdc.page.UTF8Option;
import org.etlforge.integration.data.entity.sfdc.AbstractSalesforceUCU;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnPanel;

final class SourceFileOptions implements OptionsPanelSection {
   private final InputPanel parent;
   private final AbstractSalesforceUCU wizardItem;
   private final UTF8Option utf8Option;
   private DateOptionsEditor dateOptionsEditor;
   private final SourceFileHasHeaderUi<AbstractSalesforceUCU> sourceFileHeaderOption;
   private final StartAtRowUi<AbstractSalesforceUCU> startAtRowOption;

   public SourceFileOptions(AbstractSalesforceUCU var1, InputPanel var2, UTF8Option var3) {
      this.parent = var2;
      this.wizardItem = var1;
      this.utf8Option = var3;
      this.sourceFileHeaderOption = new SourceFileHasHeaderUi<>(var1, var2);
      this.startAtRowOption = new StartAtRowUi<>(var1, var2);
   }

   public void setDateOptionsEditor(DateOptionsEditor var1) {
      this.dateOptionsEditor = var1;
      var1.registerAsChangeSource(this.parent);
   }

   @Override
   public String getTitle() {
      return "File Options";
   }

   @Override
   public void apply() {
      if (this.dateOptionsEditor != null) {
         this.wizardItem.setDateConversionParams(this.dateOptionsEditor.getParams());
      }

      this.sourceFileHeaderOption.apply();
      this.startAtRowOption.apply();
   }

   @Override
   public void layoutIn(TwoColumnPanel var1) {
      this.utf8Option.layoutIn(var1);
      if (this.dateOptionsEditor != null) {
         this.dateOptionsEditor.layoutInPage(var1);
      }

      this.sourceFileHeaderOption.layoutIn(var1);
      this.startAtRowOption.layoutIn(var1);
   }

   @Override
   public void requestFocus() {
      this.utf8Option.requestFocus();
   }
}
