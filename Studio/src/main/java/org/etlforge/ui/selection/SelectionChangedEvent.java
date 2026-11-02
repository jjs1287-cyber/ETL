package org.etlforge.ui.selection;

import java.util.EventObject;

public final class SelectionChangedEvent extends EventObject {
   private final Selection selection;

   public SelectionChangedEvent(Object var1, Selection var2) {
      super(var1);
      this.selection = var2;
   }

   public Selection getSelection() {
      return this.selection;
   }
}
