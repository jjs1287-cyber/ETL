package org.etlforge.integration.client.ui.entity.search;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.Action;
import org.etlforge.integration.client.entity.search.SearchableEnumProperty;
import org.etlforge.integration.client.entity.search.SearchableProperty;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.widget.combobox.EnumComboBox;

public final class EnumPropertySearchField<E extends Enum<E>> implements PropertySearchField {
   private final SearchableProperty<E> property;
   private final EnumComboBox<E> selector;
   private final List<EnumPropertySearchField.Listener<E>> listeners;

   public EnumPropertySearchField(SearchableProperty<E> var1) {
      Preconditions.checkArgument(var1.getValueType().isEnum(), "Not an enum property: " + var1.getName());
      this.property = var1;
      this.selector = this.createSelector();
      this.selector.addItemListener(new EnumPropertySearchField.SelectionNotifier());
      this.listeners = new CopyOnWriteArrayList<>();
   }

   private EnumComboBox<E> createSelector() {
      if (this.property instanceof SearchableEnumProperty) {
         Set var1 = ((SearchableEnumProperty)this.property).getSupportedValues();
         return new EnumComboBox<>(var1);
      } else {
         return new EnumComboBox<>(this.property.getValueType());
      }
   }

   public E getValue() {
      return this.selector.getSelectedItem();
   }

   @Override
   public SearchableProperty getProperty() {
      return this.property;
   }

   @Override
   public Predicate<IntegrationEntity> getSearcher(SearchOptions var1) {
      if (!this.isEnabled()) {
         return SearchFieldUtils.ALL;
      }

      final Enum var2 = this.selector.getSelectedItem();
      return new Predicate<IntegrationEntity>() {
         public boolean apply(IntegrationEntity var1) {
            return EnumPropertySearchField.this.property.getValue(var1) == var2;
         }
      };
   }

   @Override
   public boolean isEnabled() {
      return this.selector.isEnabled();
   }

   @Override
   public void setEnabled(boolean var1) {
      if (var1 != this.isEnabled()) {
         this.selector.setEnabled(var1);

         for (EnumPropertySearchField.Listener var3 : this.listeners) {
            var3.enabledStateChanged(var1);
         }
      }
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

   public void addListener(EnumPropertySearchField.Listener<E> var1) {
      this.listeners.add(var1);
   }

   public void removeListener(EnumPropertySearchField.Listener<E> var1) {
      this.listeners.remove(var1);
   }

   public interface Listener<V extends Enum<V>> {
      void enabledStateChanged(boolean var1);

      void selectionChanged(V var1);
   }

   private class SelectionNotifier implements ItemListener {
      private E currentValue = EnumPropertySearchField.this.selector.getSelectedItem();

      private SelectionNotifier() {
      }

      @Override
      public void itemStateChanged(ItemEvent var1) {
         Enum var2 = EnumPropertySearchField.this.selector.getSelectedItem();
         if (var2 != this.currentValue) {
            this.currentValue = (E)var2;

            for (EnumPropertySearchField.Listener var4 : EnumPropertySearchField.this.listeners) {
               var4.selectionChanged(var2);
            }
         }
      }
   }
}
