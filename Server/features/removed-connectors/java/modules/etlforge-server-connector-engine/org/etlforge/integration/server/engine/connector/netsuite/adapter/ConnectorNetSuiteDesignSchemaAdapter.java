package org.etlforge.integration.server.engine.connector.netsuite.adapter;

import org.etlforge.integration.design.netsuite.NetSuiteXsdSchemaFiles;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteDesignAdapter;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteSelectedObject;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;
import org.etlforge.integration.server.engine.connector.framework.ConnectorXsdSchemaFiles;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteExecutionContext;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;

public class ConnectorNetSuiteDesignSchemaAdapter {
   private final NetSuitePassport passport;
   private final NetSuiteSelectedObject selectedObject;

   public ConnectorNetSuiteDesignSchemaAdapter(NetSuitePassport var1, NetSuiteSelectedObject var2) {
      this.passport = var1;
      this.selectedObject = var2;
   }

   public ConnectorXsdSchemaFiles getConnectorSchemaFiles() {
      NetSuiteCustomFieldsLookupFactory var1 = new NetSuiteCustomFieldsLookupFactory(this.passport);
      NetSuiteDefinitionDesign var2 = NetSuiteExecutionContext.INSTANCE.getDesign(this.passport);
      NetSuiteDesignAdapter var3 = new NetSuiteDesignAdapter(var2, var1, this.selectedObject);
      NetSuiteXsdSchemaFiles var4 = var3.getConnectorSchemaFiles();
      return new ConnectorXsdSchemaFiles(
         var4.getRequestMainSchemaFilename(),
         var4.getRequestRootElementExpandedName(),
         var4.getResponseMainSchemaFilename(),
         var4.getResponseRootElementExpandedName(),
         var4.getSchemaFileMaps()
      );
   }
}
