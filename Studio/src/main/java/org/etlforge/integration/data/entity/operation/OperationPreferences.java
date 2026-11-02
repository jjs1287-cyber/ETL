package org.etlforge.integration.data.entity.operation;

import org.etlforge.integration.data.entity.Operation;

public interface OperationPreferences {
   OperationPreferences NONE = new OperationPreferences() {
      @Override
      public void apply(Operation var1) {
      }
   };

   void apply(Operation var1);
}
