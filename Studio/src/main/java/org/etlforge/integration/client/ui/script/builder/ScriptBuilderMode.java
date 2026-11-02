package org.etlforge.integration.client.ui.script.builder;

public enum ScriptBuilderMode {
   DEVELOPER,
   BUSINESS_ANALYST;

   @Override
   public String toString() {
      switch (this) {
         case DEVELOPER:
            return "Developer";
         case BUSINESS_ANALYST:
            return "Business Analyst";
         default:
            throw new RuntimeException("Unexpected ScriptBuilderMode: " + this.name());
      }
   }
}
