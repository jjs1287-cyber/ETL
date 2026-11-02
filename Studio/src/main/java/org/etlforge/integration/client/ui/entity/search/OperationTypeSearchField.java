package org.etlforge.integration.client.ui.entity.search;

import com.google.common.base.Predicate;
import javax.swing.Action;
import javax.swing.JComboBox;
import org.etlforge.integration.client.entity.search.SearchableProperty;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.ui.UiCanvas;

final class OperationTypeSearchField implements PropertySearchField {
   private final SearchableProperty<OperationType> property;
   private final JComboBox selector;

   public OperationTypeSearchField(SearchableProperty<OperationType> var1) {
      this.property = var1;
      this.selector = new JComboBox<>(OperationType.values().toArray());
      this.selector.setEnabled(false);
   }

   @Override
   public SearchableProperty getProperty() {
      return this.property;
   }

   @Override
   public Predicate<IntegrationEntity> getSearcher(SearchOptions var1) {
      final OperationType var2 = this.getValue();
      return this.isEnabled() && var2 != null ? new Predicate<IntegrationEntity>() {
         public boolean apply(IntegrationEntity var1) {
            return ((Operation)var1).getOperationType() == var2;
         }
      } : SearchFieldUtils.ALL;
   }

   public OperationType getValue() {
      return (OperationType)this.selector.getSelectedItem();
   }

   @Override
   public boolean isEnabled() {
      return this.selector.isEnabled();
   }

   @Override
   public void setEnabled(boolean var1) {
      this.selector.setEnabled(var1);
   }

   @Override
   public void setSearchAction(Action var1) {
      this.selector.addActionListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.selector);
   }

   @Override
   public void requestFocus() {
      this.selector.requestFocusInWindow();
   }
}
