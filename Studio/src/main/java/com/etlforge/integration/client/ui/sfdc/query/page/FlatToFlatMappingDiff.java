package com.etlforge.integration.client.ui.sfdc.query.page;

import java.util.List;
import org.etlforge.integration.structure.mapping.Mapping;

final class FlatToFlatMappingDiff implements FieldListChange.MappingDiff {
   private final QueryStructure oldQuery;
   private final QueryStructure newQuery;

   public FlatToFlatMappingDiff(QueryStructure var1, QueryStructure var2) {
      assert var1.isFlat() && var2.isFlat();
      this.oldQuery = var1;
      this.newQuery = var2;
   }

   @Override
   public List<Mapping> apply(List<Mapping> var1) {
      ObjectLevelMappingDiff var2 = new ObjectLevelMappingDiff(this.oldQuery, this.newQuery);
      return var2.apply(var1);
   }
}
