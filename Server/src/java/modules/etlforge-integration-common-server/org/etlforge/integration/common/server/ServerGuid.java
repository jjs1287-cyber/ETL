package org.etlforge.integration.common.server;

import com.google.common.base.Preconditions;
import org.etlforge.util.id.KongaGuid;
import org.etlforge.util.id.KongaID;

public final class ServerGuid extends KongaID {
   private static final long serialVersionUID = 4245797536937362732L;
   private final KongaGuid guid;

   public ServerGuid(String var1) {
      Preconditions.checkNotNull(var1, "s");
      this.guid = KongaGuid.createGuid(var1);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof ServerGuid ? this.guid.equals(((ServerGuid)var1).guid) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.guid.hashCode();
   }

   @Override
   public String toString() {
      return this.guid.toString();
   }
}
