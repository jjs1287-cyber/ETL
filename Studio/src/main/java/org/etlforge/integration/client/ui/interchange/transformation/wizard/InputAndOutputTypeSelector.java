package org.etlforge.integration.client.ui.interchange.transformation.wizard;

import com.google.common.collect.ImmutableList;
import java.awt.Component;
import java.text.MessageFormat;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.FunctionCallRequestStructure;
import org.etlforge.integration.data.structure.FunctionCallResponseStructure;
import org.etlforge.integration.data.structure.FunctionCallStructure;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.InputFieldDecorator;
import org.etlforge.ui.input.InputFieldException;
import org.etlforge.ui.input.InputFieldValidator;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.LabelIconFieldDecorator;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.widget.label.KongaLabel;

final class InputAndOutputTypeSelector {
   private final Transformation transformation;
   private final ImmutableList<WizardType> inputTypes;
   private final ImmutableList<WizardType> outputTypes;
   private final ComboBoxInputField sourceTypeBox;
   private final ComboBoxInputField targetTypeBox;
   private IntegrationEntityLookup itemLookup;

   public InputAndOutputTypeSelector(InputPanel var1, Transformation var2, List<WizardType> var3, List<WizardType> var4) {
      this.transformation = var2;
      this.inputTypes = ImmutableList.copyOf(var3);
      this.outputTypes = ImmutableList.copyOf(var4);
      this.sourceTypeBox = this.createSourceBox();
      this.targetTypeBox = this.createTargetBox();
      LabelIconFieldDecorator var5 = new LabelIconFieldDecorator();
      addInputField(var1, this.sourceTypeBox, "Source", var5);
      addInputField(var1, this.targetTypeBox, "Target", var5);
   }

   private static void addInputField(InputPanel var0, InputField var1, String var2, InputFieldDecorator var3) {
      String var4 = Strings.get("StartPage." + var2 + "Missing");
      var0.addInputField(var1, new InputAndOutputTypeSelector.DataTypeInputValidator(var4), var3);
   }

   public void setItemLookup(IntegrationEntityLookup var1) {
      this.itemLookup = var1;
   }

   public void layout(TwoColumnPanel var1) {
      var1.withInsets(2, 2, 10, 2);
      this.layoutSourceSelector(var1);
      this.layoutOutputSelector(var1);
   }

   private void layoutSourceSelector(TwoColumnPanel var1) {
      var1.addRow(this.sourceTypeBox.getLabel(), this.getSourceSelectorComponent());
   }

   private JComponent getSourceSelectorComponent() {
      WizardType var1 = this.getSourceTypeFromPreconfiguredSource();
      if (var1 == null) {
         var1 = this.deduceTypeFromInputStructure();
      }

      if (var1 != null) {
         this.sourceTypeBox.setSelectedItem(var1);
         return this.getPreconfiguredSourceTypeMessage(var1);
      } else {
         return this.sourceTypeBox.getInputComponent();
      }
   }

   private WizardType getSourceTypeFromPreconfiguredSource() {
      Source var1 = this.getPreconfiguredSource();
      if (var1 == null) {
         return null;
      }

      for (WizardType var3 : this.inputTypes) {
         if (var3.isTiedToLocatable(var1)) {
            return var3;
         }
      }

      return null;
   }

   private Source getPreconfiguredSource() {
      SourceId var1 = this.transformation.getSourceId();
      return this.itemLookup != null ? this.itemLookup.getEntity(var1, Source.class) : null;
   }

   private WizardType deduceTypeFromInputStructure() {
      for (WizardType var2 : this.inputTypes) {
         if (var2.isTiedToInput(this.transformation)) {
            return var2;
         }
      }

      return null;
   }

   private JComponent getPreconfiguredSourceTypeMessage(WizardType var1) {
      DataStructure var2 = this.transformation.getInputStructure();
      return var2 instanceof FunctionCallResponseStructure
         ? this.createFunctionCallAsInputOrOutputMessage((FunctionCallResponseStructure)var2, "source", "response from")
         : new JLabel("The transformation is already configured to use a source of type " + var1.getName() + ".");
   }

   private JComponent createFunctionCallAsInputOrOutputMessage(FunctionCallStructure var1, String var2, String var3) {
      IntegrationEntityId var4 = var1.getFunctionCallId();
      IntegrationEntity var5 = this.itemLookup.getEntity(var4);
      KongaLabel var6 = TextStyles.AdminHint.makeLabel("");
      RendererUtils.decorateRenderer((javax.swing.JLabel)var6, var5, false);
      String var7 = MessageFormat.format("The {0} of the transformation is a {1} ", var2, var3);
      return Horizontal.box().of(var7, var6).container();
   }

   private void layoutOutputSelector(TwoColumnPanel var1) {
      var1.addRow(this.targetTypeBox.getLabel(), this.getTargetSelectorComponent());
   }

   private JComponent getTargetSelectorComponent() {
      WizardType var1 = this.getTargetTypeFromPreconfiguredTarget();
      if (var1 == null) {
         var1 = this.deduceTypeFromOutputStructure();
      }

      if (var1 != null) {
         this.targetTypeBox.setSelectedItem(var1);
         return this.getPreconfiguredTargetTypeMessage(var1);
      } else {
         return this.targetTypeBox.getInputComponent();
      }
   }

   private WizardType getTargetTypeFromPreconfiguredTarget() {
      Target var1 = this.getPreconfiguredTarget();
      if (var1 == null) {
         return null;
      }

      for (WizardType var3 : this.outputTypes) {
         if (var3.isTiedToLocatable(var1)) {
            return var3;
         }
      }

      return null;
   }

   private Target getPreconfiguredTarget() {
      TargetId var1 = this.transformation.getTargetId();
      return this.itemLookup != null ? this.itemLookup.getEntity(var1, Target.class) : null;
   }

   private WizardType deduceTypeFromOutputStructure() {
      for (WizardType var2 : this.outputTypes) {
         if (var2.isTiedToOutput(this.transformation)) {
            return var2;
         }
      }

      return null;
   }

   private JComponent getPreconfiguredTargetTypeMessage(WizardType var1) {
      DataStructure var2 = this.transformation.getOutputStructure();
      return var2 instanceof FunctionCallRequestStructure
         ? this.createFunctionCallAsInputOrOutputMessage((FunctionCallRequestStructure)var2, "target", "request to")
         : new JLabel("The transformation is already configured to use a target of type " + var1.getName() + ".");
   }

   public WizardType getSourceType() {
      return (WizardType)this.sourceTypeBox.getSelectedItem();
   }

   public WizardType getTargetType() {
      return (WizardType)this.targetTypeBox.getSelectedItem();
   }

   public void setSelection(WizardType var1, WizardType var2) {
      this.sourceTypeBox.setSelectedItem(var1);
      this.targetTypeBox.setSelectedItem(var2);
   }

   public void installUndoRedo(UndoRedoInstaller var1) {
      var1.install(this.sourceTypeBox.getInputComponent());
      var1.install(this.targetTypeBox.getInputComponent());
   }

   public JComponent[] getKeyBindingTargets() {
      return new JComponent[]{this.sourceTypeBox.getInputComponent(), this.targetTypeBox.getInputComponent()};
   }

   private ComboBoxInputField createSourceBox() {
      ComboBoxModel var1 = this.createComboModel(this.inputTypes);
      ComboBoxInputField var2 = this.createComboInput(
         var1, Strings.mnemonic("StartPage.Label.Source"), Strings.get("StartPage.SelectSource"), SourceTarget.Source
      );

      for (WizardType var4 : this.inputTypes) {
         if (var4.isTiedToInput(this.transformation)) {
            var2.setSelectedItem(var4);
            break;
         }
      }

      return var2;
   }

   private ComboBoxModel createComboModel(List<WizardType> var1) {
      DefaultComboBoxModel var2 = new DefaultComboBoxModel();

      for (WizardType var4 : var1) {
         var2.addElement(var4);
      }

      return var2;
   }

   private ComboBoxInputField createTargetBox() {
      ComboBoxModel var1 = this.createComboModel(this.outputTypes);
      ComboBoxInputField var2 = this.createComboInput(
         var1, Strings.mnemonic("StartPage.Label.Target"), Strings.get("StartPage.SelectTarget"), SourceTarget.Target
      );

      for (WizardType var4 : this.outputTypes) {
         if (var4.isTiedToOutput(this.transformation)) {
            var2.setSelectedItem(var4);
            break;
         }
      }

      return var2;
   }

   private ComboBoxInputField createComboInput(ComboBoxModel var1, MnemonicString var2, String var3, SourceTarget var4) {
      JComboBox var5 = new JComboBox(var1);
      var5.setRenderer(new InputAndOutputTypeSelector.Renderer(var3, var4));
      var5.setMaximumRowCount(16);
      ComboBoxInputField var6 = new ComboBoxInputField(var2.getText(), var5, new KongaLabel(var2));
      var6.setSelectedIndex(-1);
      return var6;
   }

   private static class DataTypeInputValidator implements InputFieldValidator {
      private final String message;

      public DataTypeInputValidator(String var1) {
         this.message = var1;
      }

      @Override
      public boolean isFieldRequired(InputField var1) {
         return true;
      }

      @Override
      public boolean isFieldValid(InputField var1) {
         JComboBox var2 = ((ComboBoxInputField)var1).getInputComponent();
         WizardType var3 = (WizardType)var2.getSelectedItem();
         return var3 != null;
      }

      @Override
      public void validateField(InputField var1) throws InputFieldException {
         if (!this.isFieldValid(var1)) {
            throw new InputFieldException(var1, this.message);
         }
      }
   }

   private static class Renderer extends DefaultListCellRenderer {
      private final String nullString;
      private final SourceTarget st;

      public Renderer(String var1, SourceTarget var2) {
         this.nullString = var1;
         this.st = var2;
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         if (var2 == null) {
            this.setText(this.nullString);
         } else if (var2 instanceof WizardType) {
            this.setText(this.getDisplayName((WizardType)var2));
         }

         return this;
      }

      private String getDisplayName(WizardType var1) {
         if (var1 == WizardTypes.WEBSERVICE_REQUEST && this.st == SourceTarget.Source) {
            return "Hosted " + var1.getName();
         } else {
            return var1 == WizardTypes.WEBSERVICE_RESPONSE && this.st == SourceTarget.Target ? "Hosted " + var1.getName() : var1.getName();
         }
      }
   }
}

