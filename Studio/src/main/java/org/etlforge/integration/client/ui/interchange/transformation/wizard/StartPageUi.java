package org.etlforge.integration.client.ui.interchange.transformation.wizard;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import org.etlforge.collections.Pair;
import org.etlforge.integration.client.ui.entity.wizard.WizardNameInputField;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.undo.UndoRedoInstaller;

final class StartPageUi implements UiProvider {
   private final TextInputField2 nameField;
   private final InputAndOutputTypeSelector structureTypeSelector;
   private final InputPanel inputPanel = new InputPanel(new FlowLayout(3, 0, 0));

   public StartPageUi(Transformation var1, Folder var2, List<WizardType> var3, List<WizardType> var4) {
      this.nameField = new WizardNameInputField(Strings.mnemonic("Name"), var1, var2);
      this.inputPanel.addInputField(this.nameField, this.nameField, this.nameField);
      this.structureTypeSelector = new InputAndOutputTypeSelector(this.inputPanel, var1, var3, var4);
      this.registerListeners();
      this.installQuickSelector(var3, var4);
   }

   public void setItemLookup(IntegrationEntityLookup var1) {
      this.structureTypeSelector.setItemLookup(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.layoutInputPanel();
      var1.display(this.inputPanel.getUi());
   }

   @Override
   public void requestFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            StartPageUi.this.nameField.requestFocus();
         }
      });
   }

   public String getTransformationName() {
      return this.nameField.getValue();
   }

   public WizardType getSourceType() {
      return this.structureTypeSelector.getSourceType();
   }

   public WizardType getTargetType() {
      return this.structureTypeSelector.getTargetType();
   }

   public boolean isInputValid() {
      return this.inputPanel.isInputValid();
   }

   public void addChangeListener(InputChangeListener var1) {
      this.inputPanel.addInputChangeListener(var1);
   }

   public void addActionListener(ActionListener var1) {
      ((JTextField)this.nameField.getInputComponent()).addActionListener(var1);
   }

   public JComponent[] getKeyBindingTargets() {
      return this.structureTypeSelector.getKeyBindingTargets();
   }

   public void installUndoRedo(UndoRedoInstaller var1) {
      var1.install(this.nameField.getInputComponent());
      this.structureTypeSelector.installUndoRedo(var1);
   }

   private void registerListeners() {
      this.inputPanel.setSendInputChangeEvents(true);
      this.inputPanel.addInputChangeListener(new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1) {
            StartPageUi.this.inputPanel.decorateFields();
         }
      });
   }

   private void layoutInputPanel() {
      TwoColumnPanel var1 = new TwoColumnPanel();
      var1.withRightFill(TwoColumnPanel.HORIZONTAL);
      this.layoutNameField(var1);
      this.structureTypeSelector.layout(var1);
      var1.setOpaque(false, true);
      this.inputPanel.wrap(var1);
   }

   private void layoutNameField(TwoColumnPanel var1) {
      var1.withInsets(2, 2, 20, 2);
      var1.addRow(this.nameField.getLabel(), this.nameField.getInputComponent());
   }

   private void installQuickSelector(List<WizardType> var1, List<WizardType> var2) {
      StartPageUi.QuickSelector var3 = new StartPageUi.QuickSelector(var1, var2);
      ActionKeyBinding.install(var3, this.inputPanel.getUi(), 1);
   }

   private final class QuickSelector extends AbstractAction {
      private final TypeCombinationMenu menu;

      private QuickSelector(List<WizardType> var2, List<WizardType> var3) {
         this.menu = new TypeCombinationMenu(var2, var3);
         this.menu.setCallback(new Receiver<Pair<WizardType, WizardType>>() {
            public void handle(Pair<WizardType, WizardType> var1) {
               StartPageUi.this.structureTypeSelector.setSelection((WizardType)var1.first, (WizardType)var1.second);
            }
         });
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(81, 128));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         JPanel var2 = StartPageUi.this.inputPanel.getUi();
         this.menu.show(var2, 50, 50);
      }
   }
}
