package com.etlforge.integration.client.ui.sfdc.ucu;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.ui.entity.search.SearchValuesCollector;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceUpsert;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.sfdc.AbstractSalesforceUCU;

public final class UcuSearchValuesCollector implements SearchValuesCollector {
   @Override
   public List<String> getValues(IntegrationEntity var1) {
      AbstractSalesforceUCU var2 = (AbstractSalesforceUCU)var1;
      ArrayList var3 = Lists.newArrayList(var2.getSalesforceObjectName().getLocalName());
      if (var2 instanceof SalesforceUpsert) {
         var3.add(((SalesforceUpsert)var2).getExternalIdFieldName());
      }

      for (SalesforceWizardEntity.Header var5 : var2.getSoapHeaders()) {
         if (var5.name.equals("AssignmentRule")) {
            var3.add(var5.value);
            break;
         }
      }

      return var3;
   }
}
