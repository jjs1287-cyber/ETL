package org.etlforge.integration.design.netsuite.cf;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteCustomField;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteObjectCustomFields;

public class NetSuiteCustomFieldsResult {
   private final NetSuiteCustomFieldsRequest requestObjects;
   final NetSuiteObjectCustomFields categoryCustomFields = new NetSuiteObjectCustomFields();
   final Set<String> noCustomFieldImplementationType = Sets.newHashSet();

   NetSuiteCustomFieldsResult(NetSuiteCustomFieldsRequest var1) {
      this.requestObjects = var1;
   }

   boolean hasNoCustomFieldImplemenation() {
      return this.noCustomFieldImplementationType.size() == this.requestObjects.size() || this.requestObjects.size() == 0;
   }

   void addNoCustomFieldImplementation(String var1) {
      this.noCustomFieldImplementationType.add(var1);
   }

   public boolean hasNoCustomFieldImplementation(String var1) {
      return this.noCustomFieldImplementationType.contains(var1);
   }

   public void addCustomFields(String var1, List<NetSuiteCustomField> var2) {
      this.categoryCustomFields.add(var1, var2);
   }

   public List<NetSuiteCustomField> getNetSuiteCustomFieldsObject(String var1) {
      return this.categoryCustomFields.getNetSuiteCustomFieldsObject(var1);
   }

   public NetSuiteObjectCustomFields getNetSuiteObjectsCustomFields() {
      return this.categoryCustomFields;
   }
}
