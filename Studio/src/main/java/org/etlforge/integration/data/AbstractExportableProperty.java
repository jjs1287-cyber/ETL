package org.etlforge.integration.data;

public abstract class AbstractExportableProperty implements ExportableProperty {
   private final IntegrationData source;
   private final String internalPropertyName;
   private final String displayedPropertyName;
   private final boolean exportedByDefault;
   private final ExportDestinationFilter destinationFilter;

   public AbstractExportableProperty(IntegrationData var1, String var2, String var3, boolean var4) {
      this(var1, var2, var3, var4, new SameClassExportFilter(var1));
   }

   public AbstractExportableProperty(IntegrationData var1, String var2, String var3, boolean var4, ExportDestinationFilter var5) {
      this.source = var1;
      this.internalPropertyName = var2;
      this.displayedPropertyName = var3;
      this.exportedByDefault = var4;
      this.destinationFilter = var5;
   }

   protected final boolean isValidDestination(IntegrationData var1) {
      return this.destinationFilter.canExportTo(var1);
   }

   protected final IntegrationData getSource() {
      return this.source;
   }

   protected final String getInternalPropertyName() {
      return this.internalPropertyName;
   }

   protected final String getPropertyValue() {
      return this.source.getProperty(this.internalPropertyName);
   }

   @Override
   public final String getName() {
      return this.displayedPropertyName;
   }

   @Override
   public final boolean isExportedByDefault() {
      return this.exportedByDefault;
   }
}
