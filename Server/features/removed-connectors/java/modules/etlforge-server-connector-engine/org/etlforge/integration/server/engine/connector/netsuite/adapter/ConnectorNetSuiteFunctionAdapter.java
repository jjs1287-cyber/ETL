package org.etlforge.integration.server.engine.connector.netsuite.adapter;

import java.util.Map;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteDesignCustomFieldAdapter;
import org.etlforge.integration.design.netsuite.cf.ConnectorNetSuiteCustomFields;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerNotSupportedRuntimeException;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteExecutionContext;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;
import org.etlforge.integration.server.engine.connector.netsuite.operation.NetSuiteGetServerTime;
import org.etlforge.integration.server.engine.connector.netsuite.search.GetSavedSearch;
import org.etlforge.integration.server.engine.connector.netsuite.search.NetSuiteSearchOperation;
import org.etlforge.integration.server.engine.connector.netsuite.search.NetSuiteSearchPreference;

public class ConnectorNetSuiteFunctionAdapter {
   private final NetSuitePassport passport;

   public ConnectorNetSuiteFunctionAdapter(NetSuitePassport var1) {
      this.passport = var1;
   }

   public String executeConnector(String var1, String var2) {
      if (var1.equalsIgnoreCase("GetServerTime")) {
         NetSuiteGetServerTime var6 = new NetSuiteGetServerTime(this.passport);
         return var6.getServerTime();
      } else if (var1.equalsIgnoreCase("QUERY")) {
         boolean var3 = false;
         NetSuiteSearchOperation var4 = new NetSuiteSearchOperation(this.passport, NetSuiteSearchPreference.testSearchPre, var3);
         return var4.testQuery(var2);
      } else {
         throw new ConnectorServerNotSupportedRuntimeException("Not supported netsuite operation " + var1);
      }
   }

   public ConnectorNetSuiteCustomFields getSearchBasicCustomFields(String var1) {
      NetSuiteCustomFieldsLookupFactory var2 = new NetSuiteCustomFieldsLookupFactory(this.passport);
      NetSuiteDefinitionDesign var3 = NetSuiteExecutionContext.INSTANCE.getDesign(this.passport);
      NetSuiteDesignCustomFieldAdapter var4 = new NetSuiteDesignCustomFieldAdapter(var3, var2);
      return var4.getSearchCustomFields(var1, true);
   }

   public Map<String, String> getSavedSearch(String var1) {
      String var2 = var1;
      return GetSavedSearch.getSavedSearch(this.passport, var2);
   }
}
