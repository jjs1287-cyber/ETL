package com.etlforge.integration.client.ui.sfdc.query.page;

import com.google.common.collect.ImmutableList;
import com.etlforge.integration.client.ui.sfdc.page.OptionsPanelSection;
import com.etlforge.integration.client.ui.sfdc.page.UTF8Option;
import com.etlforge.integration.client.ui.sfdc.page.soql.SoqlModel;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.entity.sfquery.TargetHeaderMode;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnPanel;

final class TargetFileOptions implements OptionsPanelSection {
   private final SalesforceQuery query;
   private final UTF8Option utf8Option;
   private final TargetHeaderModeSelector targetHeaderModeSelector;

   public TargetFileOptions(SalesforceQuery var1, UTF8Option var2, InputPanel var3) {
      this.query = var1;
      this.utf8Option = var2;
      this.targetHeaderModeSelector = new TargetHeaderModeSelector(var1, var3);
   }

   @Override
   public String getTitle() {
      return "File Options";
   }

   @Override
   public void apply() {
      TargetHeaderMode var1 = this.targetHeaderModeSelector.getSelectedMode();
      this.query.setTargetHeaderMode(var1);
   }

   public void bindToSoqlModel(SoqlModel var1) {
      this.targetHeaderModeSelector.bindToSoqlModel(var1);
   }

   @Override
   public void layoutIn(TwoColumnPanel var1) {
      this.utf8Option.layoutIn(var1);
      this.targetHeaderModeSelector.layoutIn(var1);
   }

   @Override
   public void requestFocus() {
      this.utf8Option.requestFocus();
   }

   public Iterable<BoundProperty> getBoundProperties() {
      return ImmutableList.of(this.targetHeaderModeSelector.asBoundProperty());
   }
}
