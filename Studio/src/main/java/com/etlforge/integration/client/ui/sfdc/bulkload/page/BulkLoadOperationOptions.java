package com.etlforge.integration.client.ui.sfdc.bulkload.page;

import com.google.common.base.Optional;
import com.etlforge.integration.client.ui.sfdc.page.OptionsPanelSection;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.salesforce.soap.header.AssignmentRuleHeader;
import org.etlforge.integration.salesforce.soap.ui.AssignmentRuleHeaderUi;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.button.KongaCheckBox;

final class BulkLoadOperationOptions implements OptionsPanelSection {
   private final SalesforceBulkLoad bulkLoad;
   private final BatchParamsEditor batchParamsEditor;
   private final CheckBoxInputField serialMode;
   private final CheckBoxInputField compression;
   private final Optional<AssignmentRuleHeaderUi> assignmentRuleUi;

   public BulkLoadOperationOptions(SalesforceBulkLoad var1, InputPanel var2, BatchParamsEditor var3) {
      this.bulkLoad = var1;
      this.batchParamsEditor = var3;
      this.serialMode = new CheckBoxInputField("Serial mode", new KongaCheckBox(var1.isSerialMode()), "Ser&ial mode");
      this.compression = new CheckBoxInputField("Compress data", new KongaCheckBox(var1.isCompressionEnabled()), "&Compress data");
      this.assignmentRuleUi = this.createAssignmentRuleUi(var1, var2);
      var2.addInputField(this.serialMode);
      var2.addInputField(this.compression);
   }

   private Optional<AssignmentRuleHeaderUi> createAssignmentRuleUi(SalesforceBulkLoad var1, InputPanel var2) {
      if (var1.isAssignmentRuleSupported()) {
         AssignmentRuleHeader var3 = new AssignmentRuleHeader(var1.getAssignmentRuleId(), false);
         AssignmentRuleHeaderUi var4 = new AssignmentRuleHeaderUi(var3);
         var4.addToParent(var2);
         return Optional.of(var4);
      } else {
         return Optional.absent();
      }
   }

   @Override
   public String getTitle() {
      return "Operation Options";
   }

   @Override
   public void apply() {
      this.bulkLoad.setSerialMode(this.serialMode.isSelected());
      this.bulkLoad.setCompressionEnabled(this.compression.isSelected());
      if (this.assignmentRuleUi.isPresent()) {
         this.applyAssignmentRule();
      }
   }

   private void applyAssignmentRule() {
      String var1 = this.assignmentRuleUi.get().getAssignmentRuleId();
      this.bulkLoad.setAssignmentRuleId(var1);
   }

   @Override
   public void layoutIn(TwoColumnPanel var1) {
      this.batchParamsEditor.layoutIn(var1);
      this.serialMode.layoutIn(var1);
      this.compression.layoutIn(var1);
      if (this.assignmentRuleUi.isPresent()) {
         this.assignmentRuleUi.get().layoutIn(var1);
      }
   }

   @Override
   public void requestFocus() {
   }
}
