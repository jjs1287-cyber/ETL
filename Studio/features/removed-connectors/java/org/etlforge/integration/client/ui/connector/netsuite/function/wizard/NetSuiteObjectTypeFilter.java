package org.etlforge.integration.client.ui.connector.netsuite.function.wizard;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.ui.connector.wizard.ObjectTypeFilter;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObjectCategory;

public final class NetSuiteObjectTypeFilter implements ObjectTypeFilter {
   private final NetSuiteConnectorObjectCategory type;

   public static NetSuiteObjectTypeFilter of(NetSuiteConnectorObjectCategory var0) {
      return new NetSuiteObjectTypeFilter(var0);
   }

   public NetSuiteObjectTypeFilter(NetSuiteConnectorObjectCategory var1) {
      this.type = Preconditions.checkNotNull(var1);
   }

   public boolean apply(ConnectorObject var1) {
      Preconditions.checkNotNull(var1);
      return var1 instanceof NetSuiteConnectorObject && ((NetSuiteConnectorObject)var1).getCategory() == this.type;
   }

   @Override
   public String getName() {
      switch (this.type) {
         case StandardObject:
            return "Standard Objects";
         case CustomObject:
            return "Custom Objects";
         case ItemSearchObject:
            return "Item Search Objects";
         case TranSearchObject:
            return "Transaction Search Objects";
         default:
            throw new AssertionError(this.type.name());
      }
   }
}
