package com.etlforge.integration.client.ui.sfdc.query.page;

import com.etlforge.integration.client.ui.sfdc.page.soql.SoqlModel;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.binding.ui.BoundPropertyFactory;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.entity.sfquery.TargetHeaderMode;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.widget.combobox.EnumComboBox;

final class TargetHeaderModeSelector {
   private final ComboBoxInputField dropdown;
   private final boolean applicable;

   public TargetHeaderModeSelector(SalesforceQuery var1, InputPanel var2) {
      this.applicable = var1.isTargetHeaderSupported();
      this.dropdown = createDropdown(var1);
      this.dropdown.setEnabled(this.applicable);
      var2.addInputField(this.dropdown);
   }

   private static ComboBoxInputField createDropdown(SalesforceQuery var0) {
      EnumComboBox var1 = EnumComboBox.of(TargetHeaderMode.class);
      var1.setSelectedItem(var0.getTargetHeaderMode());
      String var2 = "Target header";
      return new ComboBoxInputField(var2, var1, var2 + ":");
   }

   public void layoutIn(TwoColumnLayout var1) {
      this.dropdown.layoutIn(var1);
   }

   public TargetHeaderMode getSelectedMode() {
      return (TargetHeaderMode)this.dropdown.getSelectedItem();
   }

   public void bindToSoqlModel(SoqlModel var1) {
      if (this.applicable) {
         TargetHeaderModeSelector.SoqlBinding var2 = new TargetHeaderModeSelector.SoqlBinding(var1);
         var2.syncUi();
      }
   }

   public BoundProperty asBoundProperty() {
      return BoundPropertyFactory.comboProperty("target_header_mode", this.dropdown);
   }

   private class SoqlBinding extends AbstractBinding<Boolean> {
      public SoqlBinding(SoqlModel var2) {
         super(var2, "headerAvailable");
      }

      protected void updateUiWithValue(Boolean var1) {
         assert TargetHeaderModeSelector.this.applicable : "The binding should not be installed if not applicable";
         TargetHeaderModeSelector.this.dropdown.setEnabled(var1);
      }
   }
}
