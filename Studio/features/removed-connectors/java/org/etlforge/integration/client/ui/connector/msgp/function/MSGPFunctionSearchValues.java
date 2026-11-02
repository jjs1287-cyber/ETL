package org.etlforge.integration.client.ui.connector.msgp.function;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.ui.entity.search.SearchValuesCollector;
import org.etlforge.integration.data.connector.MSGPFunction;
import org.etlforge.integration.data.entity.IntegrationEntity;

public class MSGPFunctionSearchValues implements SearchValuesCollector {
   @Override
   public final List<String> getValues(IntegrationEntity var1) {
      MSGPFunction var2 = (MSGPFunction)var1;
      ArrayList var3 = Lists.newArrayList();
      var3.add(var2.getObjectName());
      this.addTypeSpecificValues(var2, var3);
      return var3;
   }

   protected void addTypeSpecificValues(MSGPFunction var1, List<String> var2) {
   }
}
