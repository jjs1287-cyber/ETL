package org.etlforge.integration.client.entity.search;

import com.google.common.base.Predicate;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class StringSearcher implements Predicate<IntegrationEntity> {
   private final SearchableProperty<String> property;
   private final Predicate<String> search;

   public StringSearcher(SearchableProperty<String> var1, Predicate<String> var2) {
      this.property = var1;
      this.search = var2;
   }

   public boolean apply(IntegrationEntity var1) {
      return this.search.apply(this.property.getValue(var1));
   }
}
