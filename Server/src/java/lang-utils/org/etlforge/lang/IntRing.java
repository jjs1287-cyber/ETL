  
                             
   
                                        
                                        
   
package org.etlforge.lang;

import com.google.common.base.Preconditions;
import java.util.Iterator;
import org.etlforge.system.ArgCheck;

public final class IntRing
implements Iterable<Integer> {
    public static final Direction FORWARD = Direction.FORWARD;
    public static final Direction BACKWARD = Direction.BACKWARD;
    private final int direction;
    private final int length;

    public static IntRing forward(int n) {
        return new IntRing(FORWARD, n);
    }

    public static IntRing backward(int n) {
        return new IntRing(BACKWARD, n);
    }

    public IntRing(Direction direction, int n) {
        Preconditions.checkNotNull((Object)((Object)direction));
        this.direction = direction == Direction.FORWARD ? 1 : -1;
        this.length = ArgCheck.checkPositive(n, "length");
    }

    public int length() {
        return this.length;
    }

    public int next(int n) {
        Preconditions.checkArgument((n >= -1 && n < this.length ? 1 : 0) != 0, (Object)("start must be -1, or in the range [0, " + this.length + ")"));
        if (n == -1) {
            return this.first();
        }
        int n2 = n + this.direction;
        if (n2 < 0) {
            n2 = this.length - 1;
        } else if (n2 >= this.length) {
            n2 = 0;
        }
        return n2;
    }

    public int first() {
        return this.direction == 1 ? 0 : this.length - 1;
    }

    public int last() {
        return this.direction == 1 ? this.length - 1 : 0;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>(){
            private boolean started;
            private int value;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Integer next() {
                if (this.started) {
                    this.value = IntRing.this.next(this.value);
                } else {
                    this.value = IntRing.this.first();
                    this.started = true;
                }
                return this.value;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static enum Direction {
        FORWARD,
        BACKWARD;


        public IntRing newRing(int n) {
            return new IntRing(this, n);
        }
    }
}

