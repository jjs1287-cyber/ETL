package org.etlforge.integration.client.ui.entity.search;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;

public final class PropertySearchType extends AbstractSearchType {
   private final SearchValuesCollectorLookup collectors;

   public PropertySearchType(SearchValuesCollectorLookup var1) {
      super("Properties");
      this.collectors = Preconditions.checkNotNull(var1, "collectors");
   }

   @Override
   public Iterable<String> getStringsToSearch(IntegrationEntity var1) {
      ArrayList var2 = Lists.newArrayList();
      SearchValuesCollector var3 = this.collectors.getSearchValuesCollector(var1);
      if (var3 != null) {
         var2.addAll(var3.getValues(var1));
      }

      return Iterables.filter(var2, Predicates.notNull());
   }

   @Override
   public void setProject(IntegrationProject var1) {
   }
}
