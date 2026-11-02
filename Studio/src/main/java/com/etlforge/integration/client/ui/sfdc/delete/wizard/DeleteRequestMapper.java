package com.etlforge.integration.client.ui.sfdc.delete.wizard;

import com.google.common.collect.Lists;
import com.etlforge.integration.client.sfdc.data.AbstractMapper;
import com.etlforge.integration.client.sfdc.data.RetryPolicy;
import java.util.ArrayList;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

final class DeleteRequestMapper extends AbstractMapper {
   public void insertAllRequestMappings(Transformation var1, SalesforceConnector var2, RetryPolicy var3, SoapHeader... var4) {
      ArrayList var5 = Lists.newArrayList();
      synchronized (var1.getDataLock()) {
         this.setMapList(var5);
         this.insertRequestGeneratorMappings();
         this.mapRetryPolicy(var3);
         this.insertHeaderMappings(SfdcMethod.DELETE, var2, var4);
         this.setNoSourceDataUsed(true);
         this.apply(var1);
      }
   }
}
