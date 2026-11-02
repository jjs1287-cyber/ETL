package org.etlforge.ui.selection;

public class SelectionChangeSupport {
   private final Object source;
   private final SelectionListeners listeners;
   private Selection currentSelection;

   public SelectionChangeSupport(Object var1) {
      this(var1, Selection.EMPTY);
   }

   public SelectionChangeSupport(Object var1, Selection var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("src cannot be null");
      }

      if (var2 == null) {
         throw new IllegalArgumentException("initial cannot be null");
      }

      this.source = var1;
      this.listeners = new SelectionListeners();
      this.currentSelection = var2;
   }

   public void addListener(SelectionListener var1) {
      this.listeners.add(var1);
   }

   public void removeListener(SelectionListener var1) {
      this.listeners.remove(var1);
   }

   public void removeAllListeners() {
      this.listeners.removeAll();
   }

   public Selection getCurrentSelection() {
      return this.currentSelection;
   }

   public void setNewSelection(Selection var1) {
      this.currentSelection = var1 != null ? var1 : Selection.EMPTY;
      this.listeners.call().selectionChanged(new SelectionChangedEvent(this.source, this.currentSelection));
   }
}
