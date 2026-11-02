package org.etlforge.integration.data.entity;

import com.google.common.collect.Constraint;
import org.etlforge.integration.data.functors.EntityFilter;

public final class EntityConstraints {
   public static Constraint<IntegrationEntity> of(EntityFilter var0) {
      return new EntityConstraints.FilterConstraint(var0, null);
   }

   public static Constraint<IntegrationEntity> of(EntityFilter var0, String var1) {
      return new EntityConstraints.FilterConstraint(var0, var1);
   }

   private EntityConstraints() {
   }

   private static class FilterConstraint implements Constraint<IntegrationEntity> {
      private final EntityFilter filter;
      private final String message;

      public FilterConstraint(EntityFilter var1, String var2) {
         this.filter = var1;
         this.message = var2;
      }

      public IntegrationEntity checkElement(IntegrationEntity var1) {
         if (this.filter.apply(var1)) {
            return var1;
         } else {
            throw new IllegalArgumentException(this.message == null ? var1.toString() : this.message + var1.toString());
         }
      }
   }
}
