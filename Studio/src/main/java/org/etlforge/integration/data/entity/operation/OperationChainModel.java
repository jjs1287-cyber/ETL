package org.etlforge.integration.data.entity.operation;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.project.ProjectDependencies;

public final class OperationChainModel {
   private final OperationChainModel.NodeImpl startNode;
   private final List<Operation> preceedingSuccess;
   private final List<Operation> preceedingFailure;

   OperationChainModel(Operation var1, IntegrationEntityLookup var2, ProjectDependencies var3) {
      this.startNode = new OperationChainModel.NodeImpl(null, var1);
      this.buildChain(this.startNode, var2);
      this.preceedingSuccess = Lists.newArrayList();
      this.preceedingFailure = Lists.newArrayList();
      this.collectPreceedingOperations(var1, var3);
   }

   public Operation getOperation() {
      return this.startNode.op;
   }

   public boolean isEmpty() {
      return this.startNode.success == null && this.startNode.failure == null && this.preceedingSuccess.isEmpty() && this.preceedingFailure.isEmpty();
   }

   public OperationChainModel.Node getFailureChain() {
      return this.startNode.failure;
   }

   public OperationChainModel.Node getSuccessChain() {
      return this.startNode.success;
   }

   public Iterable<Operation> getPreceedingSuccessOperations() {
      return this.preceedingSuccess;
   }

   public Iterable<Operation> getPreceedingFailureOperations() {
      return this.preceedingFailure;
   }

   private void buildChain(OperationChainModel.NodeImpl var1, IntegrationEntityLookup var2) {
      this.buildSuccessChain(var1, var2);
      this.buildFailureChain(var1, var2);
   }

   private void buildSuccessChain(OperationChainModel.NodeImpl var1, IntegrationEntityLookup var2) {
      Operation var3 = this.lookupSuccessOperation(var1.getOperation(), var2);
      if (var3 != null) {
         OperationChainModel.NodeImpl var4 = new OperationChainModel.NodeImpl(var1, var3);
         var1.success = var4;
         if (!var4.isRecursive()) {
            this.buildChain(var4, var2);
         }
      }
   }

   private void buildFailureChain(OperationChainModel.NodeImpl var1, IntegrationEntityLookup var2) {
      Operation var3 = this.lookupFailureOperation(var1.getOperation(), var2);
      if (var3 != null) {
         OperationChainModel.NodeImpl var4 = new OperationChainModel.NodeImpl(var1, var3);
         var1.failure = var4;
         if (!var4.isRecursive()) {
            this.buildChain(var4, var2);
         }
      }
   }

   private Operation lookupSuccessOperation(Operation var1, IntegrationEntityLookup var2) {
      return var2.getEntity(var1.getSuccessOperationId(), Operation.class);
   }

   private Operation lookupFailureOperation(Operation var1, IntegrationEntityLookup var2) {
      return var2.getEntity(var1.getFailureOperationId(), Operation.class);
   }

   private void collectPreceedingOperations(Operation var1, ProjectDependencies var2) {
      OperationId var3 = var1.getID();

      for (Operation var5 : var2.getDependingObjects(var1).filter(Operation.class)) {
         if (var3.equals(var5.getSuccessOperationId())) {
            this.preceedingSuccess.add(var5);
         }

         if (var3.equals(var5.getFailureOperationId())) {
            this.preceedingFailure.add(var5);
         }
      }
   }

   public interface Node {
      Operation getOperation();

      OperationChainModel.Node getSuccessNode();

      OperationChainModel.Node getFailureNode();

      boolean isRecursive();
   }

   private static class NodeImpl implements OperationChainModel.Node {
      private final Operation op;
      private final OperationChainModel.NodeImpl parent;
      public OperationChainModel.Node success;
      public OperationChainModel.Node failure;
      private final boolean recursive;

      public NodeImpl(OperationChainModel.NodeImpl var1, Operation var2) {
         this.op = var2;
         this.parent = var1;
         this.recursive = this.checkRecursive(var1);
      }

      @Override
      public Operation getOperation() {
         return this.op;
      }

      @Override
      public OperationChainModel.Node getFailureNode() {
         return this.failure;
      }

      @Override
      public OperationChainModel.Node getSuccessNode() {
         return this.success;
      }

      @Override
      public boolean isRecursive() {
         return this.recursive;
      }

      private boolean checkRecursive(OperationChainModel.NodeImpl var1) {
         while (var1 != null) {
            if (this.op == var1.op) {
               return true;
            }

            var1 = var1.parent;
         }

         return false;
      }
   }
}
