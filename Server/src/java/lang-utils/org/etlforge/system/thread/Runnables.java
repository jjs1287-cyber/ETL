  
                             
   
package org.etlforge.system.thread;

import java.io.Serializable;

public final class Runnables {
    public static final Runnable NO_OP = new NoOp();

    private Runnables() {
    }

    private static final class NoOp
    implements Runnable,
    Serializable {
        private NoOp() {
        }

        @Override
        public void run() {
        }

        public String toString() {
            return "no-op";
        }
    }
}

