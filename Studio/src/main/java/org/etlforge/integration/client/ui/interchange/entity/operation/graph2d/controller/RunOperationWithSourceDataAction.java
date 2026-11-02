package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;

final class RunOperationWithSourceDataAction extends AbstractAction {
   private final OperationRunnerFactory runnerFactory;
   private final OperationWrapperNode opNode;

   public RunOperationWithSourceDataAction(OperationRunnerFactory var1, OperationWrapperNode var2) {
      super("Run With Source Data...");
      this.runnerFactory = var1;
      this.opNode = var2;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(
         new Runnable() {
            @Override
            public void run() {
               OperationSourceDataRunner var1x = new OperationSourceDataRunner(
                  RunOperationWithSourceDataAction.this.runnerFactory, RunOperationWithSourceDataAction.this.opNode
               );
               var1x.run();
            }
         }
      );
   }
}
