package org.etlforge.integration.client.ui.project.search;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import javax.swing.Action;
import org.etlforge.string.StringMatcherPredicate;
import org.etlforge.string.StringMatchers;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaRadioButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextField;

final class NameFilterField implements UiProvider {
   private final KongaTextField field = new KongaTextField(20);
   private final KongaRadioButton startsWith;
   private final KongaRadioButton contains;

   public NameFilterField() {
      this.field.setSelectAllWhenFocused(true);
      this.startsWith = new KongaRadioButton("Starts &with", false);
      this.contains = new KongaRadioButton("&Contains", true);
      ButtonUtils.makeMutuallyExclusive(this.startsWith, this.contains);
   }

   public Predicate<String> getFilter() {
      String var1 = this.field.getText().trim();
      if (var1.isEmpty()) {
         return Predicates.alwaysTrue();
      }

      StringMatchers var2 = this.contains.isSelected() ? StringMatchers.CONTAINS_IGNORE_CASE : StringMatchers.STARTS_WITH_IGNORE_CASE;
      return new StringMatcherPredicate(var2, var1);
   }

   public void setSearchAction(Action var1) {
      this.field.addActionListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      KongaLabel var2 = KongaLabel.withMnemonic("(Optional) &Name filter:");
      var2.setLabelFor(this.field);
      Grid var3 = Grid.oneRow(10).of(this.startsWith, this.contains);
      TwoColumnPanel var4 = this.emptyGrid();
      var4.addRow(var2);
      var4.addRow(this.field, var3.container());
      var4.decorate(var1);
   }

   private TwoColumnPanel emptyGrid() {
      return new TwoColumnPanel()
         .withLeftInsets(0, 0, 2, 5)
         .withRightInsets(0, 0, 2, 0)
         .withLeftWeights(1.0, 0.0)
         .withLeftFill(GridBagLayoutPanel.Fill.HORIZONTAL);
   }

   @Override
   public void requestFocus() {
      this.field.requestFocusInWindow();
   }
}
