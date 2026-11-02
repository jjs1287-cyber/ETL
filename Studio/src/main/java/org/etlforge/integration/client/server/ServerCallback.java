package org.etlforge.integration.client.server;

import org.etlforge.lang.exception.ExceptionHandler;

public interface ServerCallback extends ExceptionHandler<Throwable> {
   @Override
   void caught(Throwable var1);

   void cancelled();
}
