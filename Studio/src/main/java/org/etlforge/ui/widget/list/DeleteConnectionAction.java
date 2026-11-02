package org.etlforge.ui.widget.list;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import org.etlforge.ui.selection.AbstractSelectionAction;

public final class DeleteConnectionAction extends AbstractSelectionAction {
   public DeleteConnectionAction(ConnectedLists var1) {
      this("Delete Connection", var1);
   }

   public DeleteConnectionAction(String var1, ConnectedLists var2) {
      super(var1);
      Preconditions.checkNotNull(var2, "lists");
      this.setSelectionSource(var2);
   }

   @Override
   protected void handleSelectionChange() {
      ConnectedListsSelection var1 = this.getSelection();
      boolean var2 = var1.hasConnections();
      this.setEnabled(var2);
   }

   public ConnectedListsSelection getSelection() {
      return (ConnectedListsSelection)super.getSelection();
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      ConnectedListsSelection var2 = this.getSelection();

      for (ConnectedListsSelection.Connection var5 : var2.getCurrentConnections()) {
         var5.breakUp();
      }
   }
}
