package org.etlforge.integration.client.ui.connector.netsuite.search.wizard.field;

import java.awt.event.ActionListener;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchOperator;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValueStringTwo;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.text.KongaTextField;

final class TwoStringsValueUi extends ValueUi {
   private final KongaTextField searchBox1 = new KongaTextField(16);
   private final KongaTextField searchBox2 = new KongaTextField(16);

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = Horizontal.box().addAllWithSpace(10, this.searchBox1, this.searchBox2);
      Vertical.Anchor.north(var2).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.searchBox1.requestFocusInWindow();
   }

   protected SearchFieldValueStringTwo constructValue(NetSuiteSearchOperator var1) {
      return new SearchFieldValueStringTwo(var1, this.searchBox1.getText().trim(), this.searchBox2.getText().trim());
   }

   @Override
   public void setValue(SearchFieldValue var1) {
      SearchFieldValueStringTwo var2 = ArgCheck.checkInstance(var1, SearchFieldValueStringTwo.class);
      this.searchBox1.setText(var2.getSearchValue());
      this.searchBox2.setText(var2.getSearchValue2());
   }

   public void setEnabled(boolean var1) {
      this.searchBox1.setEnabled(var1);
      this.searchBox2.setEnabled(var1);
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
}
