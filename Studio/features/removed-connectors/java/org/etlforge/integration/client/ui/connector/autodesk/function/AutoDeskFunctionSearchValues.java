package org.etlforge.integration.client.ui.connector.autodesk.function;

import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.ui.entity.search.SearchValuesCollector;
import org.etlforge.integration.data.connector.AutoDeskFunction;
import org.etlforge.integration.data.entity.IntegrationEntity;

public class AutoDeskFunctionSearchValues implements SearchValuesCollector {
   @Override
   public final List<String> getValues(IntegrationEntity var1) {
      AutoDeskFunction var2 = (AutoDeskFunction)var1;
      ArrayList var3 = new ArrayList();
      var3.add(var2.getWorkspaceName());
      this.addTypeSpecificValues(var2, var3);
      return var3;
   }

   protected void addTypeSpecificValues(AutoDeskFunction var1, List<String> var2) {
   }
}
