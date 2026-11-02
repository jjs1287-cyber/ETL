package org.etlforge.integration.client.ui.entity.search;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import org.etlforge.integration.client.entity.search.SearchableProperty;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.widget.text.IntegerDocument;

public final class IntegerSearchField extends AbstractStringBasedSearchField {
   private final SearchableProperty<Integer> property;

   public IntegerSearchField(SearchableProperty<Integer> var1) {
      super(new IntegerDocument());
      this.property = var1;
   }

   @Override
   public SearchableProperty<Integer> getProperty() {
      return this.property;
   }

   @Override
   public Predicate<IntegrationEntity> getSearcher(SearchOptions var1) {
      if (!this.isEnabled()) {
         return SearchFieldUtils.ALL;
      }

      final Integer var2 = this.getValue();
      return var2 == null ? SearchFieldUtils.NONE : new Predicate<IntegrationEntity>() {
         public boolean apply(IntegrationEntity var1) {
            return Objects.equal(var2, IntegerSearchField.this.property.getValue(var1));
         }
      };
   }

   public Integer getValue() {
      String var1 = this.getSearchString();
      if (!var1.isEmpty()) {
         try {
            return Integer.valueOf(var1);
         } catch (NumberFormatException var3) {
         }
      }

      return null;
   }
}
