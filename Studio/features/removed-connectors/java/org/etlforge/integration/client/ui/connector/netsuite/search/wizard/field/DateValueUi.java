package org.etlforge.integration.client.ui.connector.netsuite.search.wizard.field;

import com.google.common.base.Strings;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchOperator;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldDate;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValueDate;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaRadioButton;
import org.etlforge.ui.widget.combobox.KongaComboBox;
import org.etlforge.ui.widget.text.KongaTextField;

public final class DateValueUi extends ValueUi {
   private final KongaRadioButton usePredefinedValue = new KongaRadioButton("Use predefined value");
   private final KongaRadioButton useManualValue = new KongaRadioButton("Enter date(s)");
   private final KongaComboBox predefinedValueSelector;
   private final KongaTextField searchBox1 = new KongaTextField(16);
   private final KongaTextField searchBox2 = new KongaTextField(16);

   public DateValueUi(SearchFieldDate var1) {
      List var2 = var1.getPredefinedSearchValues();
      this.predefinedValueSelector = new KongaComboBox(var2);
      if (var2.isEmpty()) {
         this.useManualValue.setSelected(true);
         this.predefinedValueSelector.setSelectedIndex(0);
      } else {
         this.usePredefinedValue.setSelected(true);
      }

      ButtonUtils.makeMutuallyExclusive(this.usePredefinedValue, this.useManualValue);
      ButtonUtils.addActionListener(new DateValueUi.StateHandler(), this.usePredefinedValue, this.useManualValue);
      this.setEnabledState();
   }

   private void setEnabledState() {
      if (this.useManualValue.isSelected()) {
         this.predefinedValueSelector.setEnabled(false);
         this.predefinedValueSelector.setEditable(false);
         this.searchBox1.setEnabled(true);
         this.searchBox2.setEnabled(true);
      } else {
         this.predefinedValueSelector.setEnabled(true);
         this.predefinedValueSelector.setEditable(true);
         this.searchBox1.setEnabled(false);
         this.searchBox2.setEnabled(false);
      }
   }

   @Override
   public void addActionListener(ActionListener var1) {
      this.searchBox1.addActionListener(var1);
      this.searchBox2.addActionListener(var1);
   }

   @Override
   public void removeActionListener(ActionListener var1) {
      this.searchBox1.removeActionListener(var1);
      this.searchBox2.removeActionListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      NColumnPanel var2 = new NColumnPanel(3);
      var2.setFills(GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.HORIZONTAL, GridBagLayoutPanel.Fill.HORIZONTAL);
      var2.setXWeights(0.0, 0.5, 0.5);
      String var3 = "Datetime format: 2013-10-16T14:00:00-07:00 (optional time zone)";
      var2.add(this.usePredefinedValue)
         .add(this.predefinedValueSelector, 2)
         .addRow(this.useManualValue, this.searchBox1, this.searchBox2)
         .emptyColumn()
         .add(var3, 2);
      Vertical.Anchor.north(var2).decorate(var1);
   }

   @Override
   public void requestFocus() {
      if (this.usePredefinedValue.isSelected()) {
         this.predefinedValueSelector.requestFocusInWindow();
      } else {
         this.searchBox1.requestFocusInWindow();
      }
   }

   protected SearchFieldValueDate constructValue(NetSuiteSearchOperator var1) {
      if (this.usePredefinedValue.isSelected()) {
         String var4 = (String)this.predefinedValueSelector.getSelectedItem();
         return new SearchFieldValueDate(var1, var4);
      } else {
         String var2 = this.searchBox1.getText().trim();
         String var3 = this.searchBox2.getText().trim();
         return new SearchFieldValueDate(var1, var2, Strings.emptyToNull(var3));
      }
   }

   @Override
   public void setValue(SearchFieldValue var1) {
      SearchFieldValueDate var2 = ArgCheck.checkInstance(var1, SearchFieldValueDate.class);
      String var3 = var2.getPredefinedSearchValue();
      if (Strings.isNullOrEmpty(var3)) {
         this.useManualValue.setSelected(true);
         this.searchBox1.setText(var2.getSearchValue());
         this.searchBox2.setText(var2.getSearchValue2());
      } else {
         this.usePredefinedValue.setSelected(true);
         this.predefinedValueSelector.setSelectedItem(var3);
      }

      this.setEnabledState();
   }

   private class StateHandler implements ActionListener {
      private StateHandler() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (var1.getSource() == DateValueUi.this.usePredefinedValue) {
            DateValueUi.this.predefinedValueSelector.requestFocusInWindow();
         } else {
            DateValueUi.this.searchBox1.requestFocusInWindow();
         }

         DateValueUi.this.setEnabledState();
      }
   }
}
