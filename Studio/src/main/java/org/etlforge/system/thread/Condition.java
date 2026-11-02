package org.etlforge.system.thread;

import java.util.concurrent.TimeUnit;

public interface Condition {
   Condition ALWAYS_TRUE = new Condition() {
      @Override
      public void waitFor() throws InterruptedException {
      }

      @Override
      public boolean waitFor(long var1, TimeUnit var3) throws InterruptedException {
         return true;
      }
   };

   void waitFor() throws InterruptedException;

   boolean waitFor(long var1, TimeUnit var3) throws InterruptedException;
}
