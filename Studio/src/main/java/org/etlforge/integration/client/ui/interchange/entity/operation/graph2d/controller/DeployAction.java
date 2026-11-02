package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.SwingWorker;
import org.etlforge.integration.client.ui.interchange.core.action.InterchangeActionDictionary;

public final class DeployAction extends AbstractAction {
   private final OperationGraphController controller;

   public DeployAction(OperationGraphController var1) {
      super("Deploy All", getIcon());
      this.controller = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            DeployAction.this.setEnabled(false);
            DeployAction.this.new Worker().execute();
         }
      });
   }

   private static final Icon getIcon() {
      return InterchangeActionDictionary.DICTIONARY.getSmallIcon("action.ic.DeploySelectedObjects");
   }

   private final class Worker extends SwingWorker<Void, Void> {
      private Worker() {
      }

      protected Void doInBackground() throws Exception {
         DeployAction.this.controller.deployMainOperation();
         return null;
      }

      @Override
      protected void done() {
         DeployAction.this.setEnabled(true);
      }
   }
}
