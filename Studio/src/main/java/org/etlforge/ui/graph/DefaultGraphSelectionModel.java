package org.etlforge.ui.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultGraphSelectionModel implements GraphSelectionModel {
   private Set<Object> currentSelection = new HashSet<>();
   private final List<GraphSelectionListener> listeners = new CopyOnWriteArrayList<>();

   @Override
   public void clearSelection() {
      if (!this.currentSelection.isEmpty()) {
         this.currentSelection.clear();
         this.fireSelectionChange();
      }
   }

   @Override
   public int getNumberOfSelectedObjects() {
      return this.currentSelection.size();
   }

   @Override
   public List<Object> getSelection() {
      return new ArrayList<>(this.currentSelection);
   }

   @Override
   public void addToSelection(Collection<?> var1) {
      if (var1 != null && !var1.isEmpty()) {
         HashSet var2 = new HashSet(var1);
         if (!this.currentSelection.containsAll(var2)) {
            this.currentSelection.addAll(var2);
            this.fireSelectionChange();
         }
      }
   }

   @Override
   public void flipSelection(Collection<?> var1) {
      if (var1 != null && !var1.isEmpty()) {
         for (Object var3 : var1) {
            if (this.currentSelection.contains(var3)) {
               this.currentSelection.remove(var3);
            } else {
               this.currentSelection.add(var3);
            }
         }

         this.fireSelectionChange();
      }
   }

   @Override
   public void removeFromSelection(Collection<?> var1) {
      if (var1 != null && !var1.isEmpty()) {
         this.currentSelection.removeAll(var1);
         this.fireSelectionChange();
      }
   }

   @Override
   public void setSelection(Collection<?> var1) {
      if (var1 != null && !var1.isEmpty()) {
         HashSet var2 = new HashSet(var1);
         if (this.currentSelection.equals(var2)) {
            return;
         }

         this.currentSelection = var2;
         this.fireSelectionChange();
      } else {
         this.clearSelection();
      }
   }

   @Override
   public boolean isSelected(Object var1) {
      return this.currentSelection.contains(var1);
   }

   @Override
   public void addSelectionListener(GraphSelectionListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public void removeSelectionListener(GraphSelectionListener var1) {
      this.listeners.remove(var1);
   }

   protected final void fireSelectionChange() {
      if (!this.listeners.isEmpty()) {
         GraphSelectionEvent var1 = new GraphSelectionEvent(this, this.currentSelection.toArray(new Object[this.currentSelection.size()]));

         for (GraphSelectionListener var3 : this.listeners) {
            var3.selectionChanged(var1);
         }
      }
   }
}
