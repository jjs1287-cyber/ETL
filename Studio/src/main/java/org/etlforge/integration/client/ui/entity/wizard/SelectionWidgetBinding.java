package org.etlforge.integration.client.ui.entity.wizard;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.util.Property;

final class SelectionWidgetBinding<T extends IntegrationEntity> extends AbstractBinding<T> {
   private final SelectionWidget<T> selector;
   private final SelectionListener uiListener = new SelectionListener() {
      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         SelectionWidgetBinding.this.syncModel();
      }
   };

   public SelectionWidgetBinding(SelectionWidget<T> var1, Property<T> var2) {
      super(var2);
      this.selector = var1;
      var1.addSelectionListener(this.uiListener);
   }

   protected void updateUiWithValue(T var1) {
      IntegrationEntityId var2 = var1 == null ? null : var1.getID();
      this.selector.setSelectedEntity(var2);
   }

   protected T getValueFromUi() {
      return this.selector.getSelectedEntity();
   }

   @Override
   protected void disposeUiListener() {
      this.selector.removeSelectionListener(this.uiListener);
   }
}
