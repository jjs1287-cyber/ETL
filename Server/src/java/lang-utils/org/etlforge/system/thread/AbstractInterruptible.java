  
                             
   
package org.etlforge.system.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.etlforge.system.thread.Interruptible;

public abstract class AbstractInterruptible
implements Interruptible {
    private final CountDownLatch latch = new CountDownLatch(1);
    private boolean stopHasBeenGranted;
    private int status;
    private Throwable uncaughtException;

    public AbstractInterruptible() {
        this.setStatus(0);
    }

    protected abstract void runImpl();

    @Override
    public final synchronized int status() {
        return this.status;
    }

    @Override
    public final synchronized boolean stop() {
        if (this.stopHasBeenGranted) {
            return true;
        }
        if (this.canBeStopped()) {
            this.stopHasBeenGranted = true;
            if (!this.isInState(4)) {
                this.appendStatus(2);
            }
            return true;
        }
        return false;
    }

    protected abstract boolean canBeStopped();

    protected final synchronized boolean isStopping() {
        return this.isInState(2);
    }

    @Override
    public final void waitUntilStopped() throws InterruptedException {
        this.latch.await();
    }

    @Override
    public final void waitUntilStopped(long l, TimeUnit timeUnit) throws InterruptedException {
        this.latch.await(l, timeUnit);
    }

      
                                                                         
       
    @Override
    public final void run() {
        try {
            AbstractInterruptible abstractInterruptible = this;
            synchronized (abstractInterruptible) {
                block22: {
                    if (!this.isStopping()) break block22;
                    return;
                }
                this.setStatus(1);
            }
            this.runImpl();
        }
        catch (Throwable throwable) {
            this.uncaughtException = throwable;
            this.appendStatus(32);
        }
        finally {
            AbstractInterruptible abstractInterruptible = this;
            synchronized (abstractInterruptible) {
                this.revokeState(1);
                this.revokeState(2);
                this.appendStatus(4);
                this.latch.countDown();
            }
        }
    }

    public Throwable getUncaughtException() {
        if (!this.isInState(8)) {
            throw new IllegalStateException("This method cannot be called until this runnable has completed.");
        }
        return this.uncaughtException;
    }

    protected final synchronized void setCompleted() {
        this.appendStatus(8);
        this.revokeState(2);
    }

    protected final synchronized void setInterrupted() {
        this.appendStatus(16);
        this.revokeState(2);
    }

    private synchronized void setStatus(int n) {
        this.status = n;
    }

    private synchronized void appendStatus(int n) {
        this.status |= n;
    }

    private synchronized void revokeState(int n) {
        this.status ^= n;
    }

    private synchronized boolean isInState(int n) {
        return (this.status & n) != 0;
    }

    public static void main(String[] stringArray) {
        int n = 0;
        n ^= 0;
        assert ((n |= 1) == 1);
        n |= 2;
        n |= 0x10;
        n ^= 1;
        assert ((n ^= 0) == 18);
        n ^= 2;
        assert ((n |= 4) == 20);
    }
}

