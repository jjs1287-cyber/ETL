package org.etlforge.integration.activity.serverapi.history;

import java.util.Random;

public final class MockSourceHistoryProvider implements SourceHistoryProvider {
   private static final Random random = new Random();

   @Override
   public void getSourceHistory(HistoryRequest var1, GetSourceHistoryCallback var2) {
      var2.caught(new UnsupportedOperationException());
   }

   @Override
   public void deleteSourceHistoryEntries(long[] var1, DeleteSourceHistoryCallback var2) {
      try {
         Thread.sleep(500L);
         if (random.nextInt(40) < 38) {
            var2.entriesWereDeleted(var1);
         } else {
            var2.caught(new MockSourceHistoryProvider.MockException());
         }
      } catch (InterruptedException var4) {
         Thread.currentThread().interrupt();
         var4.printStackTrace();
         var2.caught(var4);
      }
   }

   private static class MockException extends Exception {
      public MockException() {
         super("This is a mock error.");
      }
   }
}
