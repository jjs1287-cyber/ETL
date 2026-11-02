package org.etlforge.integration.data.functors;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.io.Serializable;
import java.util.Arrays;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class EntityFilters {
   public static final EntityFilter ACCEPTS_ALL = new EntityFilter() {
      @Override
      public boolean apply(IntegrationEntity var1) {
         return true;
      }

      @Override
      public String toString() {
         return "ACCEPTS_ALL";
      }
   };
   public static final EntityFilter REJECTS_ALL = new EntityFilter() {
      @Override
      public boolean apply(IntegrationEntity var1) {
         return false;
      }

      @Override
      public String toString() {
         return "REJECTS_ALL";
      }
   };
   public static final EntityFilter DEPLOY_DIRTY = new EntityFilter() {
      @Override
      public boolean apply(IntegrationEntity var1) {
         return var1 != null && var1.isDeployDirty();
      }

      @Override
      public String toString() {
         return "DEPLOY_DIRTY";
      }
   };
   public static final EntityFilter DELETED = new EntityFilter() {
      @Override
      public boolean apply(IntegrationEntity var1) {
         return var1 != null && var1.isDeleted();
      }

      @Override
      public String toString() {
         return "DELETED";
      }
   };
   public static final EntityFilter DELETABLE = new EntityFilter() {
      @Override
      public boolean apply(IntegrationEntity var1) {
         return var1 != null && var1.isDeletable();
      }

      @Override
      public String toString() {
         return "DELETABLE";
      }
   };
   public static final EntityFilter EDITABLE = new EntityFilter() {
      @Override
      public boolean apply(IntegrationEntity var1) {
         return var1.isEditable();
      }

      @Override
      public String toString() {
         return "EDITABLE";
      }
   };
   public static final EntityFilter BUILT_IN = new EntityFilter() {
      @Override
      public boolean apply(IntegrationEntity var1) {
         return var1 instanceof BuiltInIntegrationEntity;
      }

      @Override
      public String toString() {
         return "BUILT_IN";
      }
   };
   public static final EntityFilter WIZARDS = new EntityFilter() {
      @Override
      public boolean apply(IntegrationEntity var1) {
         return EntityUtils.getItemType(var1).isWizardType();
      }

      @Override
      public String toString() {
         return "WIZARDS";
      }
   };

   public static EntityFilter allOfClass(Class<? extends IntegrationEntity> var0) {
      Preconditions.checkNotNull(var0);
      return new EntityFilters.ClassFilter(var0);
   }

   public static EntityFilter allOfType(EntityType var0) {
      Preconditions.checkNotNull(var0, "type");
      return new EntityFilters.TypeFilter(var0);
   }

   public static EntityFilter oneOfTypes(EntityType... var0) {
      return new EntityFilters.OneOfTypesFilter(Arrays.asList(var0));
   }

   public static EntityFilter oneOfTypes(Iterable<EntityType> var0) {
      return new EntityFilters.OneOfTypesFilter(var0);
   }

   public static EntityFilter and(EntityFilter... var0) {
      return new CompositeEntityFilter(var0);
   }

   public static EntityFilter or(Iterable<? extends EntityFilter> var0) {
      return new EntityFilters.OrFilter(var0);
   }

   public static EntityFilter or(EntityFilter var0, EntityFilter var1, EntityFilter... var2) {
      return new EntityFilters.OrFilter(var0, var1, var2);
   }

   public static EntityFilter foldersOfType(final EntityType var0) {
      Preconditions.checkNotNull(var0, "type");
      return new EntityFilter() {
         @Override
         public boolean apply(IntegrationEntity var1) {
            return var1 instanceof Folder && ((Folder)var1).getItemType() == var0;
         }

         @Override
         public String toString() {
            return "Folders containing " + var0.getPlural();
         }
      };
   }

   public static EntityFilter nameFilter(final Predicate<String> var0) {
      return new EntityFilter() {
         @Override
         public boolean apply(IntegrationEntity var1) {
            return var0.apply(var1.getName());
         }
      };
   }

   private EntityFilters() {
   }

   private static class ClassFilter implements EntityFilter, Serializable {
      private final Class<? extends IntegrationEntity> itemClass;

      public ClassFilter(Class<? extends IntegrationEntity> var1) {
         this.itemClass = var1;
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         return this.itemClass.isInstance(var1);
      }

      @Override
      public String toString() {
         return "All of Class " + this.itemClass.getName();
      }
   }

   private static class OneOfTypesFilter implements EntityFilter, Serializable {
      private final ImmutableSet<EntityType> types;

      public OneOfTypesFilter(Iterable<EntityType> var1) {
         this.types = ImmutableSet.copyOf(var1);
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         return var1 != null && this.types.contains(var1.getEntityType());
      }

      @Override
      public String toString() {
         return "One of types " + this.types;
      }
   }

   private static class OrFilter implements EntityFilter, Serializable {
      private final ImmutableSet<EntityFilter> filters;

      public OrFilter(EntityFilter var1, EntityFilter var2, EntityFilter... var3) {
         this.filters = new Builder<EntityFilter>().add(var1).add(var2).add(var3).build();
      }

      public OrFilter(Iterable<? extends EntityFilter> var1) {
         this.filters = ImmutableSet.copyOf(var1);
         Preconditions.checkArgument(!this.filters.isEmpty(), "Must provide at least one filter");
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         for (EntityFilter var3 : this.filters) {
            if (var3.apply(var1)) {
               return true;
            }
         }

         return false;
      }

      @Override
      public String toString() {
         return Joiner.on(" OR ").join(this.filters);
      }
   }

   private static class TypeFilter implements EntityFilter, Serializable {
      private final EntityType type;

      public TypeFilter(EntityType var1) {
         this.type = var1;
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         return var1 != null && var1.getEntityType() == this.type;
      }

      @Override
      public String toString() {
         return "All of type " + this.type;
      }
   }
}
