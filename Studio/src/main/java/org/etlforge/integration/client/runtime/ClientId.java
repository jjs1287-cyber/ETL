package org.etlforge.integration.client.runtime;

import java.util.UUID;
import org.etlforge.util.id.KongaID;

public final class ClientId extends KongaID {
   private final UUID id;

   private ClientId() {
      this.id = UUID.randomUUID();
   }

   private ClientId(String var1) {
      this.id = UUID.fromString(var1);
   }

   public static ClientId newClientId() {
      return new ClientId();
   }

   public static ClientId fromString(String var0) {
      return new ClientId(var0);
   }

   @Override
   public boolean equals(Object var1) {
      return var1 instanceof ClientId && this.id.equals(((ClientId)var1).id);
   }

   @Override
   public int hashCode() {
      return this.id.hashCode();
   }

   @Override
   public String toString() {
      return this.id.toString();
   }
}
