package org.etlforge.integration.data.entity.operation.pipeline;

public interface OperationFunctionCollector {
   OperationFunctionCollector NONE = new OperationFunctionCollector() {
      @Override
      public OperationFunctions collect(OperationPipeline var1) {
         return OperationFunctions.NONE;
      }
   };

   OperationFunctions collect(OperationPipeline var1);
}
