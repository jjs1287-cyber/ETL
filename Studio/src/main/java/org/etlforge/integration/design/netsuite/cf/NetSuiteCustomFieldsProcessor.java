package org.etlforge.integration.design.netsuite.cf;

import java.util.List;
import java.util.Map.Entry;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteObjectCustomFields;
import org.etlforge.integration.design.netsuite.lookup.NetSuiteCustomFieldsLookupIFactory;

public class NetSuiteCustomFieldsProcessor {
   private final NetSuiteCustomFieldsLookupIFactory factory;
   private final NetSuiteCustomFieldsRequest requestObjects;
   private final NetSuiteCustomFieldsResult result;

   public NetSuiteCustomFieldsProcessor(NetSuiteCustomFieldsLookupIFactory var1, NetSuiteCustomFieldsRequest var2) {
      this.factory = var1;
      this.requestObjects = var2;
      this.result = new NetSuiteCustomFieldsResult(var2);
   }

   public NetSuiteCustomFieldsResult getNetSuiteCustomFieldsResult() {
      return this.result;
   }

   public void process() {
      NetSuiteCustomFieldsStandardHandler var1 = this.getNetSuiteCustomFieldHandler();
      if (!this.result.hasNoCustomFieldImplemenation()) {
         NetSuiteObjectCustomFields var2 = var1.process();

         for (Entry var4 : this.requestObjects.getRequestObjects().entrySet()) {
            String var5 = (String)var4.getKey();
            if (!this.result.hasNoCustomFieldImplementation(var5)) {
               for (Object __etlItem31 : (List)var4.getValue()) {
                  String var7 = (String) __etlItem31;
                  List var8 = var2.getNetSuiteCustomFieldsObject(var7);
                  this.result.addCustomFields(var5, var8);
               }
            }
         }
      }
   }

   private NetSuiteCustomFieldsStandardHandler getNetSuiteCustomFieldHandler() {
      return new NetSuiteCustomFieldsStandardHandler(this.factory, this.requestObjects.customizationTypes);
   }
}
