  
                             
   
                                        
                                     
   
package org.etlforge.lang.exception;

import com.google.common.base.Throwables;
import org.etlforge.lang.exception.ExceptionHandler;

public final class ExceptionUtils {
    public static String getStackTrace(Throwable throwable) {
        return Throwables.getStackTraceAsString((Throwable)throwable);
    }

    public static <T extends Throwable> void handle(T t, ExceptionHandler<? super T> exceptionHandler) {
        if (exceptionHandler == null) {
            t.printStackTrace();
        } else {
            exceptionHandler.caught(t);
        }
    }

    private ExceptionUtils() {
    }
}

