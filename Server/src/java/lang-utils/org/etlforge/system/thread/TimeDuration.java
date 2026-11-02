  
                             
   
                                        
                                  
                                        
   
package org.etlforge.system.thread;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import org.etlforge.system.ArgCheck;
import org.etlforge.system.thread.Timeout;

public final class TimeDuration
implements Serializable {
    private final long value;
    private final TimeUnit unit;

    public static TimeDuration milliseconds(long l) {
        return new TimeDuration(l, TimeUnit.MILLISECONDS);
    }

    public static TimeDuration seconds(long l) {
        return new TimeDuration(l, TimeUnit.SECONDS);
    }

    public TimeDuration(long l, TimeUnit timeUnit) {
        this.value = ArgCheck.checkPositive(l);
        this.unit = (TimeUnit)((Object)Preconditions.checkNotNull((Object)((Object)timeUnit)));
    }

    public long getValue() {
        return this.value;
    }

    public TimeUnit getUnit() {
        return this.unit;
    }

    public Timeout toTimeout() {
        return Timeout.fromDuration(this);
    }

    public void sleep() throws InterruptedException {
        this.unit.sleep(this.value);
    }

    public int hashCode() {
        return Objects.hashCode((Object[])new Object[]{this.value, this.unit});
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof TimeDuration) {
            TimeDuration timeDuration = (TimeDuration)object;
            return this.value == timeDuration.value && this.unit == timeDuration.unit;
        }
        return false;
    }

    public String toString() {
        return Objects.toStringHelper((Object)this).add("value", this.value).add("unit", (Object)this.unit).toString();
    }
}

