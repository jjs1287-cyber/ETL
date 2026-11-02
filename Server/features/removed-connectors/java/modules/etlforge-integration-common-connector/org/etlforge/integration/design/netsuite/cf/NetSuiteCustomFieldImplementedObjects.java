package org.etlforge.integration.design.netsuite.cf;

import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class NetSuiteCustomFieldImplementedObjects implements Serializable {
   private final Map<String, NetSuiteCustomFieldImplementedObject> customFieldObjects = Maps.newHashMap();

   void put(String var1, NetSuiteCustomFieldImplementedObject var2) {
      this.customFieldObjects.put(var1, var2);
   }

   boolean hasCustomFieldImplementation(String var1) {
      return this.customFieldObjects.containsKey(var1);
   }

   public NetSuiteCustomFieldImplementedObject getNetSuiteCustomFieldObject(String var1) {
      return this.customFieldObjects.get(var1);
   }

   Set<String> getHasCustomFieldsRecordObjects() {
      return this.customFieldObjects.keySet();
   }
}
