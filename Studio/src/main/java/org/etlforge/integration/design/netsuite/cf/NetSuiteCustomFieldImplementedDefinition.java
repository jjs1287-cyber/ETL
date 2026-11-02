package org.etlforge.integration.design.netsuite.cf;

import java.io.Serializable;
import java.util.Set;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteCustomFieldApplyObjectsTypes;

public class NetSuiteCustomFieldImplementedDefinition implements Serializable {
   private final NetSuiteCustomFieldImplementedObjects customFieldObjects = new NetSuiteCustomFieldImplementedObjects();
   private final NetSuiteCustomFieldApplyObjectsTypes customFieldObjectsConfig = new NetSuiteCustomFieldApplyObjectsTypes();

   public void putCustomFieldObjects(String var1, NetSuiteCustomFieldImplementedObject var2) {
      this.customFieldObjects.put(var1, var2);
   }

   public void putCustomFieldApplyObjects(String var1, String var2, String var3, String var4, String var5) {
      this.customFieldObjectsConfig.put(var1, var2, var3, var4, var5);
   }

   public NetSuiteCustomFieldApplyObjectsTypes getAppliedObjects() {
      return this.customFieldObjectsConfig;
   }

   public NetSuiteCustomFieldImplementedObjects getNetSuiteCustomFieldObjects() {
      return this.customFieldObjects;
   }

   public boolean hasCustomFieldImplementation(String var1) {
      return this.customFieldObjectsConfig.hasCustomFieldImplementation(var1);
   }

   public Set<String> getHasCustomFieldsObjects() {
      return this.customFieldObjectsConfig.getHasCustomFieldsObjects();
   }

   public Set<String> getHasCustomFieldsRecordObjects() {
      return this.customFieldObjects.getHasCustomFieldsRecordObjects();
   }

   public NetSuiteCustomFieldImplementedObject getNetSuiteCustomFieldObject(String var1) {
      return this.customFieldObjects.getNetSuiteCustomFieldObject(var1);
   }
}
