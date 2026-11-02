  
                             
   
package org.etlforge.system;

import org.etlforge.system.Clock;

public final class MockClock
extends Clock {
    private volatile long time;

    public static MockClock now() {
        return new MockClock(System.currentTimeMillis());
    }

    public MockClock() {
        this(0L);
    }

    public MockClock(long l) {
        this.time = l;
    }

    public void set(long l) {
        this.time = l;
    }

    @Override
    public long currentTime() {
        return this.time;
    }
}

