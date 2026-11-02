package com.etlforge.integration.client.ui.sfdc.query.page;

import java.util.List;
import org.etlforge.integration.structure.mapping.Mapping;

final class NestedToFlatMappingDiff implements FieldListChange.MappingDiff {
   private final QueryStructure oldQuery;
   private final QueryStructure newQuery;

   public NestedToFlatMappingDiff(QueryStructure var1, QueryStructure var2) {
      assert var1.isNested() && var2.isFlat();
      this.oldQuery = var1;
      this.newQuery = var2;
   }

   @Override
   public List<Mapping> apply(List<Mapping> var1) {
      this.processChildObjects();
      return this.processRootObject(var1);
   }

   private void processChildObjects() {
   }

   private List<Mapping> processRootObject(List<Mapping> var1) {
      ObjectLevelMappingDiff var2 = new ObjectLevelMappingDiff(this.oldQuery, this.newQuery);
      return var2.apply(var1);
   }
}
