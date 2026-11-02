package org.etlforge.integration.server.netsuite.wsdl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.design.netsuite.search.wsdl.NetSuiteDefinitionSearch;
import org.etlforge.integration.design.netsuite.search.wsdl.NetSuiteSeachRow;
import org.etlforge.integration.design.netsuite.search.wsdl.NetSuiteSeachRows;
import org.etlforge.integration.design.netsuite.search.wsdl.NetSuiteSearchField;
import org.etlforge.integration.design.netsuite.search.wsdl.NetSuiteSearchWsdl;
import org.etlforge.integration.design.util.XsdGlobalElementName;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchObjectName;

public class NetSuiteDefinitionSearchFactory {
   public static NetSuiteDefinitionSearch create(NetSuiteWsdlContents var0, ConnectorNetSuiteNamespaces var1) {
      NetSuiteSearchWsdl var2 = NetSuiteSearchWsdlCreator.newNetSuiteSearchWsdl(var0, var1);
      NetSuiteSeachRows var3 = buildNSSeachRows(var2.getSearchObjects());
      return new NetSuiteDefinitionSearch(var2, var3);
   }

   private static NetSuiteSeachRows buildNSSeachRows(Map<XsdGlobalElementName, List<NetSuiteSearchField>> var0) {
      NetSuiteSeachRows var1 = new NetSuiteSeachRows();

      for (Entry var3 : var0.entrySet()) {
         String var4 = ((XsdGlobalElementName)var3.getKey()).getLocalName();
         if (NetSuiteSearchObjectName.isSearchAdvanced(var4)) {
            var1.addAdvancedSearch(var4);
         }

         if (NetSuiteSearchObjectName.isSearchRow(var4)) {
            String var5 = var4;
            NetSuiteSeachRow var6 = new NetSuiteSeachRow(var4);
            var1.put(var5, var6);

            for (Object __etlItem36 : (List)var3.getValue()) {
               NetSuiteSearchField var8 = (NetSuiteSearchField) __etlItem36;
               var6.put(var8.getFieldName(), var8.getFieldTypeNamespace(), var8.getFieldTypeLocalName());
            }
         }
      }

      return var1;
   }
}
