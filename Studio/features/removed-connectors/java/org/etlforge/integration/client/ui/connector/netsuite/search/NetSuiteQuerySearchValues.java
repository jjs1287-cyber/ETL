package org.etlforge.integration.client.ui.connector.netsuite.search;

import java.util.List;
import org.etlforge.integration.client.ui.connector.netsuite.function.NetSuiteFunctionSearchValues;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.data.connector.NetSuiteQuery;

public final class NetSuiteQuerySearchValues extends NetSuiteFunctionSearchValues {
   @Override
   protected void addTypeSpecificValues(NetSuiteFunction var1, List<String> var2) {
      NetSuiteQuery var3 = (NetSuiteQuery)var1;
      var2.add(var3.getQueryXml());
   }
}
