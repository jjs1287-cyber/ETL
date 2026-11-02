package com.etlforge.integration.client.ui.sfdc.bulkload;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.border.Border;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.integration.salesforce.bulkload.BatchFileParams;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.IntegerInputField;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextField;

public final class BatchFileOptionsUi implements UiProvider {
   private final IntegerInputField recordsPerFile;
   private final IntegerInputField charactersPerFile;
   private Border border = StyleSheet.getDefaultTitleBorder("Batch File Settings");

   public BatchFileOptionsUi() {
      this(BatchFileParams.DEFAULT);
   }

   public BatchFileOptionsUi(BatchFileParams var1) {
      this.recordsPerFile = createField("&Records per file:", var1.recordsPerFile);
      this.charactersPerFile = createField("&Characters per file:", var1.charactersPerFile);
   }

   private static IntegerInputField createField(String var0, int var1) {
      KongaLabel var2 = KongaLabel.withMnemonic(var0);
      IntegerInputField var3 = new IntegerInputField(var2.getText(), new KongaTextField(16), 0, Integer.MAX_VALUE, var2);
      var3.setRequired(false);
      var3.setValue(var1);
      var3.setSelectAllWhenFocused(true);
      var3.setTextWhenEmpty("Unlimited");
      return var3;
   }

   public void addToParent(InputPanel var1) {
      var1.addInputField(this.recordsPerFile, this.recordsPerFile, this.recordsPerFile);
      var1.addInputField(this.charactersPerFile, this.charactersPerFile, this.charactersPerFile);
   }

   public BatchFileParams getBatchParams() {
      return new BatchFileParams(this.recordsPerFile.getIntValue(0), this.charactersPerFile.getIntValue(0));
   }

   @Override
   public void decorate(UiCanvas var1) {
      TwoColumnPanel var2 = this.layoutControls();
      String var3 = "Use '0' or leave the field blank if there is no limitation on the value";
      Vertical.borderLayout().of(var3, InvisiblePanel.of(var2)).withGap(5).withBorder(this.border).decorate(var1);
   }

   private TwoColumnPanel layoutControls() {
      TwoColumnPanel var1 = new TwoColumnPanel();
      var1.withLeftFill(GridBagLayoutPanel.Fill.NONE).withRightFill(GridBagLayoutPanel.Fill.NONE);
      var1.withLeftWeights(0.0, 0.0).withRightWeights(1.0, 0.0);
      this.recordsPerFile.layoutIn(var1);
      this.charactersPerFile.layoutIn(var1);
      var1.withRightAnchor(GridBagLayoutPanel.Anchor.EAST);
      var1.addRow(null, new KongaButton(new BatchFileOptionsUi.RestoreDefaultsAction()));
      var1.setOpaque(false, false);
      return var1;
   }

   @Override
   public void requestFocus() {
      this.recordsPerFile.requestFocus();
   }

   public BatchFileOptionsUi withoutBorder() {
      this.border = null;
      return this;
   }

   private class RestoreDefaultsAction extends AbstractAction {
      public RestoreDefaultsAction() {
         super("Restore &defaults");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         BatchFileParams var2 = BatchFileParams.DEFAULT;
         BatchFileOptionsUi.this.recordsPerFile.setValue(var2.recordsPerFile);
         BatchFileOptionsUi.this.charactersPerFile.setValue(var2.charactersPerFile);
      }
   }
}
