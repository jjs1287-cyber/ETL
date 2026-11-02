  
                             
   
package org.etlforge.system;

public class InternalMethodError
extends Error {
    public InternalMethodError() {
    }

    public InternalMethodError(String string) {
        super(string);
    }

    public InternalMethodError(Throwable throwable) {
        super(throwable);
    }

    public InternalMethodError(String string, Throwable throwable) {
        super(string, throwable);
    }
}

