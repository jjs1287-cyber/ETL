package org.etlforge.integration.client.ui.entity.search;

import com.google.common.base.Predicate;
import javax.swing.Action;
import javax.swing.JRadioButton;
import org.etlforge.integration.client.entity.search.SearchableProperty;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaRadioButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class BooleanPropertySearchField implements PropertySearchField {
   private final SearchableProperty<Boolean> property;
   private final JRadioButton yes;
   private final JRadioButton no;

   public BooleanPropertySearchField(SearchableProperty<Boolean> var1) {
      this.property = var1;
      this.yes = new KongaRadioButton("Yes", true);
      this.no = new KongaRadioButton("No", false);
      ButtonUtils.makeMutuallyExclusive(this.yes, this.no);
   }

   @Override
   public SearchableProperty getProperty() {
      return this.property;
   }

   public Boolean getValue() {
      return this.yes.isSelected();
   }

   public void setValue(boolean var1) {
      if (var1) {
         this.yes.setSelected(true);
      } else {
         this.no.setSelected(true);
      }
   }

   @Override
   public Predicate<IntegrationEntity> getSearcher(SearchOptions var1) {
      if (!this.isEnabled()) {
         return SearchFieldUtils.ALL;
      }

      final boolean var2 = this.yes.isSelected();
      return new Predicate<IntegrationEntity>() {
         public boolean apply(IntegrationEntity var1) {
            return BooleanPropertySearchField.this.property.getValue(var1) == var2;
         }
      };
   }

   @Override
   public boolean isEnabled() {
      return this.yes.isEnabled();
   }

   @Override
   public void setEnabled(boolean var1) {
      this.yes.setEnabled(var1);
      this.no.setEnabled(var1);
   }

   @Override
   public void setSearchAction(Action var1) {
      this.yes.addActionListener(var1);
      this.no.addActionListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      Grid var2 = Grid.oneRow(20).addAll(this.yes, this.no);
      var1.display(InvisiblePanel.of(var2));
   }

   @Override
   public void requestFocus() {
      this.yes.requestFocusInWindow();
   }
}
