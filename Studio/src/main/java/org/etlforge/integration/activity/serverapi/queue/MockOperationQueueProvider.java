package org.etlforge.integration.activity.serverapi.queue;

import java.util.Random;

public final class MockOperationQueueProvider implements OperationQueueProvider {
   private static final Random random = new Random();

   @Override
   public void getOperationQueue(OperationQueueRequest var1, GetOperationQueueCallback var2) {
      var2.caught(new UnsupportedOperationException());
   }

   @Override
   public void deleteQueueEntries(long[] var1, DeleteQueueEntriesCallback var2) {
      try {
         Thread.sleep(500L);
         if (random.nextInt(40) < 38) {
            var2.entriesWereDeleted(var1);
         } else {
            var2.caught(new MockOperationQueueProvider.MockException("This is a mock exception"));
         }
      } catch (InterruptedException var4) {
         Thread.currentThread().interrupt();
         var4.printStackTrace();
         var2.caught(var4);
      }
   }

   @Override
   public void deleteSystemQueue(DeleteSystemQueueCallback var1) {
      try {
         Thread.sleep(500L);
         if (random.nextInt(40) < 38) {
            var1.systemQueueWasDeleted();
         } else {
            var1.caught(new MockOperationQueueProvider.MockException("This is a mock exception"));
         }
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
         var3.printStackTrace();
         var1.caught(var3);
      }
   }

   private static class MockException extends Exception {
      public MockException(String var1) {
         super(var1);
      }
   }
}
