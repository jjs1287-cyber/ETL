  
                             
   
package org.etlforge.system.thread;

import java.util.concurrent.TimeUnit;

public interface Interruptible
extends Runnable {
    public static final int IDLE = 0;
    public static final int RUNNING = 1;
    public static final int STOPPING = 2;
    public static final int DONE = 4;
    public static final int COMPLETED = 8;
    public static final int INTERRUPTED = 16;
    public static final int FAILED = 32;

    public boolean stop();

    public void waitUntilStopped() throws InterruptedException;

    public void waitUntilStopped(long var1, TimeUnit var3) throws InterruptedException;

    public int status();
}

