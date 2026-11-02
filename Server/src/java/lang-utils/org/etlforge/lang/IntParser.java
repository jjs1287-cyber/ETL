  
                             
   
package org.etlforge.lang;

public final class IntParser {
    public static int parse(String string, int n) {
        if (string != null && !string.isEmpty()) {
            try {
                return Integer.parseInt(string);
            }
            catch (NumberFormatException numberFormatException) {
                                    
            }
        }
        return n;
    }

    private IntParser() {
    }
}

