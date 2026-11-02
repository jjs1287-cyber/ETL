package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import javax.annotation.Nullable;
import org.etlforge.integration.client.ui.connector.wizard.NameOption;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;

final class KeyAsNameOption implements NameOption {
   @Nullable
   public String apply(@Nullable ConnectorObject var1) {
      return var1 instanceof NetSuiteConnectorObject ? ((NetSuiteConnectorObject)var1).getKey() : var1.getName();
   }

   @Override
   public String getLabel() {
      return "Key";
   }
}
