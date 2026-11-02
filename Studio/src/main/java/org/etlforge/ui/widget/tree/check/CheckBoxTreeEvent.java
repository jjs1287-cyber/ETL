package org.etlforge.ui.widget.tree.check;

import java.util.EventObject;

public final class CheckBoxTreeEvent extends EventObject {
   private final CheckBoxNode node;

   public CheckBoxTreeEvent(Object var1, CheckBoxNode var2) {
      super(var1);
      this.node = var2;
   }

   public CheckBoxNode getNode() {
      return this.node;
   }
}
