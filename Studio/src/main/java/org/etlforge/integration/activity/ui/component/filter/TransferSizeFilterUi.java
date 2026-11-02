package org.etlforge.integration.activity.ui.component.filter;

import java.util.EnumSet;
import javax.swing.JComponent;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.activity.client.ActivityEntry;
import org.etlforge.integration.activity.client.filter.TransferSizeFilter;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.input.IntegerInputField;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.widget.combobox.EnumComboBox;
import org.etlforge.util.StorageSizeUnit;
import org.etlforge.util.arithmetic.ComparisonOperator;

public final class TransferSizeFilterUi<T extends ActivityEntry> extends AbstractActivityEntryFilterUi<T> {
   private static final EnumSet<ComparisonOperator> OPERATORS = EnumSet.of(ComparisonOperator.LT, ComparisonOperator.EQ, ComparisonOperator.GT);
   private final EnumComboBox<ComparisonOperator> operatorCombo;
   private final EnumComboBox<StorageSizeUnit> sizeUnitCombo;
   private final IntegerInputField valueField;
   private final String label;
   private ComparisonOperator comparisonOp;
   private StorageSizeUnit sizeUnit;
   private int value;

   public TransferSizeFilterUi(String var1) {
      this.label = var1;
      this.operatorCombo = new EnumComboBox<>(OPERATORS);
      this.sizeUnitCombo = new EnumComboBox<>(StorageSizeUnit.class);
      this.valueField = InputFieldFactory.getIntegerInputField("", null, 0, Integer.MAX_VALUE, 5);
      this.valueField.setRequired(false);
   }

   @Override
   public void apply() {
      this.comparisonOp = this.operatorCombo.getSelectedItem();
      this.sizeUnit = this.sizeUnitCombo.getSelectedItem();
      this.value = this.valueField.getIntValue(-1);
   }

   @Override
   public void reset() {
      this.operatorCombo.setSelectedItem(this.comparisonOp);
      this.sizeUnitCombo.setSelectedItem(this.sizeUnit);
      if (this.value >= 0) {
         this.valueField.setValue(this.value);
      } else {
         this.valueField.setValue("");
      }
   }

   @Override
   public boolean isActive() {
      return this.comparisonOp != null && this.sizeUnit != null && this.value >= 0;
   }

   public TransferSizeFilter getFilter() {
      return new TransferSizeFilter(
         this.comparisonOp == null ? ComparisonOperator.GE : this.comparisonOp, this.sizeUnit == null ? -1L : this.sizeUnit.toBytes(this.value)
      );
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.addAllWithSpace(5, this.operatorCombo, this.valueField.getInputComponent(), this.sizeUnitCombo);
      JComponent var3 = InputFieldFactory.getLabeledComponent(var2.container(), this.label, this.getCapsuleOrientation());
      var1.display(var3);
   }

   @Override
   public void requestFocus() {
      this.valueField.requestFocus();
   }

   @Override
   public String getFilterString() {
      return this.comparisonOp != null && this.sizeUnit != null && this.value >= 0
         ? PackageResources.TransferSizeFilter.getFilterString(this.label, this.comparisonOp.toString(), this.value, this.sizeUnit.toString())
         : "";
   }
}
