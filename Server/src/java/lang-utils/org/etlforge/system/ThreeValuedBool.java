  
                             
   
package org.etlforge.system;

public enum ThreeValuedBool {
    TRUE,
    FALSE,
    MAYBE;


    public boolean isTrue() {
        return this == TRUE;
    }

    public boolean isFalse() {
        return this == FALSE;
    }

    public boolean isMaybe() {
        return this == MAYBE;
    }
}

