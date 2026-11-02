package com.etlforge.integration.client.ui.sfdc.bulkload.page;

import com.etlforge.integration.client.ui.sfdc.DateOptionsEditor;
import com.etlforge.integration.client.ui.sfdc.page.OptionsPanelSection;
import com.etlforge.integration.client.ui.sfdc.page.StartAtRowUi;
import com.etlforge.integration.client.ui.sfdc.page.UTF8Option;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnPanel;

final class SourceFileOptions implements OptionsPanelSection {
   private final SalesforceBulkLoad bulkLoad;
   private final DateOptionsEditor dateOptionsEditor;
   private final UTF8Option utf8Option;
   private final StartAtRowUi<SalesforceBulkLoad> startAtRowOption;

   public SourceFileOptions(SalesforceBulkLoad var1, InputPanel var2, UTF8Option var3, DateOptionsEditor var4) {
      this.bulkLoad = var1;
      this.utf8Option = var3;
      this.startAtRowOption = new StartAtRowUi<>(var1, var2);
      this.dateOptionsEditor = var4;
      var4.registerAsChangeSource(var2);
   }

   @Override
   public String getTitle() {
      return "File Options";
   }

   @Override
   public void apply() {
      this.bulkLoad.setDateConversionParams(this.dateOptionsEditor.getParams());
      this.startAtRowOption.apply();
   }

   @Override
   public void layoutIn(TwoColumnPanel var1) {
      this.utf8Option.layoutIn(var1);
      this.dateOptionsEditor.layoutInPage(var1);
      this.startAtRowOption.layoutIn(var1);
   }

   @Override
   public void requestFocus() {
      this.utf8Option.requestFocus();
   }
}
