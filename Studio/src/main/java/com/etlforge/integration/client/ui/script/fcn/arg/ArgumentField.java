package com.etlforge.integration.client.ui.script.fcn.arg;

import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.ui.script.text.VariableField;
import org.etlforge.integration.data.script.ArgumentDescriptor;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;

final class ArgumentField {
   private final VariableField field;
   private final TextInputField2 inputField;
   private final ArgumentDescriptor descriptor;

   public ArgumentField(VariableField var1, ArgumentDescriptor var2) {
      this.field = var1;
      this.descriptor = var2;
      this.inputField = this.createInputField();
   }

   private TextInputField2 createInputField() {
      String var1 = this.descriptor.getLabel();
      return this.field.asInputField(var1, !this.descriptor.isOptional());
   }

   public String getValue() {
      String var1 = this.field.getText().trim();
      if (StringUtils.isBlank(var1) && !this.descriptor.isOptional()) {
         var1 = this.descriptor.getDefaultValue();
         if (var1 == null) {
            var1 = "";
         }
      }

      return var1;
   }

   public boolean isOptional() {
      return this.descriptor.isOptional();
   }

   public void layoutIn(ArgumentFieldLayout var1) {
      var1.addField(this.inputField.getLabel(), this.wrapField(), this.getDescriptionButton());
   }

   private JComponent wrapField() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder();
      var1.north(this.field);
      return var1.container();
   }

   private JComponent getDescriptionButton() {
      return this.hasDescription() ? this.createHelpButton() : new JLabel(" ");
   }

   private boolean hasDescription() {
      return StringUtils.isNotBlank(this.descriptor.getDescription());
   }

   private JComponent createHelpButton() {
      Action var1 = this.createAndBindHelpAction();
      KongaButton var2 = new KongaButton(var1);
      ButtonUtils.configureButtonForToolBar(var2);
      TextStyles.DefaultBoldLarger.makeOver(var2);
      var2.setForeground(Color.BLUE);
      return var2;
   }

   private Action createAndBindHelpAction() {
      ArgumentField.ShowDescriptionAction var1 = new ArgumentField.ShowDescriptionAction();
      this.field.bindAction(var1);
      return var1;
   }

   public void requestFocus() {
      this.field.requestFocus();
   }

   public void addToInputPanel(InputPanel var1) {
      var1.addInputField(this.inputField, this.inputField, this.inputField);
   }

   public void addFocusListener(FocusListener var1) {
      this.inputField.getInputComponent().addFocusListener(var1);
   }

   public void removeFocusListener(FocusListener var1) {
      this.inputField.getInputComponent().removeFocusListener(var1);
   }

   public JComponent getComponentForFocusTraversal() {
      return this.inputField.getInputComponent();
   }

   private class ShowDescriptionAction extends AbstractAction {
      public ShowDescriptionAction() {
         super("?");
         this.putValue("AcceleratorKey", KeyUtils.HELP);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         Window var2 = this.getOwner(var1);
         String var3 = this.getTitle();
         Alert.info(var2, ArgumentField.this.descriptor.getDescription(), var3);
         ArgumentField.this.field.requestFocus();
      }

      private Window getOwner(ActionEvent var1) {
         Window var2 = null;
         if (var1.getSource() instanceof Component) {
            var2 = SwingUtilities.getWindowAncestor((Component)var1.getSource());
         }

         if (var2 == null) {
            var2 = UiUtils.getActiveWindow();
         }

         return var2;
      }

      private String getTitle() {
         MnemonicString var1 = UiResourceBundle.MNEMONIC_FACTORY.fromString(ArgumentField.this.descriptor.getLabel());
         return var1.getText();
      }
   }
}
