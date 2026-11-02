  
                             
   
package org.etlforge.lang;

public final class Intervals {
    public static int valueWithin(int n, int n2, int n3) {
        return Math.max(Math.min(n3, n), n2);
    }

    public static double valueWithin(double d, double d2, double d3) {
        return Math.max(Math.min(d3, d), d2);
    }

    private Intervals() {
    }
}

