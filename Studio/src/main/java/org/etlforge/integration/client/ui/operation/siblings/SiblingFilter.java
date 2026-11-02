package org.etlforge.integration.client.ui.operation.siblings;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.HostedWebServiceOperationType;
import org.etlforge.integration.data.entity.operation.OperationFunctionInfo;
import org.etlforge.integration.data.function.Function;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.structure.DataStructure;

public class SiblingFilter implements EntityFilter {
   private final DataStructure inputStruct;

   public SiblingFilter(Operation var1) {
      this.inputStruct = this.extractInputStructure(var1);
   }

   public SiblingFilter(DataStructure var1) {
      this.inputStruct = var1;
   }

   @Override
   public boolean apply(IntegrationEntity var1) {
      if (this.inputStruct == null) {
         return false;
      }

      if (!(var1 instanceof Operation)) {
         return false;
      }

      Operation var2 = (Operation)var1;
      if (var2.getOperationType() == HostedWebServiceOperationType.TYPE) {
         return false;
      }

      DataStructure var3 = this.extractInputStructure(var2);
      return this.inputStruct.equals(var3);
   }

   private DataStructure extractInputStructure(Operation var1) {
      OperationFunctionInfo var2 = new OperationFunctionInfo(var1);
      Function var3 = var2.getFirstFunction();
      return var3 instanceof Transformation ? ((Transformation)var3).getInputStructure() : null;
   }
}
