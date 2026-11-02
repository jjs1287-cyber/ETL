package org.etlforge.ui.selection;

import com.google.common.base.Preconditions;

public class SelectionSourceConverter implements SelectionSource {
   private final SelectionSource wrappee;
   private final SelectionConverter converter;
   private final SelectionListeners listeners;
   private Selection selection;

   public SelectionSourceConverter(SelectionSource var1, SelectionConverter var2) {
      this.wrappee = Preconditions.checkNotNull(var1, "wrappee");
      this.converter = Preconditions.checkNotNull(var2, "converter");
      this.listeners = new SelectionListeners();
      this.wrappee.addSelectionListener(new SelectionSourceConverter.Dispatcher());
      this.selection = this.convertWrappeeSelection(var1.getSelection());
   }

   @Override
   public Selection getSelection() {
      return this.selection;
   }

   public void setSelection(Selection var1) {
      if (!(this.wrappee instanceof MutableSelectionSource)) {
         throw new UnsupportedOperationException("The wrapped SelectionSource does not support this method.");
      }

      this.selection = var1 != null ? var1 : Selection.EMPTY;
      ((MutableSelectionSource)this.wrappee).setSelection(this.convertExternalSelection(this.selection));
      this.listeners.call().selectionChanged(new SelectionChangedEvent(this, var1));
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.listeners.remove(var1);
   }

   private void dispatchSelection(Selection var1) {
      this.selection = var1;
      this.listeners.call().selectionChanged(new SelectionChangedEvent(this, var1));
   }

   private Selection convertExternalSelection(Selection var1) {
      if (var1.isEmpty()) {
         return var1;
      } else if (var1 instanceof IndexedSelection) {
         return this.convertExternal((IndexedSelection)var1);
      } else {
         return var1 instanceof OrderedSelection ? this.convertExternal((OrderedSelection)var1) : new SimpleSelection(var1);
      }
   }

   private IndexedSelection convertExternal(IndexedSelection var1) {
      int[] var2 = var1.indeces();
      Object[] var3 = this.convertExternalObjects(var1);
      return SelectionFactory.newIndexedSelection(var3, var2);
   }

   private OrderedSelection convertExternal(OrderedSelection var1) {
      Object[] var2 = this.convertExternalObjects(var1);
      return SelectionFactory.newOrderedSelection(var2);
   }

   private Object[] convertExternalObjects(OrderedSelection var1) {
      int var2 = var1.size();
      Object[] var3 = new Object[var2];
      Object[] var4 = var1.toArray();

      for (int var5 = 0; var5 < var2; var5++) {
         var3[var5] = this.converter.toSelection(var4[var5]);
      }

      return var3;
   }

   private Selection convertWrappeeSelection(Selection var1) {
      if (var1.isEmpty()) {
         return var1;
      } else if (var1 instanceof IndexedSelection) {
         return this.convertWrappee((IndexedSelection)var1);
      } else {
         return var1 instanceof OrderedSelection ? this.convertWrappee((OrderedSelection)var1) : new SimpleSelection(var1);
      }
   }

   private IndexedSelection convertWrappee(IndexedSelection var1) {
      int[] var2 = var1.indeces();
      Object[] var3 = this.convertWrappeeObjects(var1);
      return SelectionFactory.newIndexedSelection(var3, var2);
   }

   private OrderedSelection convertWrappee(OrderedSelection var1) {
      Object[] var2 = this.convertWrappeeObjects(var1);
      return SelectionFactory.newOrderedSelection(var2);
   }

   private Object[] convertWrappeeObjects(OrderedSelection var1) {
      int var2 = var1.size();
      Object[] var3 = new Object[var2];
      Object[] var4 = var1.toArray();

      for (int var5 = 0; var5 < var2; var5++) {
         var3[var5] = this.converter.fromSelection(var4[var5]);
      }

      return var3;
   }

   private class Dispatcher implements SelectionListener {
      private Dispatcher() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         Selection var2 = var1.getSelection();
         Selection var3 = SelectionSourceConverter.this.convertWrappeeSelection(var2);
         SelectionSourceConverter.this.dispatchSelection(var3);
      }
   }
}
