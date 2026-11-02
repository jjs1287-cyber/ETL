package com.etlforge.integration.client.ui.sfdc.delete.page;

import com.etlforge.integration.client.ui.sfdc.page.OptionsPanelSection;
import com.etlforge.integration.client.ui.sfdc.page.SourceFileHasHeaderUi;
import com.etlforge.integration.client.ui.sfdc.page.StartAtRowUi;
import com.etlforge.integration.client.ui.sfdc.page.UTF8Option;
import com.etlforge.integration.data.entity.SalesforceDelete;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnPanel;

final class SourceFileOptions implements OptionsPanelSection {
   private final UTF8Option utf8Option;
   private final SourceFileHasHeaderUi<SalesforceDelete> sourceFileHeaderOption;
   private final StartAtRowUi<SalesforceDelete> startAtRowOption;

   public SourceFileOptions(SalesforceDelete var1, InputPanel var2, UTF8Option var3) {
      this.utf8Option = var3;
      this.sourceFileHeaderOption = new SourceFileHasHeaderUi<>(var1, var2);
      this.startAtRowOption = new StartAtRowUi<>(var1, var2);
   }

   @Override
   public String getTitle() {
      return "File Options";
   }

   @Override
   public void apply() {
      this.sourceFileHeaderOption.apply();
      this.startAtRowOption.apply();
   }

   @Override
   public void layoutIn(TwoColumnPanel var1) {
      this.utf8Option.layoutIn(var1);
      this.sourceFileHeaderOption.layoutIn(var1);
      this.startAtRowOption.layoutIn(var1);
   }

   @Override
   public void requestFocus() {
      this.utf8Option.requestFocus();
   }
}
