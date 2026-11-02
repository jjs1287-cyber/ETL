package org.etlforge.integration.data.entity.operation;

import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.link.AbstractEntityLink;

final class OperationOperationLink extends AbstractEntityLink {
   private final Runnable runWhenMainIsDeleted;

   private OperationOperationLink(Operation var1, Operation var2, Runnable var3) {
      super(var1, var2);
      this.runWhenMainIsDeleted = var3;
   }

   static OperationOperationLink createSuccessLink(Operation var0, Operation var1) {
      return new OperationOperationLink(var0, var1, new OperationOperationLink.SuccessOperationDeletedJob(var1));
   }

   static OperationOperationLink createFailureLink(Operation var0, Operation var1) {
      return new OperationOperationLink(var0, var1, new OperationOperationLink.FailureOperationDeletedJob(var1));
   }

   static OperationOperationLink createSuccessAndFailureLink(Operation var0, Operation var1) {
      return new OperationOperationLink(var0, var1, new OperationOperationLink.SuccessAndFailureOperationDeletedJob(var1));
   }

   public Operation getDependantObject() {
      return (Operation)super.getDependantObject();
   }

   public Operation getMainObject() {
      return (Operation)super.getMainObject();
   }

   @Override
   public boolean isStrong() {
      return false;
   }

   @Override
   protected void mainObjectDeleted() {
      this.runWhenMainIsDeleted.run();
      super.mainObjectDeleted();
   }

   private static class FailureOperationDeletedJob implements Runnable {
      private final Operation operation;

      public FailureOperationDeletedJob(Operation var1) {
         this.operation = var1;
      }

      @Override
      public void run() {
         this.operation.setFailureOperationId(null);
      }
   }

   private static class SuccessAndFailureOperationDeletedJob implements Runnable {
      private final Operation operation;

      public SuccessAndFailureOperationDeletedJob(Operation var1) {
         this.operation = var1;
      }

      @Override
      public void run() {
         this.operation.setSuccessOperationId(null);
         this.operation.setFailureOperationId(null);
      }
   }

   private static class SuccessOperationDeletedJob implements Runnable {
      private final Operation operation;

      public SuccessOperationDeletedJob(Operation var1) {
         this.operation = var1;
      }

      @Override
      public void run() {
         this.operation.setSuccessOperationId(null);
      }
   }
}
