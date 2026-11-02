  
                             
   
package org.etlforge.system.thread;

import java.util.concurrent.TimeUnit;

public interface Condition {
    public static final Condition ALWAYS_TRUE = new Condition(){

        @Override
        public void waitFor() throws InterruptedException {
        }

        @Override
        public boolean waitFor(long l, TimeUnit timeUnit) throws InterruptedException {
            return true;
        }
    };

    public void waitFor() throws InterruptedException;

    public boolean waitFor(long var1, TimeUnit var3) throws InterruptedException;
}

