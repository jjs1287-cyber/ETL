package org.etlforge.integration.client.ui.entity.search;

import com.google.common.base.Predicate;
import org.etlforge.integration.client.entity.search.SearchableProperty;
import org.etlforge.integration.client.entity.search.StringSearcher;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class StringSearchField extends AbstractStringBasedSearchField {
   private final SearchableProperty<String> property;

   public StringSearchField(SearchableProperty<String> var1) {
      this.property = var1;
   }

   @Override
   public SearchableProperty<String> getProperty() {
      return this.property;
   }

   public String getValue() {
      return this.getSearchString();
   }

   @Override
   public Predicate<IntegrationEntity> getSearcher(SearchOptions var1) {
      if (!this.isEnabled()) {
         return SearchFieldUtils.ALL;
      }

      Predicate var2 = this.getPredicate(var1);
      return new StringSearcher(this.property, var2);
   }
}
