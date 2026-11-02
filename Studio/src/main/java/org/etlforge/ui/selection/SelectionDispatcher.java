package org.etlforge.ui.selection;

public final class SelectionDispatcher implements SelectionListener {
   private final SelectionListeners externalListeners = new SelectionListeners();
   private boolean blocked;

   public void addListener(SelectionListener var1) {
      this.externalListeners.add(var1);
   }

   public void removeListener(SelectionListener var1) {
      this.externalListeners.remove(var1);
   }

   public void setBlocked(boolean var1) {
      this.blocked = var1;
   }

   public boolean isBlocked() {
      return this.blocked;
   }

   @Override
   public void selectionChanged(SelectionChangedEvent var1) {
      if (!this.blocked) {
         this.externalListeners.call().selectionChanged(var1);
      }
   }
}
