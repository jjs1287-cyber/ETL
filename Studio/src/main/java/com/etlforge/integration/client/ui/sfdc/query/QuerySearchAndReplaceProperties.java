package com.etlforge.integration.client.ui.sfdc.query;

import com.etlforge.integration.client.ui.sfdc.search.ObjectNameProperty;
import java.util.EnumSet;
import java.util.Set;
import org.etlforge.integration.client.entity.search.KeyValueStringProperty;
import org.etlforge.integration.client.entity.search.ReplaceableKeyValueBooleanProperty;
import org.etlforge.integration.client.entity.search.ReplaceableKeyValueIntegerProperty;
import org.etlforge.integration.client.entity.search.ReplaceableProperty;
import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaData;
import org.etlforge.integration.client.entity.search.SearchableEnumProperty;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.entity.sfquery.TargetHeaderMode;

final class QuerySearchAndReplaceProperties extends SearchAndReplaceMetaData {
   public QuerySearchAndReplaceProperties() {
      super(
         new ObjectNameProperty(),
         new KeyValueStringProperty("SOQL", "query_string"),
         new ReplaceableKeyValueBooleanProperty("Include deleted rows", "query_all", false),
         new ReplaceableKeyValueIntegerProperty("Batch size", "batch_size", 2000),
         new QuerySearchAndReplaceProperties.TargetHeaderModeProperty()
      );
   }

   private static class TargetHeaderModeProperty implements ReplaceableProperty<TargetHeaderMode>, SearchableEnumProperty<TargetHeaderMode> {
      private TargetHeaderModeProperty() {
      }

      @Override
      public String getName() {
         return "Target header";
      }

      @Override
      public Class<TargetHeaderMode> getValueType() {
         return TargetHeaderMode.class;
      }

      public TargetHeaderMode getValue(IntegrationEntity var1) {
         return ((SalesforceQuery)var1).getTargetHeaderMode();
      }

      @Override
      public Set<TargetHeaderMode> getSupportedValues() {
         return EnumSet.allOf(TargetHeaderMode.class);
      }

      public void setValue(IntegrationEntity var1, TargetHeaderMode var2) {
         ((SalesforceQuery)var1).setTargetHeaderMode(var2);
      }
   }
}
