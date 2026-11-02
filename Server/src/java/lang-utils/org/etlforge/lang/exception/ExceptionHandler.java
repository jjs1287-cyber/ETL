  
                             
   
package org.etlforge.lang.exception;

import org.etlforge.lang.exception.ExceptionHandlers;

public interface ExceptionHandler<T extends Throwable> {
    public static final ExceptionHandler<Throwable> STANDARD_ERR = ExceptionHandlers.toStandardErr();

    public void caught(T var1);
}

