package org.etlforge.integration.client.ui.connector.mscrm.function;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.ui.entity.search.SearchValuesCollector;
import org.etlforge.integration.data.connector.MSCrmFunction;
import org.etlforge.integration.data.entity.IntegrationEntity;

public class MSCrmFunctionSearchValues implements SearchValuesCollector {
   @Override
   public final List<String> getValues(IntegrationEntity var1) {
      MSCrmFunction var2 = (MSCrmFunction)var1;
      ArrayList var3 = Lists.newArrayList();
      var3.add(var2.getObjectName());
      this.addTypeSpecificValues(var2, var3);
      return var3;
   }

   protected void addTypeSpecificValues(MSCrmFunction var1, List<String> var2) {
   }
}
