package com.etlforge.integration.client.ui.sfdc.ucu.wizard;

import com.etlforge.integration.client.ui.sfdc.wizard.AutoMapperConfig;

final class UcuAutoMapperConfig implements AutoMapperConfig {
   private final String methodName;

   public UcuAutoMapperConfig(String var1) {
      this.methodName = var1;
   }

   @Override
   public boolean getDefaultValue() {
      return false;
   }

   @Override
   public String getMessage() {
      return "<html>When you finish this wizard you will be asked to map the data from your Source to the "
         + this.methodName
         + " request.<br>To save time, "
         + "ETLForge Connect\u2122"
         + " can automatically create mappings between similar field names.<br>You can edit these mappings.</html>";
   }
}
