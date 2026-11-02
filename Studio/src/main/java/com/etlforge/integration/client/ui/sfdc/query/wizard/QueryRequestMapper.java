package com.etlforge.integration.client.ui.sfdc.query.wizard;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.etlforge.integration.client.sfdc.data.AbstractMapper;
import com.etlforge.integration.client.sfdc.data.RetryPolicy;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.integration.structure.mapping.MapStringFormat;
import org.etlforge.integration.structure.mapping.MappingPredicates;

public final class QueryRequestMapper extends AbstractMapper {
   private final Transformation tx;
   private final SoapHeader[] nonDefaultSoapHeaders;
   private final String queryStringPath = "root$transaction.body$query$queryString$";

   public QueryRequestMapper(Transformation var1, SoapHeader... var2) {
      this.tx = var1;
      this.nonDefaultSoapHeaders = var2;
   }

   public void insertAllRequestMappings(String var1, SalesforceConnector var2, RetryPolicy var3) {
      ArrayList var4 = Lists.newArrayList();
      synchronized (this.tx.getDataLock()) {
         this.setMapList(var4);
         this.insertRequestGeneratorMappings();
         this.insertHeaderMappings(SfdcMethod.QUERY, var2, this.nonDefaultSoapHeaders);
         this.mapRetryPolicy(var3);
         this.mapQuery(var1);
         this.setNoSourceDataUsed(true);
         this.apply(this.tx);
      }
   }

   private void mapQuery(String var1) {
      this.insertMapping("root$transaction.body$query$queryString$", var1);
   }

   public void replaceQuery(String var1) {
      synchronized (this.tx.getDataLock()) {
         List var3 = this.removeExistingQuery();
         this.setMapList(var3);
         this.mapQuery(var1);
         this.apply(this.tx);
      }
   }

   private List<String> removeExistingQuery() {
      Predicate var1 = Predicates.not(MappingPredicates.toTargetNode("root$transaction.body$query$queryString$"));
      FluentIterable var2 = FluentIterable.from(this.tx.getMappings()).filter(var1).transform(MapStringFormat.MAPPING_TO_STRING);
      return Lists.newArrayList(var2);
   }
}
