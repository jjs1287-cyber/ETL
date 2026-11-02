package org.etlforge.integration.client.ui.connector.netsuite.search.definition;

import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.Map;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchObject;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchObjectRow;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObjectCategory;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteCustomSearch;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteCustomSearchDefinition;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchObjectName;

public class NetSuiteDefinitionQueryBuilder implements Serializable {
   private final NetSuiteSearchNamespacePrefixes namespacePrefix;
   Map<String, NetSuiteSearchObject> searchObjects = Maps.newHashMap();
   Map<String, NetSuiteSearchObjectRow> searchObjectRows = Maps.newHashMap();
   Map<String, NetSuiteDefinitionObjectAdvanced> advancedSearch = Maps.newHashMap();
   NetSuiteCustomSearchDefinition netsuiteCustomSearchObjects = null;
   private final NetSuiteSearchFieldDictionary searchFields;

   NetSuiteDefinitionQueryBuilder(NetSuiteSearchFieldDictionary var1, Map<String, String> var2) {
      this.namespacePrefix = new NetSuiteSearchNamespacePrefixes(var2);
      this.searchFields = var1;
   }

   public NetSuiteSearchNamespacePrefixes getNamespacePrefix() {
      return this.namespacePrefix;
   }

   public SearchField getSearchField(String var1) {
      return this.searchFields.getSearchField(var1);
   }

   public NetSuiteSearchFieldDictionary getNetSuiteSearchFields() {
      return this.searchFields;
   }

   public boolean isAdvancedStandardObject(String var1) {
      return this.advancedSearch.get(var1) != null;
   }

   public final NetSuiteCustomSearch getNetSuiteCustomSearch(NetSuiteConnectorObject var1) {
      return this.netsuiteCustomSearchObjects.getNetSuiteCustomSearch(var1);
   }

   private String getAdvancedObjectName(NetSuiteConnectorObject var1) {
      if (var1.isCustomObject()) {
         return "CustomRecord";
      }

      NetSuiteCustomSearch var2 = this.getNetSuiteCustomSearch(var1);
      return var2 == null ? var1.getKey() : var2.getCustomSearchObjectName();
   }

   private NetSuiteDefinitionObjectAdvanced createNetSuiteDefinitionObjectAdvancedFromBasic(String var1) {
      String var2 = NetSuiteSearchObjectName.getSearchBasicFromObjectName(var1);
      NetSuiteSearchObject var3 = this.searchObjects.get(var2);
      if (var3 == null) {
         return null;
      }

      String var4 = NetSuiteSearchObjectName.getSearchAdvancedFromObjectName(var1);
      NetSuiteDefinitionObjectAdvanced var5 = new NetSuiteDefinitionObjectAdvanced(var3.getSearchObjectNamspaceName(), var4);
      var5.add("basic", var2);
      return var5;
   }

   public NetSuiteDefinitionObjectAdvanced getNetSuiteDefinitionObjectAdvancedExists(NetSuiteConnectorObject var1) {
      String var2 = this.getAdvancedObjectName(var1);
      NetSuiteDefinitionObjectAdvanced var3 = this.advancedSearch.get(var2);
      if (var3 == null && var1.getCategory() == NetSuiteConnectorObjectCategory.StandardObject) {
         var3 = this.createNetSuiteDefinitionObjectAdvancedFromBasic(var2);
      }

      if (var3 == null) {
         throw new ConnectorNetSuiteWsdlError("no definition found for search object. " + var1);
      } else {
         return var3;
      }
   }

   public NetSuiteSearchObjectRow getNetSuiteSearchBasicRowExists(String var1) {
      NetSuiteSearchObjectRow var2 = this.searchObjectRows.get(var1);
      if (var2 == null) {
         throw new ConnectorNetSuiteWsdlError("no definition found for search row object. " + var1);
      } else {
         return var2;
      }
   }

   public NetSuiteSearchObject getNetSuiteSearchBasicObjectExists(String var1) {
      NetSuiteSearchObject var2 = this.searchObjects.get(var1);
      if (var2 == null) {
         throw new ConnectorNetSuiteWsdlError("no definition found for search basic object. " + var1);
      } else {
         return var2;
      }
   }

   public boolean isGenericCustomSearch(String var1) {
      return this.netsuiteCustomSearchObjects.isGenericCustomSearch(var1);
   }

   public NetSuiteCustomSearchDefinition getNetSuiteCustomSearchDefinition() {
      return this.netsuiteCustomSearchObjects;
   }
}
