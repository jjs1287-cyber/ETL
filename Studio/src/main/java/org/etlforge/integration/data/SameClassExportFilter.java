package org.etlforge.integration.data;

import com.google.common.base.Preconditions;

public final class SameClassExportFilter implements ExportDestinationFilter {
   private final Class<? extends IntegrationData> sourceClass;

   public SameClassExportFilter(IntegrationData var1) {
      this((Class<? extends IntegrationData>)var1.getClass());
   }

   public SameClassExportFilter(Class<? extends IntegrationData> var1) {
      Preconditions.checkNotNull(var1, "sourceClass");
      this.sourceClass = var1;
   }

   @Override
   public boolean canExportTo(IntegrationData var1) {
      return var1 != null && this.sourceClass.equals(var1.getClass());
   }
}
