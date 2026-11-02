package org.etlforge.integration.data.entity;

public interface ReferenceReplacementService {
   IntegrationEntity getReplacement(IntegrationEntityId var1);

   <T extends IntegrationEntity> T getReplacement(IntegrationEntityId var1, Class<T> var2);

   IntegrationEntityLookup getItemLookup();
}
