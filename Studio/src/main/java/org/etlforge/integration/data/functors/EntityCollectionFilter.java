package org.etlforge.integration.data.functors;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;

public final class EntityCollectionFilter implements EntityFilter {
   private final Multimap<EntityType, IntegrationEntity> typeMap = HashMultimap.create();
   private final Set<IntegrationEntity> items = Sets.newHashSet();
   private boolean containsWizards;

   public EntityCollectionFilter() {
      this(Collections.EMPTY_LIST);
   }

   public EntityCollectionFilter(Collection<? extends IntegrationEntity> var1) {
      this.addAll(var1);
   }

   public void addAll(Collection<? extends IntegrationEntity> var1) {
      for (IntegrationEntity var3 : var1) {
         this.add(var3);
      }
   }

   public void add(IntegrationEntity var1) {
      if (this.items.add(var1)) {
         EntityType var2 = EntityUtils.getItemType(var1);
         this.typeMap.put(var2, var1);
         if (var2.isWizardType()) {
            this.containsWizards = true;
         }

         for (IntegrationEntity var3 = var1.getParent(); var3 != null && !(var3 instanceof RootFolder); var3 = var3.getParent()) {
            this.typeMap.put(var2, var3);
         }
      }
   }

   @Override
   public boolean apply(IntegrationEntity var1) {
      return this.items.contains(var1);
   }

   public boolean hasMatchingType(Iterable<EntityType> var1) {
      for (EntityType var3 : var1) {
         if (this.typeMap.containsKey(var3)) {
            return true;
         }
      }

      return false;
   }

   public boolean containsWizards() {
      return this.containsWizards;
   }

   public boolean isEmpty() {
      return this.items.isEmpty();
   }

   public Set<EntityType> getAllTypes() {
      return Sets.newHashSet(this.typeMap.keySet());
   }

   public boolean containsSameTypes(EntityCollectionFilter var1) {
      if (this.typeMap.isEmpty()) {
         return var1.typeMap.isEmpty();
      }

      if (var1.typeMap.isEmpty()) {
         return false;
      }

      if (this.containsWizards != var1.containsWizards) {
         return false;
      }

      Set var2 = this.typeMap.keySet();
      Set var3 = var1.typeMap.keySet();
      return var2.equals(var3);
   }

   public Collection<IntegrationEntity> getItemsOfType(EntityType var1) {
      return this.typeMap.get(var1);
   }

   public Collection<IntegrationEntity> collectItemsOfTypes(Iterable<EntityType> var1) {
      Builder var2 = ImmutableSet.builder();

      for (EntityType var4 : var1) {
         var2.addAll(this.typeMap.get(var4));
      }

      return var2.build();
   }

   public static EntityCollectionFilter combine(EntityCollectionFilter... var0) {
      EntityCollectionFilter var1 = new EntityCollectionFilter();

      for (EntityCollectionFilter var5 : var0) {
         var1.addAll(var5.items);
      }

      return var1;
   }

   @Override
   public int hashCode() {
      return this.items.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof EntityCollectionFilter ? this.items.equals(((EntityCollectionFilter)var1).items) : false;
      }
   }
}
