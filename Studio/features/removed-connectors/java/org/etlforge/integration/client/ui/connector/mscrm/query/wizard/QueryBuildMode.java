package org.etlforge.integration.client.ui.connector.mscrm.query.wizard;

enum QueryBuildMode {
   BUILDER {
      @Override
      public String toString() {
         return "Use ETLForge's Query Builder";
      }
   },
   DOWNLOAD {
      @Override
      public String toString() {
         return "Download or copy a saved query from your CRM system";
      }
   };

   QueryBuildMode() {
   }
}
