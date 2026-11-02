package org.etlforge.integration.server.engine.connector.netsuite;

import java.util.List;
import java.util.Map;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteObjectCustomFields;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEngine;
import org.etlforge.integration.server.engine.connector.framework.ConnectorObjectList;
import org.etlforge.integration.server.engine.connector.framework.ConnectorXsdSchemaFiles;
import org.etlforge.integration.server.engine.connector.netsuite.adapter.ConnectorNetSuiteDesignAdapter;
import org.etlforge.integration.server.engine.connector.netsuite.adapter.ConnectorNetSuiteDesignSchemaAdapter;
import org.etlforge.integration.server.engine.connector.netsuite.adapter.ConnectorNetSuiteExecutionAdapter;
import org.etlforge.integration.server.engine.connector.netsuite.adapter.ConnectorNetSuiteFunctionAdapter;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;
import org.etlforge.integration.server.engine.connector.netsuite.wsdl.NetSuiteDefinitionContentsFactory;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteDefinitionContents;

public class ConnectorEngineNetSuite implements ConnectorEngine {
   private final NetSuitePassport passport;

   public ConnectorEngineNetSuite(ConnectorEndpointNetSuite var1) {
      this.passport = var1.getConnectorEndpointNetSuiteAdapter().getNetSuitePassport();
   }

   @Override
   public ConnectorObjectList getConnectorObjectList(List<String> var1, String var2) {
      ConnectorInputValidatorNetSuite var3 = new ConnectorInputValidatorNetSuite(var1);
      ConnectorNetSuiteDesignAdapter var4 = new ConnectorNetSuiteDesignAdapter(this.passport, var3.getNetSuiteConnectorSelectedObject());
      return var4.getConnectorObjectList();
   }

   @Override
   public ConnectorXsdSchemaFiles getConnectorSchemaFiles(boolean var1, List<String> var2) {
      ConnectorInputValidatorNetSuite var3 = new ConnectorInputValidatorNetSuite(var2);
      var3.validatecomplete();
      ConnectorNetSuiteDesignSchemaAdapter var4 = new ConnectorNetSuiteDesignSchemaAdapter(this.passport, var3.getNetSuiteConnectorSelectedObject());
      return var4.getConnectorSchemaFiles();
   }

   @Override
   public void executeConnectorFileUtf8(List<String> var1, String var2, String var3) {
      ConnectorInputValidatorNetSuite var4 = new ConnectorInputValidatorNetSuite(var1);
      var4.validatecomplete();
      ConnectorNetSuiteExecutionAdapter var5 = new ConnectorNetSuiteExecutionAdapter(this.passport, var4.getNetSuiteConnectorSelectedObject());
      var5.executeConnectorFileUtf8(var4.netSuiteSearchPageSize, var2, var3);
   }

   public String executeConnector(List<String> var1, String var2) {
      String var3 = var1.size() > 0 ? (String)var1.get(0) : "";
      ConnectorNetSuiteFunctionAdapter var4 = new ConnectorNetSuiteFunctionAdapter(this.passport);
      return var4.executeConnector(var3, var2);
   }

   public Map<String, String> getSavedSearch(String var1) {
      ConnectorNetSuiteFunctionAdapter var2 = new ConnectorNetSuiteFunctionAdapter(this.passport);
      return var2.getSavedSearch(var1);
   }

   public NetSuiteDefinitionContents getNetSuiteDefinitionContents() {
      return NetSuiteDefinitionContentsFactory.createFromOnline(this.passport);
   }

   public NetSuiteObjectCustomFields getCustomFields(boolean var1, String var2) {
      return ConnectorNetSuiteDesignAdapter.getCustomFields(this.passport, var1, var2);
   }
}
