package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.prefs.AutomaticRenamePreference;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.ui.DelayedJob;

public final class NewOperationProcessor {
   private final OperationGraphController controller;

   public NewOperationProcessor(OperationGraphController var1) {
      this.controller = var1;
   }

   public void process(Operation var1) {
      OperationWrapperNode var2 = this.controller.getGraph().getModel().getOperationNode(var1);
      this.renameNode(var2);
   }

   private void renameNode(final OperationWrapperNode var1) {
      DelayedJob.createAndSchedule(100, new Runnable() {
         @Override
         public void run() {
            if (AutomaticRenamePreference.INSTANCE.get()) {
               NewOperationProcessor.this.controller.renameNode(var1);
            }
         }
      });
   }
}
