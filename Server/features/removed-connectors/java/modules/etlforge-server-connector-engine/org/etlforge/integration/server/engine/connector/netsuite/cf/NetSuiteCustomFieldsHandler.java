package org.etlforge.integration.server.engine.connector.netsuite.cf;

import com.google.common.collect.Sets;
import java.util.Set;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteObjectCustomFields;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;

public class NetSuiteCustomFieldsHandler {
   private final NetSuitePassport passport;
   private final Set<String> customizationTypes = Sets.newHashSet();
   private NetSuiteObjectCustomFields objectsCustomFields = null;

   NetSuiteCustomFieldsHandler(NetSuitePassport var1) {
      this.passport = var1;
   }

   public void addCustomizationType(String var1) {
      this.customizationTypes.add(var1);
   }

   NetSuiteObjectCustomFields process() {
      for (String var2 : this.customizationTypes) {
         this.process(var2);
      }

      return this.objectsCustomFields;
   }

   void process(String var1) {
      NetSuiteCustomFieldMetaDefault var2 = new NetSuiteCustomFieldMetaDefault(this.passport, var1);
      NetSuiteObjectCustomFields var3 = var2.getNetSuiteObjectsCustomFields();
      if (this.objectsCustomFields == null) {
         this.objectsCustomFields = var3;
      } else {
         this.objectsCustomFields.add(var3);
      }
   }
}
