package org.etlforge.integration.design.netsuite.cf;

import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.Set;
import java.util.SortedMap;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDesignNotSupportedRuntimeException;

public class NetSuiteCustomFieldTypeConverter implements Serializable {
   private final SortedMap<String, NetSuiteCustomFieldType> fieldsMap = Maps.newTreeMap();

   public void put(String var1, NetSuiteCustomFieldType var2) {
      this.fieldsMap.put(var1, var2);
   }

   public NetSuiteCustomFieldType getNSCustomFieldTypeExist(String var1) {
      NetSuiteCustomFieldType var2 = this.fieldsMap.get(var1);
      if (var2 == null) {
         throw new ConnectorNetSuiteDesignNotSupportedRuntimeException("Custom field type " + var1 + " not supported");
      } else {
         return var2;
      }
   }

   public Set<String> getAllFieldTypes() {
      return this.fieldsMap.keySet();
   }
}
