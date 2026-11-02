package org.etlforge.integration.client.ui.operation.options;

import javax.swing.JLabel;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.data.entity.operation.options.ChunkingOption;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.IntegerInputField;
import org.etlforge.ui.input.ToggleButtonActivationConstraint;
import org.etlforge.ui.layout.DialogLayoutBuilder;
import org.etlforge.ui.layout.TwoColumnLayout;

public final class ChunkSizeAndThreadCountUi implements UiProvider {
   private final ChunkingOption originalValue;
   private final IntegerInputField chunkSizeField;
   private final IntegerInputField targetChunkSizeField;
   private final JLabel targetChunkSizeLabel;
   private final IntegerInputField maxThreadsField;

   public ChunkSizeAndThreadCountUi(ChunkingOption var1) {
      this.originalValue = var1;
      this.chunkSizeField = this.createValueField(var1);
      this.targetChunkSizeField = this.createTargetChunkSizeField(var1);
      this.maxThreadsField = this.createMaxThreadsField(var1);
      this.targetChunkSizeLabel = new JLabel("(0 means all records in one file)");
   }

   private IntegerInputField createValueField(ChunkingOption var1) {
      String var2 = "Chunk size:";
      String var3 = "size";
      byte var4 = 0;
      int var5 = var1.getChunkSize();
      return this.createField(var2, var3, var4, var5);
   }

   private IntegerInputField createTargetChunkSizeField(ChunkingOption var1) {
      String var2 = "Number of Records Per File:";
      String var3 = "targetsize";
      byte var4 = 0;
      int var5 = var1.getTargetChunkSize();
      return this.createField(var2, var3, var4, var5);
   }

   private IntegerInputField createField(String var1, String var2, int var3, int var4) {
      IntegerInputField var5 = InputFieldFactory.getIntegerInputField(var2, var1, var3, Integer.MAX_VALUE, 5);
      var5.setValue(var4);
      var5.setSelectAllWhenFocused(true);
      return var5;
   }

   private IntegerInputField createMaxThreadsField(ChunkingOption var1) {
      String var2 = "Max number of threads:";
      String var3 = "threads";
      byte var4 = 1;
      int var5 = var1.getMaxNumberOfThreads();
      return this.createField(var2, var3, var4, var5);
   }

   public ChunkingOption getDefinedOption() {
      return new ChunkingOption(
         this.getChunkSize(),
         this.getTargetChunkSize(),
         this.getNumberOfThreads(),
         this.originalValue.getSourceChunkNode(),
         this.originalValue.getTargetChunkNode()
      );
   }

   public void registerAsChangeSource(InputPanel var1) {
      var1.addInputField(this.targetChunkSizeField, this.targetChunkSizeField, this.targetChunkSizeField);
      var1.addInputField(this.chunkSizeField, this.chunkSizeField, this.chunkSizeField);
      var1.addInputField(this.maxThreadsField, this.maxThreadsField, this.maxThreadsField);
   }

   public void setChunkSizeInterval(int var1, int var2) {
      this.chunkSizeField.setMinimumValue(var1);
      this.chunkSizeField.setMaximumValue(var2);
      this.chunkSizeField.decorateField();
   }

   public void setTargetChunkSizeInterval(int var1, int var2) {
      this.targetChunkSizeField.setMinimumValue(var1);
      this.targetChunkSizeField.setMaximumValue(var2);
      this.targetChunkSizeField.decorateField();
   }

   public int getChunkSize() {
      return this.chunkSizeField.getIntValue(0);
   }

   public int getTargetChunkSize() {
      return this.targetChunkSizeField.getIntValue(0);
   }

   public int getNumberOfThreads() {
      return this.maxThreadsField.getIntValue(1);
   }

   public void layoutIn(TwoColumnLayout var1) {
      this.chunkSizeField.layoutIn(var1);
      this.targetChunkSizeField.layoutIn(var1);
      var1.addRow(new JLabel(), this.targetChunkSizeLabel);
      this.maxThreadsField.layoutIn(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      DialogLayoutBuilder var2 = new DialogLayoutBuilder();
      this.layoutIn(var2);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.chunkSizeField.requestFocus();
   }

   void bindEnabling(ToggleButtonActivationConstraint var1) {
      var1.bind(this.chunkSizeField, true);
      this.chunkSizeField.addActivationConstraint(var1);
      var1.bind(this.targetChunkSizeField, true);
      this.targetChunkSizeField.addActivationConstraint(var1);
      var1.bind(this.maxThreadsField, false);
      this.maxThreadsField.addActivationConstraint(var1);
   }

   void setEnabled(boolean var1) {
      this.chunkSizeField.setEnabled(var1);
      this.targetChunkSizeField.setEnabled(var1);
      this.targetChunkSizeLabel.setEnabled(var1);
      this.maxThreadsField.setEnabled(var1);
   }

   void setToolTip(String var1) {
      this.chunkSizeField.getInputComponent().setToolTipText(var1);
      this.targetChunkSizeField.getInputComponent().setToolTipText(var1);
      this.maxThreadsField.getInputComponent().setToolTipText(var1);
   }
}
