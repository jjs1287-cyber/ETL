package com.etlforge.integration.client.ui.sfdc.ucu.page.v4;

import com.google.common.collect.Sets;
import com.etlforge.integration.client.sfdc.data.AbstractMapper;
import com.etlforge.integration.client.sfdc.data.RetryPolicy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.sfdc.UCURequestHeader;
import org.etlforge.integration.salesforce.config.ConstantHeaderMapping;
import org.etlforge.integration.salesforce.config.SfdcMethodConfig;
import org.etlforge.integration.structure.mapping.MapStringFormat;
import org.etlforge.integration.structure.mapping.Mapping;

final class RequestMapper extends AbstractMapper {
   private final Transformation tx;
   private final SfdcMethodConfig methodConfig;
   private String externalIdFieldName;

   public RequestMapper(Transformation var1, SfdcMethodConfig var2) {
      this.tx = var1;
      this.methodConfig = var2;
   }

   public void setExternalIdFieldName(String var1) {
      this.externalIdFieldName = var1;
   }

   public void updateHeaderMappings(UCURequestHeader var1) {
      synchronized (this.tx.getDataLock()) {
         List var3 = this.tx.getMapList();
         this.removeExistingHeaderMappings(var3);
         this.setMapList(var3);
         this.insertHeaderMappings(var1);
         this.apply(this.tx);
      }
   }

   private void removeExistingHeaderMappings(List<String> var1) {
      Set var2 = this.collectHeaderPaths();
      Iterator var3 = var1.iterator();

      while (var3.hasNext()) {
         String var4 = (String)var3.next();
         Mapping var5 = MapStringFormat.parse(var4);
         if (var2.contains(var5.getTargetPath().toString())) {
            var3.remove();
         }
      }
   }

   private Set<String> collectHeaderPaths() {
      HashSet var1 = Sets.newHashSet(
         this.methodConfig.getProperty("TruncateFieldsHeaderPath"),
         this.methodConfig.getProperty("AutoResponseEmailHeaderPath"),
         this.methodConfig.getProperty("OtherEmailHeaderPath"),
         this.methodConfig.getProperty("UserEmailHeaderPath"),
         this.methodConfig.getProperty("DebugLevelHeaderPath")
      );
      if (this.externalIdFieldName != null) {
         var1.add(this.methodConfig.getProperty("ExternalIdFieldPath"));
      }

      for (ConstantHeaderMapping var3 : this.methodConfig.getConstantHeaderMappings()) {
         var1.add(var3.getTargetPath());
      }

      return var1;
   }

   public void updateRetryPolicy(RetryPolicy var1) {
      synchronized (this.tx.getMapList()) {
         this.setMapList(this.tx.getMapList());
         this.mapRetryPolicy(var1);
         this.apply(this.tx);
      }
   }

   public void insertAllRequestMappings(UCURequestHeader var1, RetryPolicy var2) {
      this.setMapList(new ArrayList<>());
      synchronized (this.tx.getDataLock()) {
         this.insertRequestGeneratorMappings();
         this.insertHeaderMappings(var1);
         this.mapRetryPolicy(var2);
         this.setNoSourceDataUsed(true);
         this.apply(this.tx);
      }
   }

   private void insertHeaderMappings(UCURequestHeader var1) {
      this.insertConstantRequestHeaderMappings(this.methodConfig);
      this.insertMapping(this.methodConfig.getProperty("TruncateFieldsHeaderPath"), var1.isTruncateFieldsEnabled());
      boolean var2 = var1.isEmailTriggerEnabled();
      this.insertMapping(this.methodConfig.getProperty("AutoResponseEmailHeaderPath"), var2);
      this.insertMapping(this.methodConfig.getProperty("OtherEmailHeaderPath"), var2);
      this.insertMapping(this.methodConfig.getProperty("UserEmailHeaderPath"), var2);
      this.insertMapping(this.methodConfig.getProperty("DebugLevelHeaderPath"), var1.getDebugMode().toString());
      if (this.externalIdFieldName != null) {
         this.insertMapping(this.methodConfig.getProperty("ExternalIdFieldPath"), this.externalIdFieldName);
      }
   }
}
