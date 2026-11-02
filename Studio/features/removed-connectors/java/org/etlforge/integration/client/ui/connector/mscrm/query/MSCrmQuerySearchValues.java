package org.etlforge.integration.client.ui.connector.mscrm.query;

import java.util.List;
import org.etlforge.integration.client.ui.connector.mscrm.function.MSCrmFunctionSearchValues;
import org.etlforge.integration.data.connector.MSCrmFunction;
import org.etlforge.integration.data.connector.MSCrmQuery;

final class MSCrmQuerySearchValues extends MSCrmFunctionSearchValues {
   @Override
   protected void addTypeSpecificValues(MSCrmFunction var1, List<String> var2) {
      MSCrmQuery var3 = (MSCrmQuery)var1;
      var2.add(var3.getQueryXml());
   }
}
