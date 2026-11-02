package com.etlforge.integration.client.ui.sfdc.ucu.page;

import com.etlforge.integration.client.ui.sfdc.RetryPolicyEditor;
import com.etlforge.integration.client.ui.sfdc.page.ChunkingOptionsAction;
import com.etlforge.integration.client.ui.sfdc.page.ChunkingSettingsEditor;
import com.etlforge.integration.client.ui.sfdc.page.OptionsPanelSection;
import com.etlforge.integration.client.ui.sfdc.page.SoapHeaderEditor;
import javax.swing.JLabel;
import org.etlforge.integration.data.binding.ui.BooleanCheckBox;
import org.etlforge.integration.data.entity.sfdc.AbstractSalesforceUCU;
import org.etlforge.integration.salesforce.soap.ui.SoapHeaderUiCollection;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class UcuOperationOptions implements OptionsPanelSection {
   private final InputPanel parent;
   private final ChunkingOptionsAction chunkingOptionsAction;
   private RetryPolicyEditor retryPolicyEditor;
   private final BooleanCheckBox insertNullsOption;
   private final BooleanCheckBox compressOption;
   private SoapHeaderUiCollection soapHeaderUis;

   public UcuOperationOptions(AbstractSalesforceUCU var1, InputPanel var2) {
      this.parent = var2;
      this.chunkingOptionsAction = new ChunkingOptionsAction();
      this.insertNullsOption = buildCheckBox(var1, "insert_null_values", "Insert null values", var2);
      this.compressOption = buildCheckBox(var1, "compress", "Compress", var2);
   }

   private static BooleanCheckBox buildCheckBox(AbstractSalesforceUCU var0, String var1, String var2, InputPanel var3) {
      return BooleanCheckBox.builder(var0, var1).withName(var2).inPanel(var3).build();
   }

   public void setSoapHeaderEditor(SoapHeaderEditor var1) {
      this.soapHeaderUis = SoapHeaderUiCollection.of(var1.getEditors());
      this.soapHeaderUis.registerAsChangeSource(this.parent);
   }

   public void setChunkingEditor(ChunkingSettingsEditor var1) {
      this.chunkingOptionsAction.setEditor(var1);
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
      this.insertNullsOption.apply();
      this.compressOption.apply();
   }

   @Override
   public void layoutIn(TwoColumnPanel var1) {
      if (this.retryPolicyEditor != null) {
         this.retryPolicyEditor.layoutIn(var1);
      }

      this.layoutChunkingEditor(var1);
      this.insertNullsOption.layoutIn(var1);
      this.compressOption.layoutIn(var1);
      this.soapHeaderUis.layoutIn(var1);
   }

   private void layoutChunkingEditor(TwoColumnLayout var1) {
      KongaButton var2 = new KongaButton(this.chunkingOptionsAction);
      var2.setText("Edit");
      var1.addRow(new JLabel("Chunking settings"), InvisiblePanel.of(var2));
   }

   @Override
   public void requestFocus() {
   }
}
