package org.etlforge.integration.data;

public class ExportableIntProperty extends AbstractExportableProperty {
   private final int defaultValue;

   public ExportableIntProperty(IntegrationData var1, String var2, String var3, boolean var4, int var5) {
      super(var1, var2, var3, var4);
      this.defaultValue = var5;
   }

   public ExportableIntProperty(IntegrationData var1, String var2, String var3, boolean var4, ExportDestinationFilter var5, int var6) {
      super(var1, var2, var3, var4, var5);
      this.defaultValue = var6;
   }

   @Override
   public boolean exportTo(IntegrationData var1) {
      if (this.isValidDestination(var1)) {
         int var2 = this.getSource().getIntProperty(this.getInternalPropertyName(), this.defaultValue);
         var1.setIntProperty(this.getInternalPropertyName(), var2);
         return true;
      } else {
         return false;
      }
   }
}
