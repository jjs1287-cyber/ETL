package org.etlforge.integration.client.deploy.history;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.client.deploy.DeployedEntityDescriptor;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.util.persist.Persistor;

final class DeployedEntityDescriptorPersistor {
   private static final String DESCRIPTOR = "EntityDescriptor";
   private static final String ENTITY_TYPE = "EntityType";
   private static final String ENTITY_NAME = "EntityName";
   private static final String ENTITY_ID = "EntityId";

   void persist(DeployedEntityDescriptor var1, Persistor var2) {
      Persistor var3 = var2.createChild("EntityDescriptor");
      IntegrationEntityId var4 = var1.getId();
      var3.putString("EntityType", var4.getEntityType().name());
      var3.putString("EntityId", var4.toString());
      var3.putString("EntityName", var1.getName());

      for (DeployedEntityDescriptor var6 : var1.getChildren()) {
         this.persist(var6, var3);
      }
   }

   DeployedEntityDescriptor restore(Persistor var1) {
      Persistor var2 = var1.getFirstChild("EntityDescriptor");
      if (var2 == null) {
         throw new IllegalArgumentException("Invalid persistor");
      }

      DeployedEntityDescriptorPersistor.DescriptorImpl var3 = new DeployedEntityDescriptorPersistor.DescriptorImpl(var2);
      this.restoreChildren(var3, var2);
      return var3;
   }

   private void restoreChildren(DeployedEntityDescriptorPersistor.DescriptorImpl var1, Persistor var2) {
      for (Persistor var4 : var2.getChildren("EntityDescriptor")) {
         DeployedEntityDescriptorPersistor.DescriptorImpl var5 = new DeployedEntityDescriptorPersistor.DescriptorImpl(var4);
         var1.addChild(var5);
         this.restoreChildren(var5, var4);
      }
   }

   private static class DescriptorImpl implements DeployedEntityDescriptor {
      private final IntegrationEntityId id;
      private final String name;
      private final List<DeployedEntityDescriptor> children;

      public DescriptorImpl(Persistor var1) {
         String var2 = var1.getString("EntityType");
         EntityType var3;
         if (var2.equals("InterchangeProject")) {
            var3 = EntityType.Project;
         } else {
            var3 = EntityType.valueOf(var2);
         }

         this.id = var3.getIntegrationId(var1.getString("EntityId"));
         this.name = var1.getString("EntityName");
         this.children = Lists.newArrayList();
      }

      @Override
      public Iterable<DeployedEntityDescriptor> getChildren() {
         return this.children;
      }

      @Override
      public EntityType getType() {
         return this.id.getEntityType();
      }

      @Override
      public IntegrationEntityId getId() {
         return this.id;
      }

      @Override
      public String getName() {
         return this.name;
      }

      public void addChild(DeployedEntityDescriptorPersistor.DescriptorImpl var1) {
         this.children.add(var1);
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else {
            return var1 instanceof DeployedEntityDescriptor ? this.id.equals(((DeployedEntityDescriptor)var1).getId()) : false;
         }
      }

      @Override
      public int hashCode() {
         return this.id.hashCode();
      }

      @Override
      public String toString() {
         return this.name;
      }
   }
}
