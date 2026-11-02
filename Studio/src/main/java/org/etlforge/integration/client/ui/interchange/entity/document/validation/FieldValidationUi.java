package org.etlforge.integration.client.ui.interchange.entity.document.validation;

import com.google.common.base.Functions;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.etlforge.integration.data.structure.text.FieldDataType;
import org.etlforge.integration.data.structure.text.validation.InvalidRecordAction;
import org.etlforge.system.Disposable;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.IntegerInputField;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.BooleanBinding;
import org.etlforge.ui.property.ComboBoxBinding;
import org.etlforge.ui.property.TextBinding;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.combobox.EnumComboBox;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.IntegerDocument;
import org.etlforge.ui.widget.text.KongaTextField;

public final class FieldValidationUi implements Disposable, UiProvider {
   private final FieldValidationModel model;
   private final KongaCheckBox enabler = new KongaCheckBox("&Validate");
   private final CheckBoxInputField required = new CheckBoxInputField("Not Empty", new KongaCheckBox(), "Not &Empty");
   private final IntegerInputField lengthField = new IntegerInputField("Length", new KongaTextField(), 0, Integer.MAX_VALUE, "&Length");
   private final StringOptionsUi stringOptions;
   private final TextInputField dateFormat = new TextInputField("Date Format", new KongaTextField(20), "Date &Format");
   private final OperatorBox operatorBox;
   private final ComboBoxInputField action = createActionSelector();
   private final CopyAction copyAction;
   private final PasteAction pasteAction;

   public FieldValidationUi(FieldValidationModel var1, OperatorBox var2) {
      this.model = var1;
      this.operatorBox = var2;
      this.stringOptions = new StringOptionsUi(var1);
      this.lengthField.setRequired(false);
      this.lengthField.setDocument(new IntegerDocument());
      this.copyAction = new CopyAction(var1);
      this.pasteAction = new PasteAction(var1);
      this.pasteAction.listenToClipboard();
      this.bindToModel();
   }

   private static ComboBoxInputField createActionSelector() {
      EnumComboBox var0 = EnumComboBox.of(InvalidRecordAction.class);
      var0.generateDisplayStrings(Functions.toStringFunction());
      return new ComboBoxInputField("Action", var0, "Define what &action to take if the field is invalid:");
   }

   private void bindToModel() {
      Bindings var1 = new Bindings(
         BooleanBinding.bind(this.model, "enabled", this.enabler),
         BooleanBinding.bind(this.model, "required", this.required.getInputComponent()),
         TextBinding.bind(this.model, "length", this.lengthField.getInputComponent()),
         this.stringOptions.bind(),
         TextBinding.bind(this.model, "dateFormat", this.dateFormat.getInputComponent()),
         ComboBoxBinding.forEnum(this.action.getInputComponent(), InvalidRecordAction.class, this.model.getProperty("action")),
         new FieldValidationUi.DataTypeBinding(),
         new FieldValidationUi.EnabledStateBinding()
      );
      var1.syncUi();
   }

   private void updateEnableState(boolean var1) {
      this.required.setEnabled(var1);
      this.lengthField.setEnabled(var1);
      this.stringOptions.setEnabled(var1 && this.model.getDataType() == FieldDataType.String);
      this.dateFormat.setEnabled(var1 && this.model.getDataType() == FieldDataType.Date);
      this.operatorBox.setEnabled(var1);
      this.action.setEnabled(var1);
   }

   public void registerAsChangeSource(InputPanel var1) {
      var1.addInputField(new CheckBoxInputField("Enabler", this.enabler));
      var1.addInputField(this.required);
      var1.addInputField(this.lengthField);
      var1.addInputField(this.dateFormat);
      var1.addInputField(this.action);
      this.operatorBox.registerAsChangeSource(var1);
      this.stringOptions.registerAsChangeSource(var1);
      this.pasteAction.registerAsChangeSource(var1);
   }

   @Override
   public void dispose() {
      this.pasteAction.dispose();
   }

   @Override
   public void decorate(UiCanvas var1) {
      UiProvider var2 = this.layoutTopRow();
      UiProvider var3 = this.layoutControls();
      CaptionLayout.of(var2, var3).decorate(var1);
   }

   private UiProvider layoutTopRow() {
      return Horizontal.box().add(this.enabler).glue().addAllWithSpace(10, this.copyAction, this.pasteAction);
   }

   private UiProvider layoutControls() {
      return Vertical.borderLayout(10).withBorder(Empty.border(12, 12, 0, 0)).of(null, this.layoutRules(), this.layoutActionSelector());
   }

   private JComponent layoutActionSelector() {
      JPanel var1 = InvisiblePanel.newPanel();
      this.action.layoutVertically(var1);
      var1.setBorder(Empty.border(0, 0, 10, 0));
      return InvisiblePanel.of(var1);
   }

   private TwoColumnPanel layoutRules() {
      TwoColumnPanel var1 = TwoColumnPanel.forFormEntry(5, 8).withBorder(new TitledBorder("Rules"));
      this.required.layoutIn(var1);
      if (!this.model.isFixedWidth()) {
         this.lengthField.layoutIn(var1);
      }

      this.stringOptions.layoutIn(var1);
      this.dateFormat.layoutIn(var1);
      this.operatorBox.layoutIn(var1);
      return var1;
   }

   @Override
   public void requestFocus() {
      this.enabler.requestFocusInWindow();
   }

   private class DataTypeBinding extends AbstractBinding<FieldDataType> {
      public DataTypeBinding() {
         super(FieldValidationUi.this.model, "dataType");
      }

      protected void updateUiWithValue(FieldDataType var1) {
         FieldValidationUi.this.updateEnableState(FieldValidationUi.this.model.isEnabled());
      }
   }

   private class EnabledStateBinding extends AbstractBinding<Boolean> {
      public EnabledStateBinding() {
         super(FieldValidationUi.this.model, "enabled");
      }

      protected void updateUiWithValue(Boolean var1) {
         FieldValidationUi.this.updateEnableState(var1);
      }
   }
}
