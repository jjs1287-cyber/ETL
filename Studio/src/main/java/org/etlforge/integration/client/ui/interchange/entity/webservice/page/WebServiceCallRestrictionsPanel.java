package org.etlforge.integration.client.ui.interchange.entity.webservice.page;

import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.data.CallRestrictions;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.LongInputField;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.util.StorageSizeUnit;

public final class WebServiceCallRestrictionsPanel extends InputPanel {
   private final LongInputField delayField = createDelayField();
   private final LongInputField dataLimitField = createDataLimitField();
   private final JComboBox sizeUnitCombo = new JComboBox<>(StorageSizeUnit.values());

   public WebServiceCallRestrictionsPanel() {
      this(null);
   }

   public WebServiceCallRestrictionsPanel(WebServiceCall var1) {
      this.layoutComponents();
      if (var1 != null) {
         this.setValues(var1);
      }

      this.addInputFields();
   }

   private static LongInputField createDelayField() {
      JLabel var0 = new JLabel(Strings.get("Page.Label.Delay"));
      LongInputField var1 = new LongInputField("Call Delay", new KongaTextField(10), 0L, 2147483647L, var0);
      var1.setRequired(false);
      return var1;
   }

   private static LongInputField createDataLimitField() {
      JLabel var0 = new JLabel(Strings.get("Page.Label.MaxSize"));
      LongInputField var1 = new LongInputField("Data Size", new KongaTextField(10), 0L, 2147483647L, var0);
      var1.setRequired(false);
      return var1;
   }

   private void layoutComponents() {
      TwoColumnPanel var1 = new TwoColumnPanel();
      String var2 = Strings.get("Page.Caption.Restrictions");
      if (var2 != null && var2.length() > 0) {
         var1.addRow(TextStyles.InfoText.makeLabel(var2));
      }

      var1.addRow(InputFieldFactory.getCapsule(this.delayField), null);
      Box var3 = Box.createHorizontalBox();
      var3.add(this.dataLimitField.getInputComponent());
      var3.add(Box.createHorizontalStrut(10));
      var3.add(this.sizeUnitCombo);
      JComponent var4 = InputFieldFactory.getLabeledComponent(var3, this.dataLimitField.getLabel(), 2);
      var1.addRow(var4);
      this.setLayout(new FlowLayout(0));
      var1.addToParent(this.getUi(), null);
      UiUtils.setOpaqueRecursivelySkipDefaults(this.getUi(), false);
   }

   private void addInputFields() {
      this.addInputField(this.delayField, this.delayField, this.delayField);
      this.addInputField(this.dataLimitField, this.dataLimitField, this.dataLimitField);
      this.addInputField(new ComboBoxInputField("Size", this.sizeUnitCombo));
   }

   public void installUndoRedo(UndoRedoInstaller var1) {
      var1.install(this.dataLimitField.getInputComponent());
      var1.install(this.delayField.getInputComponent());
      var1.install(this.sizeUnitCombo);
   }

   public void setValues(WebServiceCall var1) {
      CallRestrictions var2 = var1.getCallRestrictions();
      if (var2 == null) {
         this.dataLimitField.setValue("");
         this.delayField.setValue("");
         this.sizeUnitCombo.setSelectedIndex(0);
      } else {
         StorageSizeUnit var3 = var2.getMaximumSizeUiUnit();
         if (var3 == null) {
            this.dataLimitField.setValue(0);
         } else {
            this.dataLimitField.setValue(var2.getMaximumSizeInBytes() / var3.inBytes());
         }

         this.delayField.setValue(var2.getMinimumCallDelayInMilliSecs());
         this.sizeUnitCombo.setSelectedItem(var3);
      }
   }

   public CallRestrictions getCallRestrictions(WebServiceCall var1) throws IllegalStateException {
      Long var2 = this.getCallDelay();
      StorageSizeUnit var3 = (StorageSizeUnit)this.sizeUnitCombo.getSelectedItem();
      Long var4 = this.getMaxDataToPost(var3);
      CallRestrictions var5 = null;
      if (var2 != null || var4 != null) {
         var5 = new CallRestrictions(var1);
         if (var2 != null) {
            var5.setMinimumCallDelayInMilliSecs(var2);
         }

         if (var4 != null) {
            var5.setMaximumSizeInBytes(var4);
            var5.setMaximumSizeUiUnit(var3);
         }
      }

      return var5;
   }

   private Long getCallDelay() {
      Long var1 = null;
      String var2 = this.delayField.getValue().trim();
      if (var2.length() > 0) {
         var1 = this.delayField.getLongValue();
      }

      return var1;
   }

   private Long getMaxDataToPost(StorageSizeUnit var1) {
      Long var2 = null;
      String var3 = this.dataLimitField.getValue();
      if (var3.length() > 0) {
         var2 = this.dataLimitField.getLongValue() * var1.inBytes();
      }

      return var2;
   }
}
