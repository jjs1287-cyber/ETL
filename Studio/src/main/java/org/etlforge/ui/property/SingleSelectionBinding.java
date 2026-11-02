package org.etlforge.ui.property;

import org.etlforge.ui.selection.MutableSelectionSource;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public class SingleSelectionBinding<T> extends AbstractBinding<T> {
   private final SelectionSource selectionSource;
   private final Class<T> cls;
   private final SingleSelectionBinding<T>.UiListener uiListener;

   public static <T> SingleSelectionBinding<T> of(Property<T> var0, SelectionSource var1, Class<T> var2) {
      return new SingleSelectionBinding<>(var0, var1, var2);
   }

   public SingleSelectionBinding(AbstractPropertyBasedModel var1, String var2, SelectionSource var3, Class<T> var4) {
      this(var1.getProperty(var2), var3, var4);
   }

   public SingleSelectionBinding(Property<T> var1, SelectionSource var2, Class<T> var3) {
      super(var1);
      this.selectionSource = var2;
      this.cls = var3;
      this.uiListener = new SingleSelectionBinding.UiListener();
      var2.addSelectionListener(this.uiListener);
   }

   @Override
   protected final void updateUiWithValue(T var1) {
      if (this.selectionSource instanceof MutableSelectionSource) {
         ((MutableSelectionSource)this.selectionSource).setSelection(this.toSelection((T)var1));
      }
   }

   protected Selection toSelection(T var1) {
      return var1 == null ? OrderedSelection.EMPTY : SelectionFactory.newOrderedSelection(var1);
   }

   @Override
   protected T getValueFromUi() {
      Selection var1 = this.selectionSource.getSelection();

      for (Object var3 : var1.items()) {
         if (this.cls.isInstance(var3)) {
            return this.cls.cast(var3);
         }
      }

      return null;
   }

   @Override
   protected void disposeUiListener() {
      this.selectionSource.removeSelectionListener(this.uiListener);
   }

   private class UiListener implements SelectionListener {
      private UiListener() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         if (SingleSelectionBinding.this.isUiToModelEnabled()) {
            SingleSelectionBinding.this.syncModel();
         }
      }
   }
}
