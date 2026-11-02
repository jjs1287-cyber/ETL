package org.etlforge.integration.server.idmappings;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.EnumMap;
import java.util.Map;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.lang.exception.NotImplementedYetException;

public final class CachedGuidToIdMapper implements GuidToIdMapper {
   private final GuidToIdMapper decoratee;
   private final EnumMap<EntityType, CachedGuidToIdMapper.IdMaps> cachedIds = Maps.newEnumMap(EntityType.class);
   private final Object lock = new Object();

   public CachedGuidToIdMapper(GuidToIdMapper var1) {
      Preconditions.checkNotNull(var1, "mapper");
      this.decoratee = var1;
   }

   @Override
   public void deleteItem(EntityType var1, long var2) throws GuidToIdMappingException {
      throw new NotImplementedYetException();
   }

   @Override
   public void deleteItem(IntegrationEntityId var1) throws GuidToIdMappingException {
      throw new NotImplementedYetException();
   }

   @Override
   public IntegrationEntityId getGuid(EntityType var1, long var2) throws GuidToIdMappingException {
      CachedGuidToIdMapper.IdMaps var4 = this.cachedIds.get(var1);
      synchronized (this.lock) {
         IntegrationEntityId var6 = var4.getGuid(var2);
         if (var6 == null) {
            var6 = this.decoratee.getGuid(var1, var2);
            var4.store(var2, var6);
         }

         return var6;
      }
   }

   @Override
   public Long getId(IntegrationEntityId var1) throws GuidToIdMappingException {
      CachedGuidToIdMapper.IdMaps var2 = this.cachedIds.get(var1.getEntityType());
      synchronized (this.lock) {
         Long var4 = var2.getId(var1);
         if (var4 == null) {
            var4 = this.decoratee.getId(var1);
            var2.store(var4, var1);
         }

         return var4;
      }
   }

   @Override
   public void setGuidForItem(EntityType var1, long var2, IntegrationEntityId var4) throws GuidToIdMappingException {
      throw new NotImplementedYetException();
   }

   private static class IdMaps {
      private final Map<Long, IntegrationEntityId> idToGuid = Maps.newHashMap();
      private final Map<IntegrationEntityId, Long> guidToId = Maps.newHashMap();

      public void store(Long var1, IntegrationEntityId var2) {
         this.idToGuid.put(var1, var2);
         this.guidToId.put(var2, var1);
      }

      public IntegrationEntityId getGuid(long var1) {
         return this.idToGuid.get(var1);
      }

      public Long getId(IntegrationEntityId var1) {
         return this.guidToId.get(var1);
      }
   }
}
