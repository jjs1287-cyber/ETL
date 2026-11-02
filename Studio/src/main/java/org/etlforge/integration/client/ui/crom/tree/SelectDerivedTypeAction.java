package org.etlforge.integration.client.ui.crom.tree;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;

public final class SelectDerivedTypeAction extends AbstractSingleObjectSelectionAction<CROM> {
   private final CromTreeWidget tree;

   public SelectDerivedTypeAction(CromTreeWidget var1) {
      super(CROM.class, "Select Derived Type...");
      this.tree = var1;
   }

   protected boolean isEnabledFor(CROM var1) {
      return var1.isDerivedTypeHolder();
   }

   protected void run(final CROM var1, ActionEvent var2) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SelectDerivedTypeAction.this.tree.selectDerivedType(var1);
         }
      });
   }
}
