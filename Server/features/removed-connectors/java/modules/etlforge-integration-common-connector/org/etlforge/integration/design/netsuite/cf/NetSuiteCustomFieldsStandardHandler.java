package org.etlforge.integration.design.netsuite.cf;

import java.util.Set;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteObjectCustomFields;
import org.etlforge.integration.design.netsuite.lookup.NetSuiteCustomFieldsLookup;
import org.etlforge.integration.design.netsuite.lookup.NetSuiteCustomFieldsLookupIFactory;

public class NetSuiteCustomFieldsStandardHandler {
   private final NetSuiteCustomFieldsLookupIFactory factory;
   private final Set<String> customizationTypes;
   private NetSuiteObjectCustomFields objectsCustomFields = null;
   private final boolean isCustomObject = false;

   NetSuiteCustomFieldsStandardHandler(NetSuiteCustomFieldsLookupIFactory var1, Set<String> var2) {
      this.factory = var1;
      this.customizationTypes = var2;
   }

   NetSuiteObjectCustomFields process() {
      for (String var2 : this.customizationTypes) {
         this.process(var2);
      }

      return this.objectsCustomFields;
   }

   void process(String var1) {
      NetSuiteCustomFieldsLookup var2 = this.factory.getNetSuiteCustomFieldsLookup(false, var1);
      NetSuiteObjectCustomFields var3 = var2.getNetSuiteObjectsCustomFields();
      if (this.objectsCustomFields == null) {
         this.objectsCustomFields = var3;
      } else {
         this.objectsCustomFields.add(var3);
      }
   }
}
