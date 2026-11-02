package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchOperator;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.SearchField;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchRecord;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.NetSuiteSearchObjectValue;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.NetSuiteSearchObjectValueRow;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.NetSuiteSearchRecordValue;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.NetSuiteSearchRecordValueFormatter;
import org.etlforge.integration.client.ui.connector.netsuite.search.wizard.field.Filter;
import org.etlforge.integration.data.connector.NetSuiteQuery;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteQueryPageSize;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchAdvancedProperties;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchCategory;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;

public final class QueryBuilderModel extends AbstractPropertyBasedModel {
   public static final String ADVANCEDPARA = "advancedpara";
   public static final String OBJECT = "object";
   public static final String FIELDS = "fields";
   public static final String SELECTED_OBJECT = "selectedObject";
   public static final String SELECTED_FIELD = "selectedField";
   public static final String FILTERS = "filters";
   public static final String OPERATORS = "operators";
   public static final String SELECTED_OPERATOR = "selectedOperator";
   public static final String SELECTED_FILTERS = "selectedFilters";
   public static final String PAGE_SIZE = "pageSize";
   private final Property<NetSuiteSearchRecord> object = this.newProperty("object");
   private final Property<NetSuiteSearchAdvancedProperties> advancedpara = this.newProperty("advancedpara");
   private final Property<String> selectedObject = this.newProperty("selectedObject");
   private final Property<String> selectedField = this.newProperty("selectedField");
   private final Map<String, Filter> filterMap = new HashMap<>();
   private final ListProperty<Filter> filterList = this.newListProperty("filters");
   private final ListProperty<Filter> selectedFilters = this.newListProperty("selectedFilters");
   private final ListProperty<NetSuiteSearchOperator> operators = this.newListProperty("operators");
   private final Property<NetSuiteSearchOperator> selectedOperator = this.newProperty("selectedOperator");
   private final Property<NetSuiteQueryPageSize> pageSize = this.newProperty("pageSize", NetSuiteQueryPageSize.DEFAULT);
   private final SaveSearchModel savedSearchModel;
   private final SearchRecordTreeModel recordTreeModel;
   private String searchXml = null;
   private final StringBuilder invalidMessage = new StringBuilder();

   public static QueryBuilderModel forCompletedQuery(String var0, NetSuiteQuery.QueryParams var1, NetSuiteSearchRecord var2) {
      return forCompletedQuery(var0, var1, var2, null);
   }

   public static QueryBuilderModel forCompletedQuery(String var0, NetSuiteQuery.QueryParams var1, NetSuiteSearchRecord var2, Map<String, String> var3) {
      QueryBuilderModel var4 = new QueryBuilderModel();
      NetSuiteSearchAdvancedProperties var5 = var1.getAdvancedSearchProperty();
      var4.setNetSuiteSearchAdvancedProperties(var5);
      var4.setPageSize(var1.getPageSize());
      var4.setSearchObject(var2);
      String var6 = var1.getXml();
      NetSuiteSearchRecordValue var7 = NetSuiteSearchRecordValueFormatter.parse(var2, var6);
      if (var5.getSearchType().isSavedSearch()) {
         var4.getSaveSearchModel().setObjectName(var2.getNetSuiteConnectorObject().getName());
         var4.getSaveSearchModel().setSavedSearch(var3);
         var4.getSaveSearchModel().setSavedSearchId(var7.getSavedSearchId());
      } else {
         var4.setCriteria(var7.getCriteria());
         if (var5.getSearchType().isAdvancedSearch()) {
            var4.setColumns(var7.getColumns());
         }
      }

      return var4;
   }

   private void setCriteria(Map<String, NetSuiteSearchObjectValue> var1) {
      for (Entry var3 : var1.entrySet()) {
         String var4 = (String)var3.getKey();
         String var5 = this.recordTreeModel.getObjectDisplayName(var4);
         NetSuiteSearchObjectValue var6 = (NetSuiteSearchObjectValue)var3.getValue();

         for (Entry var8 : var6.getFilters().entrySet()) {
            String var9 = (String)var8.getKey();
            SearchField var10 = this.object.get().getSearchField(var4, var9);
            SearchFieldValue var11 = (SearchFieldValue)var8.getValue();
            Filter var12 = new Filter(var5, var9, var10, var11);
            this.setSelected(var5, var9);
            this.addOrReplaceFilter(var12);
         }
      }
   }

   private void setColumns(Map<String, NetSuiteSearchObjectValueRow> var1) {
      this.recordTreeModel.setSelectedColumnNodes(var1);
   }

   public QueryBuilderModel() {
      this.savedSearchModel = new SaveSearchModel();
      this.recordTreeModel = new SearchRecordTreeModel();
      this.pageSize.setAllowsNull(false);
      this.selectedField.addListener(new QueryBuilderModel.SelectedFieldListener());
      this.selectedFilters.addListener(new QueryBuilderModel.SelectedFiltersListener());
   }

   public final SaveSearchModel getSaveSearchModel() {
      return this.savedSearchModel;
   }

   public boolean isValueValid() {
      return this.invalidMessage.length() == 0;
   }

   public String getInvalidMessage() {
      return this.invalidMessage.toString();
   }

   public final SearchRecordTreeModel getRecordTreeModel() {
      return this.recordTreeModel;
   }

   NetSuiteSearchRecord getSearchObject() {
      return this.object.get();
   }

   NetSuiteSearchCategory getSearchType() {
      return this.advancedpara.get() == null ? null : this.advancedpara.get().getSearchType();
   }

   public NetSuiteSearchAdvancedProperties getNetSuiteSearchAdvancedProperties() {
      return this.advancedpara.get();
   }

   public void setNetSuiteSearchAdvancedProperties(NetSuiteSearchAdvancedProperties var1) {
      if (var1 != this.advancedpara.get()) {
         this.advancedpara.set(Preconditions.checkNotNull(var1));
         this.setRecordTreeModel();
      }
   }

   private void setRecordTreeModel() {
      NetSuiteSearchAdvancedProperties var1 = this.advancedpara.get();
      if (var1 != null) {
         this.recordTreeModel.setModel(this.getSearchObject(), var1.getSearchType());
      }
   }

   public void setSearchObject(NetSuiteSearchRecord var1) {
      if (var1 != this.object.get()) {
         this.object.set(Preconditions.checkNotNull(var1));
         this.setRecordTreeModel();
         this.filterList.clear();
         this.filterMap.clear();
         this.selectedObject.set(null);
         this.selectedField.set(null);
         this.selectedOperator.set(null);
         this.selectedFilters.clear();
      }
   }

   public void setSelected(String var1, String var2) {
      this.selectedField.set(null);
      this.selectedObject.set(var1);
      this.selectedField.set(var2);
   }

   @Nullable
   public String getSelectedObjectName() {
      return this.selectedObject.get();
   }

   @Nullable
   public String getSelectedFieldName() {
      return this.selectedField.get();
   }

   public SearchField getFieldMetaData(String var1, String var2) {
      String var3 = this.recordTreeModel.getInternalName(var1);
      return this.object.get().getSearchField(var3, var2);
   }

   @Nullable
   public Filter getFilterFor(String var1, String var2) {
      return this.filterMap.get(Filter.getKey(var1, var2));
   }

   public void addOrReplaceFilter(Filter var1) {
      this.filterMap.put(var1.getKey(), var1);
      this.updateFilterList();
      this.selectedFilters.set(Collections.singletonList(var1));
   }

   private void updateFilterList() {
      ArrayList var1 = Lists.newArrayList(this.filterMap.values());
      this.filterList.set(var1);
   }

   public void removeFilters(Collection<Filter> var1) {
      boolean var2 = false;

      for (Filter var4 : var1) {
         var2 |= this.filterMap.remove(var4.getKey()) != null;
      }

      if (var2) {
         this.updateFilterList();
      }
   }

   private void updateCustomSearchSubtype() {
      NetSuiteSearchRecord var1 = this.object.get();
      NetSuiteSearchAdvancedProperties var2 = this.advancedpara.get();
      if (var1 != null && var2 != null) {
         if (!var2.getSearchType().isSavedSearch() && var2.hasCustomSearchObjects()) {
            var1.setCustomSearchSubtypes(var2.getCustomSearchObjects());
         }
      }
   }

   private NetSuiteSearchRecordValue createNetSuiteSearchRecordValue() {
      NetSuiteSearchRecord var1 = this.object.get();
      if (var1 == null) {
         return null;
      }

      this.updateCustomSearchSubtype();
      NetSuiteSearchCategory var2 = this.getSearchType();
      NetSuiteSearchRecordValue var3 = new NetSuiteSearchRecordValue(this.object.get());
      if (var2.isSavedSearch()) {
         var3.setSavedSearchId(this.savedSearchModel.getSelectedSavedSearchId());
      } else {
         for (Filter var5 : this.filterList) {
            String var6 = this.recordTreeModel.getInternalName(var5.getObjectName());
            var3.put(var6, var5.getFieldName(), var5.getFieldValue());
         }

         this.recordTreeModel.updateSelectedColumns(var3);
      }

      return var3;
   }

   public void updateResult() {
      this.searchXml = null;
      this.invalidMessage.setLength(0);
      NetSuiteSearchRecordValue var1 = this.createNetSuiteSearchRecordValue();
      if (var1 == null) {
         this.invalidMessage.append("Fatal Bug: The structure is unavailable. ");
      } else {
         NetSuiteSearchCategory var2 = var1.getNetSuiteSearchTypeFromValue();
         NetSuiteSearchCategory var3 = this.getSearchType();
         boolean var4 = var2 == var3;
         if (var4) {
            this.searchXml = var1.toXml(this.advancedpara.get());
         } else {
            switch (var3) {
               case NetSuiteSearchAdvancedSavedSearch:
                  this.invalidMessage.append("Please select a 'Saved Search' from the drop-down list.");
                  break;
               case NetSuiteSearchAdvanced:
                  this.invalidMessage.append("No return column(s) selected! Advanced Search requires to select response columns for the NetSuite Response.");
                  break;
               case NetSuiteSearch:
                  this.invalidMessage
                     .append(
                        "The criteria selected is not compatible with the type of search selected in the wizard. 'Expanded Search' require that the criteria be defined on an associated object join.\n If the criteria and the fields being retrieved are on the same object, please use a 'Basic Search'."
                     );
               case NetSuiteSearchBasic:
            }

            this.searchXml = var1.toXml(this.advancedpara.get());
         }
      }
   }

   public NetSuiteQueryPageSize getPageSize() {
      return this.pageSize.get();
   }

   public void setPageSize(NetSuiteQueryPageSize var1) {
      this.pageSize.set(var1);
   }

   public String toXml() {
      return this.searchXml;
   }

   public NetSuiteQuery.QueryParams getDefinedParams() {
      this.updateResult();
      String var1 = this.toXml();
      NetSuiteQueryPageSize var2 = this.getPageSize();
      return new NetSuiteQuery.QueryParams(var1, var2, this.advancedpara.get());
   }

   private final class SelectedFieldListener implements PropertyChangeListener {
      private SelectedFieldListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = (String)var1.getNewValue();
         if (var2 == null) {
            QueryBuilderModel.this.selectedFilters.clear();
            QueryBuilderModel.this.operators.clear();
            QueryBuilderModel.this.selectedOperator.set(null);
         } else {
            QueryBuilderModel.this.selectedFilters.clear();
            String var3 = QueryBuilderModel.this.getSelectedObjectName();
            List var4 = this.getAvailableOperators(var3, var2);
            QueryBuilderModel.this.operators.set(var4);
            Filter var5 = QueryBuilderModel.this.filterMap.get(Filter.getKey(var3, var2));
            if (var5 == null) {
               if (var4.size() > 1) {
                  NetSuiteSearchOperator var6 = QueryBuilderModel.this.selectedOperator.get();
                  if (var6 != null && var4.contains(var6)) {
                     return;
                  }
               }

               QueryBuilderModel.this.selectedOperator.set((NetSuiteSearchOperator)var4.get(0));
            } else {
               QueryBuilderModel.this.selectedFilters.set(Collections.singletonList(var5));
            }
         }
      }

      private List<NetSuiteSearchOperator> getAvailableOperators(String var1, String var2) {
         SearchField var3 = QueryBuilderModel.this.getFieldMetaData(var1, var2);
         List var4 = var3.getNetSuiteSearchOperators();
         return var4.isEmpty() ? Collections.singletonList(NetSuiteSearchOperator.none) : var4;
      }
   }

   private class SelectedFiltersListener implements PropertyChangeListener {
      private SelectedFiltersListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         List var2 = (List)var1.getNewValue();
         if (var2.size() == 1) {
            Filter var3 = (Filter)var2.get(0);
            QueryBuilderModel.this.selectedField.set(var3.getFieldName());
            SearchField var4 = var3.getFieldDef();
            List var5 = var4.getNetSuiteSearchOperators();
            QueryBuilderModel.this.operators.set(var5);
            QueryBuilderModel.this.selectedOperator.set(var3.getFieldValue().getNetSuiteSearchOperator());
         } else {
            QueryBuilderModel.this.operators.clear();
            QueryBuilderModel.this.selectedOperator.set(null);
         }
      }
   }
}
