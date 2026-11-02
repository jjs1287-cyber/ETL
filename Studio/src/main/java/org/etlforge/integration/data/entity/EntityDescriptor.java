package org.etlforge.integration.data.entity;

import org.apache.commons.lang.StringUtils;

public final class EntityDescriptor {
   private final EntityType type;
   private final IntegrationEntityId id;
   private String name;

   public EntityDescriptor(IntegrationEntity var1) {
      this.type = var1.getEntityType();
      this.id = var1.getID();
   }

   public EntityDescriptor(EntityType var1, String var2) {
      this.type = var1;
      this.id = var1.getIntegrationId(var2);
   }

   public EntityDescriptor(String var1, String var2) {
      if (var1.equals("InterchangeProject")) {
         this.type = EntityType.Project;
      } else {
         this.type = EntityType.valueOf(var1);
      }

      this.id = this.type.getIntegrationId(var2);
   }

   public EntityType getEntityType() {
      return this.type;
   }

   public IntegrationEntityId getID() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
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
      return this.type == var2.type && this.id.equals(var2.id);
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 37 * var1 + this.type.hashCode();
      return 37 * var1 + this.id.hashCode();
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder(this.type.toString());
      if (StringUtils.isNotEmpty(this.name)) {
         var1.append(" [Name: ").append(this.name).append("] ");
      }

      var1.append(" [GUID: ").append(this.id).append("]");
      return var1.toString();
   }
}
