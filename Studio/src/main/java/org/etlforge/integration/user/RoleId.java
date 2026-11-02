package org.etlforge.integration.user;

import org.etlforge.util.id.KongaGuid;
import org.etlforge.util.id.KongaID;

public abstract class RoleId extends KongaID {
   private final KongaGuid id;

   protected RoleId() {
      this.id = KongaGuid.createGuid();
   }

   protected RoleId(String var1) {
      this.id = KongaGuid.createGuid(var1);
   }

   protected final KongaGuid getGuid() {
      return this.id;
   }

   @Override
   public String toString() {
      return this.id.toString();
   }
}
