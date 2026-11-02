package org.etlforge.integration.client.ui.connector.ebs.function;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.ui.entity.search.SearchValuesCollector;
import org.etlforge.integration.data.connector.EBSFunction;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;

final class EBSFunctionSearchValues implements SearchValuesCollector {
   @Override
   public List<String> getValues(IntegrationEntity var1) {
      ArrayList var2 = Lists.newArrayList();
      EBSFunction var3 = (EBSFunction)var1;
      var2.add(var3.getProgram().getName());
      ConnectorObject var4 = var3.getCatalog();
      if (var4 != null) {
         var2.add(var4.getName());
      }

      return var2;
   }
}
