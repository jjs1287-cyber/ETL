package org.etlforge.integration.client.ui.connector.netsuite.search.wizard.field;

import java.awt.Window;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchOperator;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.SearchField;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.combobox.EnumComboBox;

final class FilterUi implements UiProvider {
   private final String objectName;
   private final String fieldName;
   private final SearchField field;
   private final EnumComboBox<NetSuiteSearchOperator> operatorBox;
   private final ValueUi valueUi;

   public static FilterUi newFilter(String var0, String var1, SearchField var2) {
      ValueUi var3 = ValueUi.fromField(var2);
      return new FilterUi(var0, var1, var2, var3);
   }

   public static FilterUi editFilter(Filter var0) {
      ValueUi var1 = ValueUi.fromFilter(var0);
      FilterUi var2 = new FilterUi(var0.getObjectName(), var0.getFieldName(), var0.getFieldDef(), var1);
      NetSuiteSearchOperator var3 = var0.getFieldValue().getNetSuiteSearchOperator();
      var2.selectOperator(var3);
      return var2;
   }

   private FilterUi(String var1, String var2, SearchField var3, ValueUi var4) {
      this.objectName = var1;
      this.fieldName = var2;
      this.field = var3;
      this.valueUi = var4;
      this.operatorBox = createOpBox(var3);
   }

   private static EnumComboBox<NetSuiteSearchOperator> createOpBox(SearchField var0) {
      List var1 = var0.getNetSuiteSearchOperators();
      if (var1.isEmpty()) {
         EnumComboBox var3 = EnumComboBox.of(EnumSet.noneOf(NetSuiteSearchOperator.class));
         var3.setEditable(false);
         var3.setEnabled(false);
         return var3;
      } else {
         EnumComboBox var2 = EnumComboBox.of(var1);
         var2.setSelectedIndex(0);
         return var2;
      }
   }

   public void selectOperator(NetSuiteSearchOperator var1) {
      this.operatorBox.setSelectedItem(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (this.hasOperators()) {
         TwoColumnPanel var2 = new TwoColumnPanel()
            .withBorder(StyleSheet.getDefaultTitleBorder(null))
            .withLeftAnchor(GridBagLayoutPanel.Anchor.WEST)
            .withRightAnchor(GridBagLayoutPanel.Anchor.WEST)
            .withLeftFill(GridBagLayoutPanel.Fill.NONE)
            .withRightFill(GridBagLayoutPanel.Fill.HORIZONTAL)
            .withLeftInsets(0, 0, 10, 8)
            .withRightInsets(0, 0, 10, 0);
         var2.addLabeledComponent("Operator:", this.operatorBox);
         var2.withLeftAnchor(GridBagLayoutPanel.Anchor.NORTHWEST)
            .withRightAnchor(GridBagLayoutPanel.Anchor.NORTHWEST)
            .withRightFill(GridBagLayoutPanel.Fill.BOTH)
            .withRightWeights(1.0, 1.0);
         var2.addLabeledComponent("Value(s):", this.valueUi);
         var2.decorate(var1);
      } else {
         this.valueUi.decorate(var1);
      }
   }

   private boolean hasOperators() {
      return this.operatorBox.getItemCount() > 0;
   }

   @Override
   public void requestFocus() {
      this.operatorBox.requestFocusInWindow();
   }

   @Nullable
   public Filter getDefinedFilter() {
      NetSuiteSearchOperator var1 = this.operatorBox.getSelectedItem();
      if (var1 == null && this.hasOperators()) {
         return null;
      }

      SearchFieldValue var2 = this.valueUi.getValue(var1);
      return new Filter(this.objectName, this.fieldName, this.field, var2);
   }

   @Nullable
   public Filter openInAddFilterDialog(Window var1) {
      String var2 = "Add Condition";
      String var3 = var2;
      return this.openInDialog(var1, var2, var3);
   }

   @Nullable
   public Filter openInEditFilterDialog(Window var1) {
      String var2 = "Edit Condition";
      String var3 = "Update Condition";
      return this.openInDialog(var1, var2, var3);
   }

   private Filter openInDialog(Window var1, String var2, final String var3) {
      WizardStyleDialog var4 = new WizardStyleDialog(var1, var2, this, KongaDialog.OK_CANCEL, true) {
         @Override
         protected void onOk() {
            ValueUi.ValidationResult var1x = FilterUi.this.valueUi.validateInput();
            if (var1x.isValid()) {
               super.onOk();
            } else {
               Alert.error(this, var1x.whyNot(), "Invalid input");
            }
         }

         @Override
         protected boolean useFixedButtonSize() {
            return false;
         }

         @Override
         protected String getOkButtonText() {
            return var3;
         }
      };
      var4.setOkButtonAsDefault();
      var4.manageLocation(this.valueUi.getClass());
      var4.setVisible(true);
      return var4.wasOkPressed() ? this.getDefinedFilter() : null;
   }
}
