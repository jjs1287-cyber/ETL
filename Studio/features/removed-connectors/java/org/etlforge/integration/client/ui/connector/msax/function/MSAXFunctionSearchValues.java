package org.etlforge.integration.client.ui.connector.msax.function;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.ui.entity.search.SearchValuesCollector;
import org.etlforge.integration.data.connector.MSAXFunction;
import org.etlforge.integration.data.entity.IntegrationEntity;

final class MSAXFunctionSearchValues implements SearchValuesCollector {
   @Override
   public List<String> getValues(IntegrationEntity var1) {
      ArrayList var2 = Lists.newArrayList();
      MSAXFunction var3 = (MSAXFunction)var1;
      var2.add(var3.getWSDLUrl());
      var2.add(var3.getMSAXServiceOperation());
      return var2;
   }
}
