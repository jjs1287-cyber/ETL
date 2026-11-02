package org.etlforge.integration.server.engine;

import java.util.UUID;
import org.etlforge.util.id.KongaID;

public abstract class EngineSessionId<S extends EngineSession> extends KongaID {
   private final UUID guid;

   protected EngineSessionId() {
      this.guid = UUID.randomUUID();
   }

   protected EngineSessionId(String var1) {
      this.guid = UUID.fromString(var1);
   }

   @Override
   public final boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 != null && var1.getClass().equals(this.getClass()) ? this.guid.equals(((EngineSessionId)var1).guid) : false;
      }
   }

   @Override
   public final int hashCode() {
      return this.guid.hashCode();
   }

   @Override
   public final String toString() {
      return this.guid.toString();
   }
}
