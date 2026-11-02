package com.etlforge.integration.data.download.entity;

import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.Sets;
import com.etlforge.integration.data.download.InvalidDownloadedDataException;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.Set;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.string.StringMatcherPredicate;
import org.etlforge.string.WildcardStringMatcher;

public abstract class CustomEntityFactory<E extends IntegrationEntity> implements EntityFactory {
   private final EntityType entityType;
   private final Class<E> entityClass;

   protected CustomEntityFactory(EntityType var1, Class<E> var2) {
      this.entityType = var1;
      this.entityClass = var2;
   }

   @Override
   public final E createItem(StringKeyValuePairs var1) throws InvalidDownloadedDataException {
      IntegrationEntityId var2 = DownloadedDataUtils.getEntityId(var1, this.getIdKey(), this.entityType);
      String var3 = this.getName(var1);

      try {
         Constructor var4 = this.entityClass.getConstructor(var2.getClass(), String.class);
         IntegrationEntity var5 = (IntegrationEntity)var4.newInstance(var2, var3);
         var5.setPropertyValues(var1);
         this.customizeItem((E)var5, var1);
         this.removeRedundantKeys((E)var5);
         return (E)var5;
      } catch (Exception var6) {
         Throwables.propagateIfInstanceOf(var6, InvalidDownloadedDataException.class);
         throw Throwables.propagate(var6);
      }
   }

   protected void customizeItem(E var1, StringKeyValuePairs var2) throws InvalidDownloadedDataException {
   }

   protected final String getName(StringKeyValuePairs var1) throws InvalidDownloadedDataException {
      return DownloadedDataUtils.getRequiredValue(var1, this.getNameKey());
   }

   protected Set<String> getRequiredPropertyKeys() {
      return null;
   }

   protected abstract String getNameKey();

   protected abstract String getIdKey();

   private void removeRedundantKeys(E var1) {
      Set var2 = this.getRequiredPropertyKeys();
      if (var2 != null) {
         CustomEntityFactory.RedundantKeyRemover var3 = new CustomEntityFactory.RedundantKeyRemover(var2);
         var3.removeRedundantKeys(var1);
      }
   }

   private static class RedundantKeyRemover {
      private final Set<String> requiredKeys;
      private final Set<Predicate<String>> wildcards;

      public RedundantKeyRemover(Set<String> var1) {
         this.requiredKeys = var1;
         this.wildcards = Sets.newHashSet();
         this.identifyWildcards(var1);
      }

      private void identifyWildcards(Set<String> var1) {
         Iterator var2 = var1.iterator();

         while (var2.hasNext()) {
            String var3 = (String)var2.next();
            if (var3.contains("?") || var3.contains("*")) {
               WildcardStringMatcher var4 = new WildcardStringMatcher();
               var4.setLeadingMatchSufficient(false);
               this.wildcards.add(new StringMatcherPredicate(var4, var3));
               var2.remove();
            }
         }
      }

      public void removeRedundantKeys(IntegrationEntity var1) {
         StringKeyValuePairs var2 = var1.getKeyValues();

         for (String var4 : var2.getKeys()) {
            if (!this.requiredKeys.contains(var4) && !this.matchAgainstWildcards(var4)) {
               var2.remove(var4);
            }
         }
      }

      private boolean matchAgainstWildcards(String var1) {
         for (Predicate var3 : this.wildcards) {
            if (var3.apply(var1)) {
               return true;
            }
         }

         return false;
      }
   }
}
