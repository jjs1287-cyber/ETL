package org.etlforge.integration.server.data;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.EntityType;

public final class EntityDescriptor {
   private final EntityType type;
   private final long databaseId;

   public EntityDescriptor(EntityType var1, long var2) {
      Preconditions.checkNotNull(var1, "type");
      this.type = var1;
      this.databaseId = var2;
   }

   public EntityType getEntityType() {
      return this.type;
   }

   public long getDatabaseId() {
      return this.databaseId;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof EntityDescriptor)) {
         return false;
      }

      EntityDescriptor var2 = (EntityDescriptor)var1;
      return this.type == var2.type && this.databaseId == var2.databaseId;
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.type.hashCode();
      return 31 * var1 + Long.valueOf(this.databaseId).hashCode();
   }

   @Override
   public String toString() {
      return "[Type: " + this.type + " - DB ID: " + this.databaseId + "]";
   }
}
