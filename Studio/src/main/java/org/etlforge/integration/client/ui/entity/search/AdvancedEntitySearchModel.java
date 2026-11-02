package org.etlforge.integration.client.ui.entity.search;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public final class AdvancedEntitySearchModel extends AbstractPropertyBasedModel {
   public static final String MATCHING_ITEMS = "matches";
   public static final String OPTIONS = "options";
   private final Property<List<IntegrationEntity>> matchingItems = this.newProperty("matches");
   private final Property<SearchOptions> options;

   public AdvancedEntitySearchModel() {
      this.matchingItems.set(new ArrayList<>());
      this.matchingItems.setAllowsNull(false);
      this.options = this.newProperty("options");
      this.options.set(new SearchOptions(false, false, SearchFieldCombineMode.AND, false));
      this.options.setAllowsNull(false);
   }

   public List<IntegrationEntity> getMatchingItems() {
      return Lists.newArrayList(this.matchingItems.get());
   }

   Property<List<IntegrationEntity>> getMatchingItemsProperty() {
      return this.matchingItems;
   }

   public void setMatchingItems(List<IntegrationEntity> var1) {
      this.matchingItems.set(Lists.newArrayList(var1));
   }

   public SearchOptions getOptions() {
      return this.options.get();
   }

   public void setOptions(SearchOptions var1) {
      this.options.set(var1);
   }
}
