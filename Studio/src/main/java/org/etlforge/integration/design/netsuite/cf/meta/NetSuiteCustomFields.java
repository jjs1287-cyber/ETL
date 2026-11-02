package org.etlforge.integration.design.netsuite.cf.meta;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class NetSuiteCustomFields implements Serializable {
   private final List<NetSuiteCustomField> customFields = Lists.newArrayList();
   private final Set<String> keys = Sets.newHashSet();

   public void add(NetSuiteCustomField var1) {
      if (!this.keys.contains(var1.getPrimaryKey())) {
         this.keys.add(var1.getPrimaryKey());
         this.customFields.add(var1);
      }
   }

   public List<NetSuiteCustomField> getCustomFields() {
      return this.customFields;
   }
}
