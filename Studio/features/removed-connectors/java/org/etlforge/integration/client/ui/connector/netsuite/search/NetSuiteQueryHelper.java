package org.etlforge.integration.client.ui.connector.netsuite.search;

import java.util.Map;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteCustomFieldsQueryBuilderLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionQueryBuilderLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteSearchObjectSavedSearchLookup;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchAdvancedFactory;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchRecord;
import org.etlforge.integration.client.ui.connector.netsuite.search.wizard.QueryBuilderModel;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.data.connector.NetSuiteQuery;

public class NetSuiteQueryHelper {
   private final NetSuiteDefinitionQueryBuilderLookup definitionLookup;
   private final NetSuiteCustomFieldsQueryBuilderLookup customFieldsLookup;
   private final NetSuiteSearchObjectSavedSearchLookup savedSearchLookup;
   private final NetSuiteQuery queryEntity;

   public NetSuiteQueryHelper(
      NetSuiteQuery var1, NetSuiteDefinitionQueryBuilderLookup var2, NetSuiteSearchObjectSavedSearchLookup var3, NetSuiteCustomFieldsQueryBuilderLookup var4
   ) {
      this.queryEntity = var1;
      this.definitionLookup = var2;
      this.customFieldsLookup = var4;
      this.savedSearchLookup = var3;
   }

   public QueryBuilderModel getQueryBuilderModel() throws Exception {
      NetSuiteEndpoint var1 = this.queryEntity.getEndpoint();
      String var2 = this.queryEntity.getObjectName();
      NetSuiteQuery.QueryParams var3 = this.queryEntity.getQueryParams();
      boolean var4 = var3.getAdvancedSearchProperty().getSearchType().isSavedSearch();
      NetSuiteCustomFieldsQueryBuilderLookup var5 = var4 ? null : this.customFieldsLookup;
      NetSuiteSearchRecord var6 = NetSuiteSearchAdvancedFactory.newSearchRecord(var1, var2, this.definitionLookup, var5);
      if (var4) {
         Map var7 = this.savedSearchLookup.savedSearchLookup(var1, var2);
         return QueryBuilderModel.forCompletedQuery(var2, var3, var6, var7);
      } else {
         return QueryBuilderModel.forCompletedQuery(var2, var3, var6);
      }
   }

   public NetSuiteQuery getNetSuiteQuery() {
      return this.queryEntity;
   }

   public void saveQueryBuilderModel(QueryBuilderModel var1) {
      NetSuiteQuery.QueryParams var2 = var1.getDefinedParams();
      this.setQueryParams(var2);
   }

   public void setQueryParams(NetSuiteQuery.QueryParams var1) {
      this.queryEntity.setQueryParams(var1);
   }

   public NetSuiteQuery.QueryParams getQueryParams() {
      return this.queryEntity.getQueryParams();
   }

   public void refreshQueryXml() throws Exception {
      QueryBuilderModel var1 = this.getQueryBuilderModel();
      this.saveQueryBuilderModel(var1);
   }
}
