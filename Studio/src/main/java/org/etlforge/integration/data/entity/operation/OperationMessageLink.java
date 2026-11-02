package org.etlforge.integration.data.entity.operation;

import org.etlforge.integration.data.entity.Message;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.link.AbstractEntityLink;

final class OperationMessageLink extends AbstractEntityLink {
   private final Runnable runWhenMessageDeleted;

   private OperationMessageLink(Message var1, Operation var2, Runnable var3) {
      super(var1, var2);
      this.runWhenMessageDeleted = var3;
   }

   static OperationMessageLink createSuccessLink(Message var0, Operation var1) {
      return new OperationMessageLink(var0, var1, new OperationMessageLink.SuccessMessageDeletedJob(var1));
   }

   static OperationMessageLink createFailureLink(Message var0, Operation var1) {
      return new OperationMessageLink(var0, var1, new OperationMessageLink.FailureMessageDeletedJob(var1));
   }

   static OperationMessageLink createSuccessAndFailureLink(Message var0, Operation var1) {
      return new OperationMessageLink(var0, var1, new OperationMessageLink.SuccessAndFailureMessageDeletedJob(var1));
   }

   public Message getMainObject() {
      return (Message)super.getMainObject();
   }

   public Operation getDependantObject() {
      return (Operation)super.getDependantObject();
   }

   @Override
   public boolean isStrong() {
      return false;
   }

   @Override
   protected void mainObjectDeleted() {
      this.runWhenMessageDeleted.run();
      super.mainObjectDeleted();
   }

   private static class FailureMessageDeletedJob implements Runnable {
      private final Operation operation;

      public FailureMessageDeletedJob(Operation var1) {
         this.operation = var1;
      }

      @Override
      public void run() {
         this.operation.setFailureMessageId(null);
      }
   }

   private static class SuccessAndFailureMessageDeletedJob implements Runnable {
      private final Operation operation;

      public SuccessAndFailureMessageDeletedJob(Operation var1) {
         this.operation = var1;
      }

      @Override
      public void run() {
         this.operation.setSuccessMessageId(null);
         this.operation.setFailureMessageId(null);
      }
   }

   private static class SuccessMessageDeletedJob implements Runnable {
      private final Operation operation;

      public SuccessMessageDeletedJob(Operation var1) {
         this.operation = var1;
      }

      @Override
      public void run() {
         this.operation.setSuccessMessageId(null);
      }
   }
}
