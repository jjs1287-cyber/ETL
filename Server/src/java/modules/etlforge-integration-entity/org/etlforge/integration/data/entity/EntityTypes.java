package org.etlforge.integration.data.entity;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.Iterator;

public final class EntityTypes implements Iterable<EntityType> {
   private final ImmutableSet<EntityType> types;

   public EntityTypes(Iterable<EntityType> var1, Iterable<EntityType> var2) {
      this.types = new Builder<EntityType>().addAll(var1).addAll(var2).build();
   }

   public Iterable<EntityType> wizardTypes() {
      return Iterables.filter(this.types, EntityTypeFilters.wizards());
   }

   @Override
   public Iterator<EntityType> iterator() {
      return this.types.iterator();
   }
}
