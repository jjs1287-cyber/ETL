package org.etlforge.ui.selection;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public abstract class ListSelectionDispatcher implements ListSelectionListener {
   private final SelectionListeners externalListeners = new SelectionListeners();

   public final void addListener(SelectionListener var1) {
      this.externalListeners.add(var1);
   }

   public final void removeListener(SelectionListener var1) {
      this.externalListeners.remove(var1);
   }

   @Override
   public final void valueChanged(ListSelectionEvent var1) {
      if (!var1.getValueIsAdjusting()) {
         SelectionChangedEvent var2 = this.convertEvent(var1);
         this.externalListeners.call().selectionChanged(var2);
      }
   }

   private SelectionChangedEvent convertEvent(ListSelectionEvent var1) {
      Selection var2 = this.getSelection(var1);
      return new SelectionChangedEvent(var1.getSource(), var2);
   }

   protected abstract Selection getSelection(ListSelectionEvent var1);
}
