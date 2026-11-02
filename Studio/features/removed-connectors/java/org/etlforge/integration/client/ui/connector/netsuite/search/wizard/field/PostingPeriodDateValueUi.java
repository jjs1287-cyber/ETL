package org.etlforge.integration.client.ui.connector.netsuite.search.wizard.field;

import java.util.List;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchOperator;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldPostingPeriodDate;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValueString;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.combobox.KongaComboBox;

final class PostingPeriodDateValueUi extends ValueUi {
   private final KongaComboBox valueSelector;

   public PostingPeriodDateValueUi(SearchFieldPostingPeriodDate var1) {
      List var2 = var1.getPredefinedList();
      this.valueSelector = new KongaComboBox(var2);
      if (!var2.isEmpty()) {
         this.valueSelector.setSelectedIndex(0);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      Vertical.Anchor.north(this.valueSelector).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.valueSelector.requestFocusInWindow();
   }

   protected SearchFieldValueString constructValue(NetSuiteSearchOperator var1) {
      String var2 = (String)this.valueSelector.getSelectedItem();
      return new SearchFieldValueString(var1, var2, true);
   }

   @Override
   public void setValue(SearchFieldValue var1) {
      SearchFieldValueString var2 = ArgCheck.checkInstance(var1, SearchFieldValueString.class);
      this.valueSelector.setSelectedItem(var2.getSearchValue());
   }
}
