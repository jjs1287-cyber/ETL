package com.etlforge.integration.client.ui.sfdc.bulkload.page;

import com.etlforge.integration.client.ui.sfdc.page.OptionsPanelSection;
import com.etlforge.integration.client.ui.sfdc.page.UTF8Option;
import org.etlforge.ui.layout.TwoColumnPanel;

final class TargetFileOptions implements OptionsPanelSection {
   private final UTF8Option utf8Option;

   public TargetFileOptions(UTF8Option var1) {
      this.utf8Option = var1;
   }

   public UTF8Option getUtf8Option() {
      return this.utf8Option;
   }

   @Override
   public String getTitle() {
      return "File Options";
   }

   @Override
   public void apply() {
   }

   @Override
   public void layoutIn(TwoColumnPanel var1) {
      this.utf8Option.layoutIn(var1);
   }

   @Override
   public void requestFocus() {
      this.utf8Option.requestFocus();
   }
}
