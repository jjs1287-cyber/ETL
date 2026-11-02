package org.etlforge.integration.client.connect.db2xml.wizard;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.DialogLayoutBuilder;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.BooleanBinding;
import org.etlforge.ui.property.ComponentEnabledBinding;
import org.etlforge.ui.property.TextBinding;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.xml.ElementNameSanitizer;

final class XsdOptionsDialog {
   private final XsdGeneratorModel model;
   private final XsdOptionsDialog.RootNameField rootNameField;
   private final KongaTextField targetNamespaceField;
   private final KongaCheckBox includeDefaultsChoice;
   private final Bindings bindings;

   public XsdOptionsDialog(XsdGeneratorModel var1) {
      this.model = var1;
      this.rootNameField = new XsdOptionsDialog.RootNameField();
      this.targetNamespaceField = new KongaTextField(30);
      this.targetNamespaceField.setSelectAllWhenFocused(true);
      this.includeDefaultsChoice = new KongaCheckBox("Include &default values");
      this.bindings = this.bindProperties();
   }

   private Bindings bindProperties() {
      Bindings var1 = new Bindings(
         TextBinding.bind(this.model, "root", this.rootNameField.getInputComponent()),
         TextBinding.bind(this.model, "targetNamespace", this.targetNamespaceField),
         BooleanBinding.bind(this.model, "includeDefaults", this.includeDefaultsChoice)
      );
      var1.syncUi();
      return var1;
   }

   public void open(Component var1) {
      String var2 = this.model.getRootName();
      String var3 = this.model.getTargetNamespace();
      boolean var4 = this.model.isIncludeDefaultsSelected();
      KongaDialog var5 = this.createDialog(var1);
      var5.setVisible(true);
      if (var5.wasCancelled()) {
         this.model.setRootName(var2);
         this.model.setTargetNamespace(var3);
         this.model.setIncludeDefaultsSelected(var4);
      }

      this.bindings.dispose();
   }

   private KongaDialog createDialog(Component var1) {
      UiProvider var2 = this.layoutDialog();
      KongaDialog var3 = new KongaDialog(SwingUtilities.getWindowAncestor(var1), "XSD Options");
      var3.standardLayout(var2, KongaDialog.OK_CANCEL);
      var3.setOkButtonAsDefault();
      var3.setLocationRelativeTo(null);
      this.bindOkButton(var3);
      return var3;
   }

   private UiProvider layoutDialog() {
      DialogLayoutBuilder var1 = new DialogLayoutBuilder(5, 5);
      var1.addRow(this.rootNameField.getLabel(), this.rootNameField.getInputComponent());
      KongaLabel var2 = KongaLabel.withMnemonic("Target &namespace (optional):");
      var2.setLabelFor(this.targetNamespaceField);
      var1.addRow(var2, this.targetNamespaceField);
      var1.addRow(new JLabel(" "), this.includeDefaultsChoice);
      return var1;
   }

   private void bindOkButton(KongaDialog var1) {
      ComponentEnabledBinding var2 = new ComponentEnabledBinding(this.model, "validSettings", var1.getOKButton());
      var2.syncUi();
      this.bindings.add(var2);
   }

   private static class RootNameField extends TextInputField2 {
      public RootNameField() {
         super("Root Name", new KongaTextField(30), "&Root name:");
         this.setSelectAllWhenFocused(true);
         this.setAutoTrim(true);
         this.setActive(true);
      }

      @Override
      public boolean isInputValid(String var1) {
         return ElementNameSanitizer.isValid(var1);
      }

      @Override
      public String getInvalidMessage(String var1) {
         return "The name is not a valid XML element name.";
      }
   }
}
