package org.etlforge.system.thread;

import java.util.concurrent.TimeUnit;

public interface Interruptible extends Runnable {
   int IDLE = 0;
   int RUNNING = 1;
   int STOPPING = 2;
   int DONE = 4;
   int COMPLETED = 8;
   int INTERRUPTED = 16;
   int FAILED = 32;

   boolean stop();

   void waitUntilStopped() throws InterruptedException;

   void waitUntilStopped(long var1, TimeUnit var3) throws InterruptedException;

   int status();
}
