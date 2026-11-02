package org.etlforge.integration.client.ui.entity.search;

import com.google.common.base.Predicate;
import org.etlforge.integration.client.entity.search.SearchableProperty;
import org.etlforge.integration.client.entity.search.StringList;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class StringListSearchField extends AbstractStringBasedSearchField {
   private final SearchableProperty<StringList> property;

   public StringListSearchField(SearchableProperty<StringList> var1) {
      this.property = var1;
   }

   @Override
   public SearchableProperty getProperty() {
      return this.property;
   }

   @Override
   public Object getValue() {
      return this.getSearchString();
   }

   @Override
   public Predicate<IntegrationEntity> getSearcher(SearchOptions var1) {
      if (!this.isEnabled()) {
         return SearchFieldUtils.ALL;
      }

      final Predicate var2 = this.getPredicate(var1);
      return new Predicate<IntegrationEntity>() {
         public boolean apply(IntegrationEntity var1) {
            for (String var3 : StringListSearchField.this.property.getValue(var1)) {
               if (var2.apply(var3)) {
                  return true;
               }
            }

            return false;
         }
      };
   }
}
