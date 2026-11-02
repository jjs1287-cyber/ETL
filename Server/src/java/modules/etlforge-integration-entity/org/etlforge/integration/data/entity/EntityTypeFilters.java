package org.etlforge.integration.data.entity;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.io.Serializable;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class EntityTypeFilters {
   public static Predicate<EntityType> is(EntityType var0) {
      return Predicates.equalTo(Preconditions.checkNotNull(var0, "type"));
   }

   public static Predicate<EntityType> oneOf(Iterable<EntityType> var0) {
      return new EntityTypeFilters.OneOf(var0);
   }

   public static Predicate<EntityType> oneOf(EntityType var0, EntityType var1, EntityType... var2) {
      return new EntityTypeFilters.OneOf(var0, var1, var2);
   }

   public static Predicate<EntityType> wizards() {
      return new EntityTypeFilters.Wizards();
   }

   public static Predicate<EntityType> nonWizards() {
      return new EntityTypeFilters.NoWizards();
   }

   private EntityTypeFilters() {
   }

   private static final class NoWizards implements Predicate<EntityType>, Serializable {
      private NoWizards() {
      }

      public boolean apply(@Nullable EntityType var1) {
         return var1 != null && !var1.isWizardType();
      }

      @Override
      public String toString() {
         return "Non-wizard types";
      }
   }

   private static final class OneOf implements Predicate<EntityType>, Serializable {
      private final ImmutableSet<EntityType> types;

      public OneOf(Iterable<EntityType> var1) {
         this.types = ImmutableSet.copyOf(var1);
      }

      public OneOf(EntityType var1, EntityType var2, EntityType... var3) {
         this.types = new Builder<EntityType>().add(var1).add(var2).add(var3).build();
      }

      public boolean apply(@Nullable EntityType var1) {
         return var1 != null && this.types.contains(var1);
      }

      @Override
      public String toString() {
         return "One of " + this.types;
      }
   }

   private static final class Wizards implements Predicate<EntityType>, Serializable {
      private Wizards() {
      }

      public boolean apply(@Nullable EntityType var1) {
         return var1 != null && var1.isWizardType();
      }

      @Override
      public String toString() {
         return "Wizard types";
      }
   }
}
