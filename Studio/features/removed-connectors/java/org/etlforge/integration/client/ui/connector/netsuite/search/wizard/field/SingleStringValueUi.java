package org.etlforge.integration.client.ui.connector.netsuite.search.wizard.field;

import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchOperator;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValueString;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.text.KongaTextField;

final class SingleStringValueUi extends ValueUi {
   private final KongaTextField textField = new KongaTextField();

   @Override
   public void decorate(UiCanvas var1) {
      Vertical.Anchor.north(this.textField).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.textField.requestFocusInWindow();
   }

   protected SearchFieldValueString constructValue(NetSuiteSearchOperator var1) {
      String var2 = this.textField.getText().trim();
      return new SearchFieldValueString(var1, var2);
   }

   @Override
   public void setValue(SearchFieldValue var1) {
      SearchFieldValueString var2 = ArgCheck.checkInstance(var1, SearchFieldValueString.class);
      this.textField.setText(var2.getSearchValue());
   }
}
