package org.etlforge.integration.data.script;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.lang.reflect.Constructor;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.system.ArgCheck;

public final class ProjectItemArgDescriptor {
   private final String prefix;
   private final Class<? extends IntegrationEntityId> idClass;
   private final Class<? extends IntegrationEntity> dataClass;

   public static com.google.common.base.Function<ProjectItemArgDescriptor, String> toPrefix() {
      return new com.google.common.base.Function<ProjectItemArgDescriptor, String>() {
         public String apply(ProjectItemArgDescriptor var1) {
            return var1.getPrefix();
         }
      };
   }

   static com.google.common.base.Function<ProjectItemArgDescriptor, EntityFilter> toFilter() {
      return new com.google.common.base.Function<ProjectItemArgDescriptor, EntityFilter>() {
         public EntityFilter apply(ProjectItemArgDescriptor var1) {
            return EntityFilters.allOfClass(var1.dataClass);
         }
      };
   }

   public ProjectItemArgDescriptor(String var1, Class<? extends IntegrationEntityId> var2, Class<? extends IntegrationEntity> var3) {
      this.prefix = ArgCheck.checkNotEmpty(var1);
      this.idClass = Preconditions.checkNotNull(var2);
      this.dataClass = Preconditions.checkNotNull(var3);
   }

   public String getPrefix() {
      return this.prefix;
   }

   boolean matches(Class<? extends IntegrationEntityId> var1) {
      return var1.equals(this.idClass);
   }

   public IntegrationEntityId getIdFromGuidString(String var1) {
      try {
         Constructor var2 = this.idClass.getConstructor(String.class);
         return (IntegrationEntityId)var2.newInstance(var1);
      } catch (Exception var3) {
         throw new RuntimeException(var3);
      }
   }

   public Class<? extends IntegrationEntity> getDataClass() {
      return this.dataClass;
   }

   public Class<? extends IntegrationEntityId> getIdClass() {
      return this.idClass;
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this)
         .add("prefix", this.prefix)
         .add("idClass", this.idClass.getName())
         .add("dataClass", this.dataClass.getName())
         .toString();
   }
}
