package org.etlforge.lang.exception;

public interface ExceptionHandler<T extends Throwable> {
   ExceptionHandler<Throwable> STANDARD_ERR = ExceptionHandlers.toStandardErr();

   void caught(T var1);
}
