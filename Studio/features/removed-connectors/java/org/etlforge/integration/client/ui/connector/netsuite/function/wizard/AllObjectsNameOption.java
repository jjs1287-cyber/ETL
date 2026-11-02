package org.etlforge.integration.client.ui.connector.netsuite.function.wizard;

import javax.annotation.Nullable;
import org.etlforge.integration.client.ui.connector.wizard.NameOption;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;

public final class AllObjectsNameOption implements NameOption {
   @Nullable
   public String apply(@Nullable ConnectorObject var1) {
      String var2 = var1.getName();
      if (!(var1 instanceof NetSuiteConnectorObject)) {
         return var2;
      }

      NetSuiteConnectorObject var3 = (NetSuiteConnectorObject)var1;
      String var4 = var3.getCategory().getPrefix();
      return var4.isEmpty() ? var2 : var2 + " " + var4;
   }

   @Override
   public String getLabel() {
      return "Name";
   }
}
