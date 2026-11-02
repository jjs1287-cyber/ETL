package org.etlforge.integration.client.ui.interchange.entity.schedule.page.occurrence;

import javax.swing.JLabel;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.IntegerInputField;
import org.etlforge.ui.input.LabelIconFieldDecorator;
import org.etlforge.ui.layout.BoxBuilder;

final class Every_X_Panel extends InputPanel {
   private final String name;
   private IntegerInputField inputField;
   private final int minValue;
   private final int maxValue;
   private JLabel frontLabel;
   private final LabelIconFieldDecorator fieldDecorator;

   public Every_X_Panel(String var1, String var2, String var3, int var4, int var5) {
      this.name = var1;
      this.minValue = var4;
      this.maxValue = var5;
      this.fieldDecorator = new LabelIconFieldDecorator();
      this.fieldDecorator.setHorizontalTextPosition(4);
      this.setOpaque(false);
      this.createComponents(var2, var3);
      this.addInputComponents();
      this.layoutComponents();
   }

   private void createComponents(String var1, String var2) {
      if (var1 != null && var1.length() > 0) {
         this.frontLabel = new JLabel(var1);
      }

      this.inputField = InputFieldFactory.getIntegerInputField(this.name, var2, this.minValue, this.maxValue, 3);
      this.inputField.setSelectAllWhenFocused(true);
   }

   private void addInputComponents() {
      this.addInputField(this.inputField, this.inputField, this.inputField);
   }

   private void layoutComponents() {
      BoxBuilder var1 = BoxBuilder.horizontal(this.getUi());
      var1.addAllWithSpace(3, this.frontLabel, InputFieldFactory.getCapsule(this.inputField, 1));
   }

   public int getValue() throws NumberFormatException {
      return this.inputField.getIntValue(0);
   }

   public void setValue(int var1) {
      this.inputField.setValue(var1);
   }

   public void clearValue() {
      this.inputField.setValue("");
   }

   @Override
   public void setActive(boolean var1) {
      super.setActive(var1);
      if (this.frontLabel != null) {
         this.frontLabel.setEnabled(var1);
      }
   }
}
