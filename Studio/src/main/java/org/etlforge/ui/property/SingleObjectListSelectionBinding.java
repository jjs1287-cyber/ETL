package org.etlforge.ui.property;

import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.util.Property;

public class SingleObjectListSelectionBinding<T> extends AbstractBinding<T> {
   private final KongaList list;
   private final SingleObjectListSelectionBinding<T>.UiListener uiListener;

   public SingleObjectListSelectionBinding(Property<T> var1, KongaList var2) {
      super(var1);
      this.list = var2;
      this.uiListener = new SingleObjectListSelectionBinding.UiListener();
      var2.addSelectionListener(this.uiListener);
   }

   @Override
   protected void updateUiWithValue(T var1) {
      OrderedSelection var2 = var1 == null ? OrderedSelection.EMPTY : SelectionFactory.newOrderedSelection(var1);
      this.list.setSelection(var2);
   }

   @Override
   protected T getValueFromUi() {
      IndexedSelection var1 = this.list.getSelection();
      return (T)(!var1.isEmpty() ? var1.head() : null);
   }

   @Override
   protected void disposeUiListener() {
      this.list.removeSelectionListener(this.uiListener);
   }

   private class UiListener implements SelectionListener {
      private UiListener() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         if (SingleObjectListSelectionBinding.this.isUiToModelEnabled()) {
            SingleObjectListSelectionBinding.this.syncModel();
         }
      }
   }
}
