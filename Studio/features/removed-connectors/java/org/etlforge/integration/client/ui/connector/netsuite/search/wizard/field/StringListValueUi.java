package org.etlforge.integration.client.ui.connector.netsuite.search.wizard.field;

import org.etlforge.application.ui.widget.StringListUi;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchOperator;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValueStringList;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.CaptionLayout;

public final class StringListValueUi extends ValueUi {
   private final StringListUi widget = new StringListUi(4);

   public StringListValueUi() {
      this.widget.setOrientation(StringListUi.Orientation.FIELD_ABOVE);
   }

   @Override
   public void decorate(UiCanvas var1) {
      String var2 = "Enter values in the field and click Add to add them to the list.";
      CaptionLayout.of(var2, this.widget).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.widget.requestFocus();
   }

   protected SearchFieldValueStringList constructValue(NetSuiteSearchOperator var1) {
      SearchFieldValueStringList var2 = new SearchFieldValueStringList(var1);

      for (String var5 : this.widget.getStringList()) {
         var2.add(var5);
      }

      return var2;
   }

   @Override
   public void setValue(SearchFieldValue var1) {
      SearchFieldValueStringList var2 = ArgCheck.checkInstance(var1, SearchFieldValueStringList.class);
      this.widget.setStringList(var2.getSearchValue());
   }
}
