package org.etlforge.integration.server.engine;

import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.db.ServerDbException;

public abstract class SessionExecutionWrapper<S extends EngineSession<S>, R> {
   private final S session;

   protected SessionExecutionWrapper(S var1) {
      this.session = (S)var1;
   }

   public final R run() throws EngineSessionException, ServerAccessException, ServerDbException {
      this.session.setActive(true);

      try {
         return this.doWork(this.session);
      } finally {
         this.session.setActive(false);
      }
   }

   protected abstract R doWork(S var1) throws EngineSessionException, ServerAccessException, ServerDbException;
}
