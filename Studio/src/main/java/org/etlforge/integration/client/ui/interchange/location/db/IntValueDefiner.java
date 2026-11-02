package org.etlforge.integration.client.ui.interchange.location.db;

import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.IntegerInputField;
import org.etlforge.ui.widget.label.KongaLabel;

abstract class IntValueDefiner implements DatabaseLocationPanelPart {
   private final IntegerInputField inputField;
   private final int defaultValue;

   protected IntValueDefiner(InputPanel var1, String var2, String var3, int var4, int var5, int var6) {
      this.inputField = InputFieldFactory.getIntegerInputField(var2, KongaLabel.withMnemonic(var3), var4, var5, 10, true);
      this.inputField.setSelectAllWhenFocused(true);
      var1.addInputField(this.inputField, this.inputField, this.inputField);
      this.defaultValue = var6;
   }

   @Override
   public final void addToLayout(DatabaseLocationPanelLayout var1) {
      var1.add(InputFieldFactory.getCapsule(this.inputField));
   }

   @Override
   public final void addToLayout(DataLocationPanelLayout var1) {
      var1.add(this.inputField.getLabel(), this.inputField.getInputComponent(), false);
   }

   public final void setEnabled(boolean var1) {
      this.inputField.setEnabled(var1);
      this.inputField.setActive(var1);
   }

   public final void applyTo(DatabaseLocation var1) {
      int var2 = this.inputField.getIntValue(this.defaultValue);
      this.applyValue(var1, var2);
   }

   protected abstract void applyValue(DatabaseLocation var1, int var2);

   @Override
   public final void dispose() {
      this.inputField.dispose();
   }

   protected final void setValue(int var1) {
      this.inputField.setValue(var1);
   }

   protected final int getValue() {
      return this.inputField.getIntValue(this.defaultValue);
   }
}
