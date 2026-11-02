package org.etlforge.ui.widget.tree;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public final class DeleteAllConnectionsAction extends AbstractAction {
   private final ConnectedTrees trees;

   public DeleteAllConnectionsAction(ConnectedTrees var1) {
      this("Delete All Connections", var1);
   }

   public DeleteAllConnectionsAction(String var1, ConnectedTrees var2) {
      super(var1);
      this.trees = var2;
      var2.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent var1) {
            DeleteAllConnectionsAction.this.updateEnabledState();
         }
      });
      this.updateEnabledState();
   }

   private void updateEnabledState() {
      boolean var1 = this.trees.getConnectionCount() > 0;
      this.setEnabled(var1);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            DeleteAllConnectionsAction.this.remove();
         }
      });
   }

   private void remove() {
      this.trees.removeAllConnections();
   }
}
