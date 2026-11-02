package com.etlforge.integration.client.ui.sfdc.wizard;

import com.etlforge.integration.client.sfdc.data.RetryPolicy;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.border.TitledBorder;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.IntegerInputField;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextField;

public final class RetryPolicyUi implements UiProvider {
   private final CheckBoxInputField suppressor = this.createSuppressor();
   private final IntegerInputField numberOfRetriesField = createTextField("Number of retries", "Number of &retries:", 0);
   private final IntegerInputField intervalField = createTextField("Interval", "Retry &interval (in seconds):", 0);

   public RetryPolicyUi() {
      InputChangeListener var1 = new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1) {
            RetryPolicyUi.this.updateFieldStates();
         }
      };
      this.suppressor.addInputChangeListener(var1);
      this.numberOfRetriesField.addInputChangeListener(var1);
      this.updateFieldStates();
   }

   private CheckBoxInputField createSuppressor() {
      CheckBoxInputField var1 = new CheckBoxInputField("Suppressor", new KongaCheckBox("Do not &use retries"));
      configureFieldToSendEvents(var1);
      var1.getInputComponent().addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            if (!((JCheckBox)var1.getSource()).isSelected()) {
               RetryPolicyUi.this.numberOfRetriesField.requestFocus();
            }
         }
      });
      return var1;
   }

   private static void configureFieldToSendEvents(InputField var0) {
      var0.setSendInputChangeEvents(true);
      var0.setSendUndoEvents(true);
   }

   private static IntegerInputField createTextField(String var0, String var1, int var2) {
      IntegerInputField var3 = new IntegerInputField(var0, new KongaTextField(6), 0, Integer.MAX_VALUE, KongaLabel.withMnemonic(var1));
      var3.setValue(var2);
      var3.setSelectAllWhenFocused(true);
      configureFieldToSendEvents(var3);
      return var3;
   }

   private void updateFieldStates() {
      boolean var1 = !this.suppressor.isSelected();
      this.numberOfRetriesField.setEnabled(var1);
      this.intervalField.setEnabled(var1 && this.getNumberOfRetries() > 0);
   }

   private int getNumberOfRetries() {
      return this.numberOfRetriesField.getIntValue(0);
   }

   public boolean isInputValid() {
      if (this.suppressor.isSelected()) {
         return true;
      } else {
         return !this.numberOfRetriesField.isFieldValid() ? false : this.intervalField.isFieldValid() || !this.intervalField.isEnabled();
      }
   }

   public void setRetryPolicy(RetryPolicy var1) {
      if (var1 == RetryPolicy.NONE) {
         this.suppressor.setSelected(true);
         this.numberOfRetriesField.setValue(0);
         this.intervalField.setValue(0);
      } else {
         this.suppressor.setSelected(false);
         this.numberOfRetriesField.setValue(var1.numberOfRetries);
         this.intervalField.setValue(var1.retryIntervalInSeconds);
      }

      this.updateFieldStates();
   }

   public RetryPolicy getRetryPolicy() {
      if (this.suppressor.isSelected()) {
         return RetryPolicy.NONE;
      }

      if (this.isInputValid()) {
         int var1 = this.getNumberOfRetries();
         if (var1 > 0) {
            return new RetryPolicy(var1, this.getRetryInterval());
         }
      }

      return RetryPolicy.NO_RETRIES;
   }

   private int getRetryInterval() {
      return this.intervalField.getIntValue(0);
   }

   public void addChangeListener(InputChangeListener var1) {
      this.suppressor.addInputChangeListener(var1);
      this.numberOfRetriesField.addInputChangeListener(var1);
      this.intervalField.addInputChangeListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      TwoColumnPanel var2 = emptyGrid();
      var2.addRow(this.numberOfRetriesField);
      var2.addRow(this.intervalField);
      CaptionLayout.of(this.suppressor.getInputComponent(), var2).decorate(var1);
   }

   private static TwoColumnPanel emptyGrid() {
      byte var0 = 5;
      return new TwoColumnPanel().withLeftInsets(new Insets(0, 0, var0, 5)).withRightInsets(0, 0, var0, 0).withBorder(new TitledBorder(""));
   }

   @Override
   public void requestFocus() {
      if (this.suppressor.isSelected()) {
         this.suppressor.requestFocus();
      } else {
         this.numberOfRetriesField.requestFocus();
      }
   }
}
