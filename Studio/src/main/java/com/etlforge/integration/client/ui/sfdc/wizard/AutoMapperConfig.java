package com.etlforge.integration.client.ui.sfdc.wizard;

public interface AutoMapperConfig {
   AutoMapperConfig NO_AUTO_MAPPING = new AutoMapperConfig() {
      @Override
      public String getMessage() {
         return null;
      }

      @Override
      public boolean getDefaultValue() {
         return false;
      }
   };

   boolean getDefaultValue();

   String getMessage();
}
