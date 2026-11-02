package org.etlforge.integration.data;

public class ExportableStringProperty extends AbstractExportableProperty {
   public ExportableStringProperty(IntegrationData var1, String var2, String var3, boolean var4) {
      super(var1, var2, var3, var4);
   }

   public ExportableStringProperty(IntegrationData var1, String var2, String var3, boolean var4, ExportDestinationFilter var5) {
      super(var1, var2, var3, var4, var5);
   }

   @Override
   public boolean exportTo(IntegrationData var1) {
      if (this.isValidDestination(var1)) {
         var1.setStringProperty(this.getInternalPropertyName(), this.getPropertyValue());
         return true;
      } else {
         return false;
      }
   }
}
