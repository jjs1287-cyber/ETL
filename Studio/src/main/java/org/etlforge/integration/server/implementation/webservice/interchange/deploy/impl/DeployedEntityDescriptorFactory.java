package org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.client.deploy.DeployedEntityDescriptor;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.project.IntegrationProject;

class DeployedEntityDescriptorFactory {
   DeployedEntityDescriptor create(IntegrationProject var1) {
      DeployedEntityDescriptorFactory.DescriptorImpl var2 = new DeployedEntityDescriptorFactory.DescriptorImpl(var1);
      this.buildTree(var1, var2);
      return var2;
   }

   private void buildTree(IntegrationEntity var1, DeployedEntityDescriptorFactory.DescriptorImpl var2) {
      for (IntegrationEntity var4 : var1.getAllChildren()) {
         if (this.includeBranch(var4)) {
            DeployedEntityDescriptorFactory.DescriptorImpl var5 = new DeployedEntityDescriptorFactory.DescriptorImpl(var4);
            var2.addChild(var5);
            this.buildTree(var4, var5);
         }
      }
   }

   private boolean includeBranch(IntegrationEntity var1) {
      return var1.getEntityType() == EntityType.RootFolder ? var1.getNumberOfChildren() > 0 : true;
   }

   private static class DescriptorImpl implements DeployedEntityDescriptor {
      private final IntegrationEntity entity;
      private final List<DeployedEntityDescriptor> children;

      public DescriptorImpl(IntegrationEntity var1) {
         this.entity = var1;
         this.children = Lists.newArrayList();
      }

      @Override
      public Iterable<DeployedEntityDescriptor> getChildren() {
         return this.children;
      }

      @Override
      public EntityType getType() {
         return this.getId().getEntityType();
      }

      @Override
      public IntegrationEntityId getId() {
         return this.entity.getID();
      }

      @Override
      public String getName() {
         return this.entity.getName();
      }

      public void addChild(DeployedEntityDescriptor var1) {
         this.children.add(var1);
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else {
            return var1 instanceof DeployedEntityDescriptor ? this.getId().equals(((DeployedEntityDescriptor)var1).getId()) : false;
         }
      }

      @Override
      public int hashCode() {
         return this.getId().hashCode();
      }
   }
}
