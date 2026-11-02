package com.etlforge.integration.client.ui.sfdc.ucu.wizard;

import com.google.common.collect.Lists;
import com.etlforge.integration.client.sfdc.data.AbstractMapper;
import com.etlforge.integration.client.sfdc.data.RetryPolicy;
import java.util.ArrayList;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

final class RequestHeaderMapper extends AbstractMapper {
   private final Transformation tx;
   private final SfdcMethod method;
   private final SalesforceConnector connector;
   private final RetryPolicy retryPolicy;
   private String externalIdFieldName;
   private final SoapHeader[] nonDefaultSoapHeaders;

   public RequestHeaderMapper(Transformation var1, SfdcMethod var2, SalesforceConnector var3, RetryPolicy var4, SoapHeader... var5) {
      this.tx = var1;
      this.method = var2;
      this.connector = var3;
      this.retryPolicy = var4;
      this.nonDefaultSoapHeaders = var5;
   }

   public void setExternalIdFieldName(String var1) {
      this.externalIdFieldName = var1;
   }

   public void insertAllRequestMappings() {
      ArrayList var1 = Lists.newArrayList();
      this.setMapList(var1);
      synchronized (this.tx.getDataLock()) {
         this.insertRequestGeneratorMappings();
         this.mapRetryPolicy(this.retryPolicy);
         this.insertHeaderMappings(this.method, this.connector, this.nonDefaultSoapHeaders);
         this.insertExternalFieldMapping();
         this.setNoSourceDataUsed(true);
         this.apply(this.tx);
      }
   }

   private void insertExternalFieldMapping() {
      if (this.externalIdFieldName != null) {
         this.insertMapping("root$transaction.body$upsert$externalIDFieldName$", this.externalIdFieldName);
      }
   }
}
