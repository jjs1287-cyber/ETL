package org.etlforge.integration.client.ui.connector.mscrm.function.wizard;

import org.etlforge.integration.client.ui.connector.wizard.NameOption;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.integration.mscrm.metadata.BasicEntityDetails;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;

public enum NameOptions implements NameOption {
   LOGICAL_NAME {
      @Override
      public String getLabel() {
         return "Logical name";
      }

      public String apply(ConnectorObject var1) {
         BasicEntityDetails var2 = NameOptions.getDetails(var1);
         return var2.getLogicalName();
      }
   },
   DISPLAY_NAME {
      @Override
      public String getLabel() {
         return "Display name";
      }

      public String apply(ConnectorObject var1) {
         BasicEntityDetails var2 = NameOptions.getDetails(var1);
         return var2.getDisplayName();
      }
   };

   NameOptions() {
   }

   private static BasicEntityDetails getDetails(ConnectorObject var0) {
      if (var0 instanceof BasicEntityDetails) {
         return (BasicEntityDetails)var0;
      } else if (var0 instanceof MSCrmObject) {
         return ((MSCrmObject)var0).getBasicDetails();
      } else {
         throw new RuntimeException("Unsupported ConnectorObject: " + var0);
      }
   }
}
