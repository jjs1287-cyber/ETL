package com.etlforge.integration.client.project.share.history;

import org.etlforge.integration.data.entity.EntityPath;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.util.persist.Persistor;

public final class EntityDescriptor {
   private final String path;
   private final IntegrationEntityId guid;

   static EntityDescriptor restore(Persistor var0, EntityType var1) {
      if (var1 == null) {
         var1 = restoreType(var0);
         if (var1 == null) {
            throw new IllegalArgumentException("No EntityType was given and the Persistor does not contain this information");
         }
      }

      String var2 = var0.getString("guid");
      String var3 = var0.getString("path");
      if (var2 != null && var3 != null) {
         try {
            return new EntityDescriptor(var1.getIntegrationId(var2), var3);
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

      return null;
   }

   private static EntityType restoreType(Persistor var0) {
      String var1 = var0.getString("type");
      if (var1 != null) {
         try {
            return EntityType.valueOf(var1);
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

      return null;
   }

   EntityDescriptor(IntegrationEntity var1) {
      this(var1.getID(), new EntityPath(var1, false).toString());
   }

   private EntityDescriptor(IntegrationEntityId var1, String var2) {
      this.guid = var1;
      this.path = var2;
   }

   public final EntityType getType() {
      return this.guid.getEntityType();
   }

   public final String getPath() {
      return this.path;
   }

   public final IntegrationEntityId getGuid() {
      return this.guid;
   }

   @Override
   public int hashCode() {
      int var2 = 1;
      var2 = 31 * var2 + this.guid.hashCode();
      return 31 * var2 + this.path.hashCode();
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
      return this.guid.equals(var2.guid) && this.path.equals(var2.path);
   }

   void writeTo(Persistor var1, boolean var2) {
      if (var2) {
         var1.putString("type", this.getType().name());
      }

      var1.putString("path", this.path);
      var1.putString("guid", this.guid.toString());
   }
}
