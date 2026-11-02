package org.etlforge.integration.data.entity;

public interface IntegrationEntityLookup {
   IntegrationEntityLookup NULL = new IntegrationEntityLookup() {
      @Override
      public <T extends IntegrationEntity> T getEntity(IntegrationEntityId var1, Class<T> var2) {
         return null;
      }

      @Override
      public IntegrationEntity getEntity(IntegrationEntityId var1) {
         return null;
      }
   };

   IntegrationEntity getEntity(IntegrationEntityId var1);

   <T extends IntegrationEntity> T getEntity(IntegrationEntityId var1, Class<T> var2);
}
