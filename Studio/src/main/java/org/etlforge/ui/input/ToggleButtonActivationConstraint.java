package org.etlforge.ui.input;

import com.google.common.collect.Maps;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.Action;
import javax.swing.JToggleButton;

public final class ToggleButtonActivationConstraint implements InputFieldActivationConstraint {
   private final JToggleButton toggle;
   private boolean reversed;
   private final Map<InputField, Boolean> boundFields;
   private final List<Action> boundActions;

   public ToggleButtonActivationConstraint(ToggleButtonInputField var1) {
      this(var1.getInputComponent());
   }

   public ToggleButtonActivationConstraint(JToggleButton var1) {
      this.toggle = var1;
      var1.addItemListener(new ToggleButtonActivationConstraint.ToggleListener());
      this.boundFields = Maps.newHashMap();
      this.boundActions = new ArrayList<>();
   }

   public void reverse() {
      this.reversed = true;
   }

   @Override
   public boolean evaluate() {
      return this.reversed ? !this.toggle.isSelected() : this.toggle.isSelected();
   }

   public void bind(InputField var1, boolean var2) {
      this.updateInputFieldState(var1, this.evaluate());
      this.boundFields.put(var1, var2);
   }

   public void bind(Action var1) {
      this.updateActionState(var1, this.evaluate());
      this.boundActions.add(var1);
   }

   private void updateInputFieldState(InputField var1, boolean var2) {
      var1.setEnabled(var2);
      var1.setActive(var2);
   }

   private void updateActionState(Action var1, boolean var2) {
      var1.setEnabled(var2);
   }

   private class ToggleListener implements ItemListener {
      private ToggleListener() {
      }

      @Override
      public void itemStateChanged(ItemEvent var1) {
         boolean var2 = ToggleButtonActivationConstraint.this.evaluate();
         this.updateFields(var2);
         this.updateActions(var2);
      }

      private void updateFields(boolean var1) {
         for (Entry var3 : ToggleButtonActivationConstraint.this.boundFields.entrySet()) {
            ToggleButtonActivationConstraint.this.updateInputFieldState((InputField)var3.getKey(), var1);
            if ((Boolean)var3.getValue()) {
               ((InputField)var3.getKey()).requestFocus();
            }
         }
      }

      private void updateActions(boolean var1) {
         for (Action var3 : ToggleButtonActivationConstraint.this.boundActions) {
            ToggleButtonActivationConstraint.this.updateActionState(var3, var1);
         }
      }
   }
}
