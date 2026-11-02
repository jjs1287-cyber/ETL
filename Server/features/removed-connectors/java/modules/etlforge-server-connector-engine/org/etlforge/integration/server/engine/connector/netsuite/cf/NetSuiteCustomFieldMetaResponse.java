package org.etlforge.integration.server.engine.connector.netsuite.cf;

import java.util.List;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteCustomField;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteCustomFieldApplyObjects;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteObjectCustomFields;

public class NetSuiteCustomFieldMetaResponse {
   private final NetSuiteCustomField customField;
   private final List<String> trueAppliesObjects;

   public NetSuiteCustomFieldMetaResponse(String var1, String var2, String var3, String var4, List<String> var5) {
      this.customField = new NetSuiteCustomField(var1, var2, var3, var4);
      this.trueAppliesObjects = var5;
   }

   public static NetSuiteObjectCustomFields getNetSuiteObjectCustomFields(NetSuiteCustomFieldApplyObjects var0, String var1) {
      NetSuiteObjectCustomFields var2 = new NetSuiteObjectCustomFields();

      for (NetSuiteCustomFieldMetaResponse var5 : NetSuiteCustomFieldMetaResponseHandler.getNetSuiteCustomFieldMetaRecord(var1)) {
         var5.put(var2, var0);
      }

      return var2;
   }

   private void put(NetSuiteObjectCustomFields var1, NetSuiteCustomFieldApplyObjects var2) {
      for (String var4 : this.trueAppliesObjects) {
         this.put(var1, var2.getAppliedObjects(var4));
      }
   }

   private void put(NetSuiteObjectCustomFields var1, List<String> var2) {
      if (!this.customField.isInvalid()) {
         if (var2 != null) {
            for (String var4 : var2) {
               this.put(var1, var4);
            }
         }
      }
   }

   private void put(NetSuiteObjectCustomFields var1, String var2) {
      var1.add(var2, this.customField);
   }
}
