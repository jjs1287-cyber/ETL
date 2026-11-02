  
                             
   
package org.etlforge.lang;

public final class DoubleParser {
    public static double parse(String string, double d) {
        if (string != null && !string.isEmpty()) {
            try {
                return Double.parseDouble(string);
            }
            catch (NumberFormatException numberFormatException) {
                                    
            }
        }
        return d;
    }

    private DoubleParser() {
    }
}

