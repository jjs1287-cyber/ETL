package org.etlforge.integration.client.ui.connector.netsuite.search.wizard.field;

import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchOperator;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValueBoolean;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaRadioButton;

public final class BooleanValueUi extends ValueUi {
   private final KongaRadioButton trueBox = new KongaRadioButton("TRUE", true);
   private final KongaRadioButton falseBox = new KongaRadioButton("FALSE");

   public BooleanValueUi() {
      ButtonUtils.makeMutuallyExclusive(this.trueBox, this.falseBox);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = Horizontal.box().addAllWithSpace(16, this.trueBox, this.falseBox);
      Vertical.Anchor.north(var2).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.trueBox.requestFocusInWindow();
   }

   protected SearchFieldValueBoolean constructValue(NetSuiteSearchOperator var1) {
      return new SearchFieldValueBoolean(this.trueBox.isSelected());
   }

   @Override
   public void setValue(SearchFieldValue var1) {
      SearchFieldValueBoolean var2 = ArgCheck.checkInstance(var1, SearchFieldValueBoolean.class);
      if (var2.getSearchValue()) {
         this.trueBox.setSelected(true);
      } else {
         this.falseBox.setSelected(true);
      }
   }
}
