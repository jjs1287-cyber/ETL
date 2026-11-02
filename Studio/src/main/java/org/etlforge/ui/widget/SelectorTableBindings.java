package org.etlforge.ui.widget;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.util.AbstractPropertyBasedModel;

@ParametersAreNonnullByDefault
public final class SelectorTableBindings<E> {
   private final AbstractPropertyBasedModel model;
   private final String availableFieldsPropertyName;
   private final String selectedFieldsPropertyName;
   private final Optional<String> disabledFieldsPropertyName;

   public SelectorTableBindings(AbstractPropertyBasedModel var1, String var2, String var3) {
      this(var1, var2, var3, null);
   }

   public SelectorTableBindings(AbstractPropertyBasedModel var1, String var2, String var3, @Nullable String var4) {
      this.model = Preconditions.checkNotNull(var1, "model");
      this.availableFieldsPropertyName = ArgCheck.checkNotEmpty(var2, "availableFieldsPropertyName");
      this.selectedFieldsPropertyName = ArgCheck.checkNotEmpty(var3, "selectedFieldsPropertyName");
      this.disabledFieldsPropertyName = Optional.fromNullable(var4);
   }

   public Bindings install(SelectorTable<E> var1) {
      Preconditions.checkNotNull(var1, "table");
      Bindings var2 = Bindings.of(
         new SelectorTableBindings.AvailableFieldsBinding(var1, this.model, this.availableFieldsPropertyName),
         new SelectorTableBindings.SelectedFieldsBinding(var1, this.model, this.selectedFieldsPropertyName)
      );
      if (this.disabledFieldsPropertyName.isPresent()) {
         var2.add(new SelectorTableBindings.DisabledFieldsBinding(var1, this.model, this.disabledFieldsPropertyName.get()));
      }

      return var2;
   }

   public Bindings installAndSyncUi(SelectorTable<E> var1) {
      Bindings var2 = this.install(var1);
      var2.syncUi();
      return var2;
   }

   private static class AvailableFieldsBinding<E> extends AbstractBinding<List<E>> {
      private final SelectorTable<E> table;

      public AvailableFieldsBinding(SelectorTable<E> var1, AbstractPropertyBasedModel var2, String var3) {
         super(var2, var3);
         this.table = var1;
      }

      protected void updateUiWithValue(List<E> var1) {
         this.table.replaceValues(var1);
      }
   }

   private static class DisabledFieldsBinding<E> extends AbstractBinding<List<E>> {
      private final SelectorTable<E> table;

      public DisabledFieldsBinding(SelectorTable<E> var1, AbstractPropertyBasedModel var2, String var3) {
         super(var2, var3);
         this.table = var1;
      }

      protected void updateUiWithValue(List<E> var1) {
         this.table.setDisabledRows(var1);
      }
   }

   private static class SelectedFieldsBinding<E> extends AbstractBinding<List<E>> implements SelectionListener {
      private final SelectorTable<E> table;

      public SelectedFieldsBinding(SelectorTable<E> var1, AbstractPropertyBasedModel var2, String var3) {
         super(var2, var3);
         this.table = var1;
         var1.asSelectionSource().addSelectionListener(this);
      }

      protected void updateUiWithValue(List<E> var1) {
         this.table.setSelectedObjects(var1);
      }

      protected List<E> getValueFromUi() {
         return this.table.getSelectedObjects();
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         if (this.isUiToModelEnabled()) {
            this.syncModel();
         }
      }

      @Override
      protected void disposeUiListener() {
         this.table.asSelectionSource().removeSelectionListener(this);
      }
   }
}
