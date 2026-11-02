package org.etlforge.integration.server.idmappings;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public interface GuidToIdMapper {
   Long getId(IntegrationEntityId var1) throws GuidToIdMappingException;

   IntegrationEntityId getGuid(EntityType var1, long var2) throws GuidToIdMappingException;

   void setGuidForItem(EntityType var1, long var2, IntegrationEntityId var4) throws GuidToIdMappingException;

   void deleteItem(EntityType var1, long var2) throws GuidToIdMappingException;

   void deleteItem(IntegrationEntityId var1) throws GuidToIdMappingException;
}
