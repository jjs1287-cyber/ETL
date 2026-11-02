package org.etlforge.integration.client.ui.interchange.entity.operation.page;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraph2D;
import org.etlforge.integration.data.entity.operation.flow.OperationFlow;

public interface OperationGraphSaver {
   OperationGraphSaver READ_ONLY = new OperationGraphSaver() {
      @Override
      public void saveGraphSettings(OperationGraph2D var1) {
      }

      @Override
      public void saveGraphLogic(OperationFlow var1) {
      }
   };

   void saveGraphLogic(OperationFlow var1);

   void saveGraphSettings(OperationGraph2D var1);
}
