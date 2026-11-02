package com.etlforge.integration.client.ui.sfdc.query.page.v4;

import com.etlforge.integration.client.sfdc.data.AbstractMapper;
import com.etlforge.integration.client.sfdc.data.RetryPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.salesforce.config.SfdcMethodConfig;
import org.etlforge.integration.structure.mapping.MapStringFormat;
import org.etlforge.integration.structure.mapping.Mapping;

final class RequestMapperV4 extends AbstractMapper {
   private final Transformation tx;
   private final SfdcMethodConfig cfg;
   private final String queryStringPath;
   private final String batchSizePath;

   public RequestMapperV4(Transformation var1, SfdcMethodConfig var2) {
      this.tx = var1;
      this.cfg = var2;
      this.queryStringPath = var2.getProperty("QueryStringPath");
      this.batchSizePath = var2.getProperty("BatchSizePath");
   }

   public void insertAllRequestMappings(String var1, int var2, RetryPolicy var3) {
      synchronized (this.tx.getDataLock()) {
         this.setMapList(new ArrayList<>());
         this.insertRequestGeneratorMappings();
         this.insertConstantRequestHeaderMappings(this.cfg);
         this.mapBatchSize(var2);
         this.mapQuery(var1);
         this.mapRetryPolicy(var3);
         this.setNoSourceDataUsed(true);
         this.apply(this.tx);
      }
   }

   private void mapBatchSize(int var1) {
      this.insertMapping(this.batchSizePath, var1);
   }

   private void mapQuery(String var1) {
      this.insertMapping(this.queryStringPath, var1);
   }

   public void updateQueryStringAndBatchSize(String var1, int var2) {
      synchronized (this.tx.getDataLock()) {
         List var4 = this.getMapListWithoutQueryStringAndBatchSize();
         this.setMapList(var4);
         this.mapBatchSize(var2);
         this.mapQuery(var1);
         this.apply(this.tx);
      }
   }

   private List<String> getMapListWithoutQueryStringAndBatchSize() {
      boolean var1 = false;
      boolean var2 = false;
      List var3 = this.tx.getMapList();
      Iterator var4 = var3.iterator();

      while ((!var1 || !var2) && var4.hasNext()) {
         String var5 = (String)var4.next();
         Mapping var6 = MapStringFormat.parse(var5);
         String var7 = var6.getTargetPath().toString();
         if (var7.equals(this.batchSizePath)) {
            var4.remove();
            var2 = true;
         } else if (var7.equals(this.queryStringPath)) {
            var4.remove();
            var1 = true;
         }
      }

      return var3;
   }

   public void updateRetryPolicy(RetryPolicy var1) {
      synchronized (this.tx.getDataLock()) {
         this.setMapList(this.tx.getMapList());
         this.mapRetryPolicy(var1);
         this.apply(this.tx);
      }
   }
}
