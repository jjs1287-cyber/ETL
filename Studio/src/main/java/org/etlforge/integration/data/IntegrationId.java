package org.etlforge.integration.data;

import org.etlforge.util.id.KongaGuid;
import org.etlforge.util.id.KongaID;

public abstract class IntegrationId extends KongaID {
   private final KongaGuid id;

   public IntegrationId() {
      this.id = KongaGuid.createGuid();
   }

   public IntegrationId(String var1) {
      this.id = KongaGuid.createGuid(var1);
   }

   @Override
   public boolean equals(Object var1) {
      return var1 instanceof IntegrationId ? this.id.equals(((IntegrationId)var1).id) : false;
   }

   @Override
   public int hashCode() {
      return this.id.hashCode();
   }

   @Override
   public String toString() {
      return this.id != null ? this.id.toString() : "";
   }

   public byte[] getBytes() {
      return this.id.getBytes();
   }
}
