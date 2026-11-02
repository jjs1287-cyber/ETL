package org.etlforge.integration.design.netsuite.schema;

import com.google.common.collect.Lists;
import java.util.List;

public class ConnectorNetSuiteCustomFieldSchema {
   private final String customFieldNamespace;
   private final List<String> customFieldsSchema = Lists.newArrayList();
   private String customObjectId = null;

   ConnectorNetSuiteCustomFieldSchema(String var1) {
      this.customFieldNamespace = var1;
   }

   void clear() {
      this.customFieldsSchema.clear();
   }

   public String getCustomObjectId() {
      return this.customObjectId;
   }

   public void setCustomObjectId(String var1) {
      this.customObjectId = var1;
   }

   public void add(String var1) {
      this.customFieldsSchema.add(var1);
   }

   public void appendSchema(String var1, StringBuilder var2) {
      if (var1.equals(this.customFieldNamespace)) {
         for (String var4 : this.customFieldsSchema) {
            var2.append(var4);
         }
      }
   }
}
