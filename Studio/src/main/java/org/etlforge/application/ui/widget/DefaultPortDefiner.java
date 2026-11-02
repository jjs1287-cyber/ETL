package org.etlforge.application.ui.widget;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.AbstractDocument;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.IntegerInputField;
import org.etlforge.ui.input.ToggleButtonActivationConstraint;
import org.etlforge.ui.layout.Flow;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.IntegerRangeFilter;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.util.net.Port;

public final class DefaultPortDefiner implements UiProvider {
   private final CheckBoxInputField defaultActivator = this.createActivator();
   private final IntegerInputField portField = this.createPortField();

   public DefaultPortDefiner() {
      this.addActivationConstraint();
   }

   public void registerAsChangeSource(InputPanel var1) {
      var1.addInputField(this.defaultActivator);
      var1.addInputField(this.portField, this.portField, this.portField);
   }

   public void setEnabled(boolean var1) {
      this.defaultActivator.setEnabled(var1);
      this.portField.setEnabled(var1 && this.portField.isActive());
   }

   public void setPort(Port var1) {
      if (var1 == Port.DEFAULT) {
         this.setDefaultPort();
      } else {
         this.setNonDefaultPort(var1);
      }
   }

   public Port getPort() {
      if (this.defaultActivator.isSelected()) {
         return Port.DEFAULT;
      }

      int var1 = this.portField.getIntValue(-1);
      return Port.valueOf(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      Flow var2 = Flow.invisible();
      JComponent[] var3 = this.getComponentsForLayout();
      var2.add(var3[0]).strut(10).add(var3[1]).glue();
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      if (this.defaultActivator.isSelected()) {
         this.defaultActivator.requestFocus();
      } else {
         this.portField.requestFocus();
      }
   }

   public void dispose() {
      this.defaultActivator.dispose();
      this.portField.dispose();
   }

   private JComponent[] getComponentsForLayout() {
      JComponent var1 = InputFieldFactory.getCapsule(this.defaultActivator, 0);
      JComponent var2 = InputFieldFactory.getCapsule(this.portField, 0);
      return new JComponent[]{var1, var2};
   }

   private void setNonDefaultPort(Port var1) {
      this.defaultActivator.setSelected(false);
      if (var1 == Port.INVALID) {
         this.portField.setValue("");
      } else {
         this.portField.setValue(var1.getPort());
      }
   }

   private void setDefaultPort() {
      this.defaultActivator.setSelected(true);
      this.portField.setValue("");
   }

   private CheckBoxInputField createActivator() {
      KongaCheckBox var1 = new KongaCheckBox(true);
      return new CheckBoxInputField("Port", var1, KongaLabel.withMnemonic("&Use default port"));
   }

   private IntegerInputField createPortField() {
      String var1 = "Port";
      KongaTextField var2 = new KongaTextField(6);
      IntegerRangeFilter var3 = new IntegerRangeFilter(Port.VALID_PORT_RANGE);
      ((AbstractDocument)var2.getDocument()).setDocumentFilter(var3);
      return new IntegerInputField(var1, var2, 1, 65536, new JLabel(var1));
   }

   private void addActivationConstraint() {
      ToggleButtonActivationConstraint var1 = new ToggleButtonActivationConstraint(this.defaultActivator);
      var1.reverse();
      var1.bind(this.portField, true);
      this.portField.addActivationConstraint(var1);
   }
}
