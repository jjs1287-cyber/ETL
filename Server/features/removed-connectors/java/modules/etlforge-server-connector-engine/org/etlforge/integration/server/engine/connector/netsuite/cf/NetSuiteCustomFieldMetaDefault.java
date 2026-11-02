package org.etlforge.integration.server.engine.connector.netsuite.cf;

import com.google.common.base.Strings;
import java.util.List;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteCustomFieldApplyObjects;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteCustomFieldApplyObjectsTypes;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteObjectCustomFields;
import org.etlforge.integration.design.netsuite.lookup.NetSuiteCustomFieldsLookup;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteExecutionContext;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;
import org.etlforge.integration.server.engine.connector.netsuite.operation.NetSuiteGetCustomizationId;
import org.etlforge.integration.server.engine.connector.netsuite.operation.NetSuiteGetList;

public class NetSuiteCustomFieldMetaDefault implements NetSuiteCustomFieldsLookup {
   private final NetSuiteCustomFieldApplyObjectsTypes applyObjectTypes;
   private final NetSuitePassport passport;
   private final NetSuiteCustomFieldApplyObjects appliesObjects;

   public NetSuiteCustomFieldMetaDefault(NetSuitePassport var1, String var2) {
      this.applyObjectTypes = NetSuiteExecutionContext.INSTANCE.getExecution(var1).getNetSuiteConfig().getNetSuiteCustomFieldDefinition().getAppliedObjects();
      this.passport = var1;
      this.appliesObjects = this.applyObjectTypes.getApplyObjectsTypes(var2);
   }

   @Override
   public NetSuiteObjectCustomFields getNetSuiteObjectsCustomFields() {
      return this.getCustomFields();
   }

   public NetSuiteObjectCustomFields getCustomFields() {
      String var1 = this.getNetSuiteCustomFieldsResponse();
      return Strings.isNullOrEmpty(var1)
         ? new NetSuiteObjectCustomFields()
         : NetSuiteCustomFieldMetaResponse.getNetSuiteObjectCustomFields(this.appliesObjects, var1);
   }

   private String getNetSuiteCustomFieldsResponse() {
      NetSuiteGetCustomizationId var1 = new NetSuiteGetCustomizationId(this.passport);
      List var2 = var1.getCustomizationId(this.appliesObjects.getCustomizationType());
      if (var2 != null && var2.size() != 0) {
         NetSuiteGetList var3 = new NetSuiteGetList(this.passport);
         return var3.getList(var2);
      } else {
         return null;
      }
   }

   public static void main(String[] var0) {
   }
}
