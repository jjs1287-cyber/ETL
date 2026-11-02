package org.etlforge.ui.widget.tree;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import org.etlforge.ui.selection.AbstractSelectionAction;

public final class DeleteConnectionAction extends AbstractSelectionAction {
   public DeleteConnectionAction(ConnectedTrees var1) {
      this("Delete Connection", var1);
   }

   public DeleteConnectionAction(String var1, ConnectedTrees var2) {
      super(var1);
      Preconditions.checkNotNull(var2, "trees");
      this.setSelectionSource(var2);
   }

   @Override
   protected void handleSelectionChange() {
      ConnectedTreesSelection var1 = this.getSelection();
      boolean var2 = var1.hasConnections();
      this.setEnabled(var2);
   }

   public ConnectedTreesSelection getSelection() {
      return (ConnectedTreesSelection)super.getSelection();
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      ConnectedTreesSelection var2 = this.getSelection();
      var2.breakAllConnections();
   }
}
