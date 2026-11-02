  
                             
   
package org.etlforge.lang;

public final class LongParser {
    public static long parse(String string, long l) {
        if (string != null && !string.isEmpty()) {
            try {
                return Long.parseLong(string);
            }
            catch (NumberFormatException numberFormatException) {
                                    
            }
        }
        return l;
    }

    private LongParser() {
    }
}

