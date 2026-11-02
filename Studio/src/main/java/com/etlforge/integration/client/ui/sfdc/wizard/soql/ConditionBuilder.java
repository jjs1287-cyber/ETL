package com.etlforge.integration.client.ui.sfdc.wizard.soql;

import com.etlforge.integration.salesforce.ui.renderer.Renderers;
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumSet;
import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.border.Border;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.combobox.EnumComboBox;
import org.etlforge.ui.widget.combobox.KongaComboBox;

final class ConditionBuilder implements UiProvider {
   private final QueryBuilderModel model;
   private final KongaComboBox fieldDropdown;
   private final EnumComboBox<Operator> operatorDropdown = EnumComboBox.of(Operator.class);
   private final ConditionValueField valueField = new ConditionValueField();
   private final ConditionBuilder.AddConditionAction addAction = new ConditionBuilder.AddConditionAction();
   private final JComponent ui;

   public ConditionBuilder(QueryBuilderModel var1) {
      this.model = var1;
      this.fieldDropdown = this.createFieldDropDown(var1);
      this.ui = this.layout();
      this.init();
   }

   private KongaComboBox createFieldDropDown(QueryBuilderModel var1) {
      KongaComboBox var2 = new KongaComboBox(var1.getFilterableFields());
      var2.setRenderer(Renderers.forList(var1.getFieldNameDisplayMode()));
      var2.setMaximumRowCount(12);
      return var2;
   }

   private JComponent layout() {
      NColumnPanel var1 = this.layoutControls();
      UiProvider var2 = this.layoutButtons();
      return new OneColumnPanel().withInsets(0, 0, 10, 0).setWeightX(1.0).of(var1, var2).container();
   }

   private NColumnPanel layoutControls() {
      NColumnPanel var1 = this.emptyGrid();
      var1.addRow("Field", "Operator", "Value");
      var1.addRow(this.fieldDropdown, this.operatorDropdown, this.valueField.getUi());
      return var1;
   }

   private NColumnPanel emptyGrid() {
      Insets var1 = new Insets(0, 0, 0, 10);
      return new NColumnPanel(3)
         .setInsets(0, var1)
         .setInsets(1, var1)
         .setInsets(2, Empty.insets())
         .setFills(GridBagLayoutPanel.Fill.HORIZONTAL, GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.HORIZONTAL)
         .setXWeights(0.5, 0.0, 0.5);
   }

   private UiProvider layoutButtons() {
      ConditionBuilder.RemoveAllAction var1 = new ConditionBuilder.RemoveAllAction();
      Grid var2 = Horizontal.grid().of(this.addAction, var1).withHGap(10);
      return Horizontal.Align.west(var2);
   }

   private void init() {
      this.fieldDropdown.setSelectedIndex(-1);
      this.operatorDropdown.setSelectedIndex(-1);
      this.fieldDropdown.setSelectionReceiver(new ConditionBuilder.SelectedFieldListener());
      this.valueField.addActionListener(this.addAction);
      this.model.addPropertyChangeListener("object", new ConditionBuilder.ObjectListener());
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
      this.fieldDropdown.requestFocusInWindow();
   }

   public ConditionBuilder withBorder(Border var1) {
      this.ui.setBorder(var1);
      return this;
   }

   private class AddConditionAction extends AbstractAction {
      public AddConditionAction() {
         super("Add Condition");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               AddConditionAction.this.addCondition();
            }
         });
      }

      private void addCondition() {
         SObjectSchema.Field var1 = (SObjectSchema.Field)ConditionBuilder.this.fieldDropdown.getSelectedItem();
         if (var1 != null) {
            Operator var2 = ConditionBuilder.this.operatorDropdown.getSelectedItem();
            if (var2 != null) {
               String var3 = ConditionBuilder.this.valueField.getValue();
               if (!StringUtils.isBlank(var3) || !this.requiresValue(var1)) {
                  ConditionValueValidator var4 = ConditionValueValidatorFactory.forField(var1);
                  if (var4.isValid(var3, var2)) {
                     Condition var5 = new Condition(var1, var2, var3);
                     ConditionBuilder.this.model.addCondition(var5);
                  } else {
                     Alert.error(ConditionBuilder.this.ui, var4.whyNot(), "Invalid Condition");
                  }
               }
            }
         }
      }

      private boolean requiresValue(SObjectSchema.Field var1) {
         SObjectSchema.Field.SoapType var2 = var1.getSoapType();
         return var2 != SObjectSchema.Field.SoapType.STRING && var2 != SObjectSchema.Field.SoapType.ID;
      }
   }

   private class ObjectListener implements PropertyChangeListener {
      private ObjectListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         DefaultComboBoxModel var2 = new DefaultComboBoxModel();

         for (SObjectSchema.Field var4 : ConditionBuilder.this.model.getFilterableFields()) {
            var2.addElement(var4);
         }

         ConditionBuilder.this.fieldDropdown.setModel(var2);
         ConditionBuilder.this.fieldDropdown.setSelectedIndex(-1);
      }
   }

   private class RemoveAllAction extends AbstractAction {
      public RemoveAllAction() {
         super("Remove All");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ConditionBuilder.this.model.removeAllConditions();
            }
         });
      }
   }

   private class SelectedFieldListener implements Receiver<Object> {
      private SelectedFieldListener() {
      }

      @Override
      public void handle(Object var1) {
         SObjectSchema.Field var2 = (SObjectSchema.Field)var1;
         ConditionBuilder.this.valueField.setField(var2);
         Enum var3 = ConditionBuilder.this.operatorDropdown.getSelectedItem();
         ComboBoxModel var4 = this.createOperatorModel(var2);
         ConditionBuilder.this.operatorDropdown.setModel(var4);
         ConditionBuilder.this.operatorDropdown.setSelectedItem(var3);
      }

      private ComboBoxModel createOperatorModel(SObjectSchema.Field var1) {
         if (var1 == null) {
            return new DefaultComboBoxModel<>(Operator.values());
         }

         EnumSet var2 = Operator.getApplicableOperators(var1);
         return new DefaultComboBoxModel<>(var2.toArray());
      }
   }
}
