package org.etlforge.integration.client.ui.operation.siblings;

import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.entity.IdToObjectUtils;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.project.ProjectDependencies;

public class SiblingOperationsInfo {
   private final IntegrationEntityLookup entityLookup;
   private final ProjectDependencies dependencies;

   public SiblingOperationsInfo(IntegrationEntityLookup var1, ProjectDependencies var2) {
      this.entityLookup = var1;
      this.dependencies = var2;
   }

   public List<Operation> getSiblingOperations(Operation var1) {
      return new ArrayList<>(IdToObjectUtils.getEntitiesFromIds(var1.getSiblingOperations(), this.entityLookup, Operation.class));
   }

   public List<Operation> getSiblingOperations(OperationWrapper var1) {
      return new ArrayList<>(IdToObjectUtils.getEntitiesFromIds(var1.getSiblingIds(), this.entityLookup, Operation.class));
   }

   public List<Operation> getTriggeringOperations(Operation var1) {
      ArrayList var2 = new ArrayList();

      for (IntegrationEntity var4 : this.dependencies.getDependingObjects(var1)) {
         if (var4 instanceof Operation && this.isSiblingTo(var1, (Operation)var4)) {
            var2.add((Operation)var4);
         }
      }

      return var2;
   }

   private boolean isSiblingTo(Operation var1, Operation var2) {
      for (OperationId var4 : var2.getSiblingOperations()) {
         if (var4.equals(var1.getID())) {
            return true;
         }
      }

      return false;
   }
}
