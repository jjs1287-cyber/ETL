package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import java.util.List;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;
import org.etlforge.xml.XmlStringBuilder;

final class FilterDefinitionModel extends AbstractPropertyBasedModel {
   public static final String OPERATOR = "filterOperator";
   public static final String ENTRIES = "entries";
   private final MSCrmObject object;
   private final Property<FilterOperator> operator = this.newProperty("filterOperator", FilterOperator.AND);
   private final ListProperty<FilterEntry> entries = this.newListProperty("entries");

   public FilterDefinitionModel(MSCrmObject var1) {
      this.object = var1;
      this.operator.setAllowsNull(false);
   }

   FilterDefinitionModel(FilterDefinitionModel var1) {
      this.object = var1.object;
      this.operator.setSilently(var1.operator.get());
      this.entries.setSilently(var1.entries.get());
   }

   public MSCrmObject getObject() {
      return this.object;
   }

   public void setFilterOperator(FilterOperator var1) {
      this.operator.set(var1);
   }

   public void addEntry(FilterEntry var1) {
      this.entries.add(var1);
   }

   public void setEntries(List<FilterEntry> var1) {
      this.entries.set(var1);
   }

   public void removeEntries(List<? extends FilterEntry> var1) {
      this.entries.removeAll(var1);
   }

   public void replaceEntry(FilterEntry var1, FilterEntry var2) {
      this.entries.identityReplace(var1, var2);
   }

   public Filter getFilter() {
      return Filter.builder(this.operator.get()).addEntries(this.entries.get()).build();
   }

   public void setFilter(Filter var1) {
      this.operator.set(var1.getOperator());
      this.entries.set(var1.getEntries());
   }

   public void appendToXml(XmlStringBuilder var1) {
      Filter var2 = this.getFilter();
      var2.appendToXml(var1);
   }
}
