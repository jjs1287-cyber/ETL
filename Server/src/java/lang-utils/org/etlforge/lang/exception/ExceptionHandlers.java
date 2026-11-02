  
                             
   
package org.etlforge.lang.exception;

import org.etlforge.lang.exception.ExceptionHandler;

public class ExceptionHandlers {
    public static <T extends Throwable> ExceptionHandler<T> toStandardErr() {
        return new ExceptionHandler<T>(){

            @Override
            public void caught(T t) {
                ((Throwable)t).printStackTrace();
            }
        };
    }

    private ExceptionHandlers() {
    }
}

