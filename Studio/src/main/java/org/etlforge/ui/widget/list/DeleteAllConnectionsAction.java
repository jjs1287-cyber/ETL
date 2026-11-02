package org.etlforge.ui.widget.list;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public final class DeleteAllConnectionsAction extends AbstractAction {
   private final ConnectedLists lists;

   public DeleteAllConnectionsAction(ConnectedLists var1) {
      this("Delete All Connections", var1);
   }

   public DeleteAllConnectionsAction(String var1, ConnectedLists var2) {
      super(var1);
      Preconditions.checkNotNull(var2, "lists");
      this.lists = var2;
      var2.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent var1) {
            DeleteAllConnectionsAction.this.updateEnabledState();
         }
      });
      this.updateEnabledState();
   }

   private void updateEnabledState() {
      boolean var1 = this.lists.getConnectionCount() > 0;
      this.setEnabled(var1);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.lists.removeAllConnections();
   }
}
