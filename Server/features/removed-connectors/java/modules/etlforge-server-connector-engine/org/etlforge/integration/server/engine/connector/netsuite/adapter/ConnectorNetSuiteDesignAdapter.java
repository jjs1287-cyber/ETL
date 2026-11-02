package org.etlforge.integration.server.engine.connector.netsuite.adapter;

import java.util.Map;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteSelectedObject;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteObjectCustomFields;
import org.etlforge.integration.design.netsuite.config.NetSuiteConfig;
import org.etlforge.integration.design.netsuite.lookup.NetSuiteCustomFieldsLookup;
import org.etlforge.integration.server.engine.connector.framework.ConnectorObjectList;
import org.etlforge.integration.server.engine.connector.netsuite.customobject.NetSuiteCustomObjects;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteDefinitionExecution;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteExecutionContext;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;

public class ConnectorNetSuiteDesignAdapter {
   private final NetSuitePassport passport;
   private final NetSuiteSelectedObject selectedObject;

   public ConnectorNetSuiteDesignAdapter(NetSuitePassport var1, NetSuiteSelectedObject var2) {
      this.passport = var1;
      this.selectedObject = var2;
   }

   public ConnectorObjectList getConnectorObjectList() {
      Map var1 = this.getObjects(this.selectedObject.isSearch());
      return new ConnectorObjectList(var1);
   }

   private Map<String, String> getObjects(boolean var1) {
      NetSuiteDefinitionExecution var2 = NetSuiteExecutionContext.INSTANCE.getExecution(this.passport);
      Map var3 = var2.getDefinitionConnector().getRecordTypes(var1);
      if (var1) {
         NetSuiteConfig var4 = var2.getNetSuiteConfig();
         var3.putAll(var4.getNetSuiteCustomSearchDefinition().getCustomSearchObjects());
      }

      NetSuiteCustomObjects var5 = NetSuiteCustomObjects.retrieve(this.passport);
      var3.putAll(var5.getCustomObjectList());
      return var3;
   }

   public static NetSuiteObjectCustomFields getCustomFields(NetSuitePassport var0, boolean var1, String var2) {
      NetSuiteCustomFieldsLookupFactory var3 = new NetSuiteCustomFieldsLookupFactory(var0);
      NetSuiteCustomFieldsLookup var4 = var3.getNetSuiteCustomFieldsLookup(var1, var2);
      return var4.getNetSuiteObjectsCustomFields();
   }
}
