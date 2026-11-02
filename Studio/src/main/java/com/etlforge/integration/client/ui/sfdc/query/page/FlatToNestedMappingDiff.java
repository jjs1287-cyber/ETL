package com.etlforge.integration.client.ui.sfdc.query.page;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.structure.mapping.Mapping;

final class FlatToNestedMappingDiff implements FieldListChange.MappingDiff {
   private final QueryStructure oldQuery;
   private final QueryStructure newQuery;
   private List<Mapping> resultingMappings;

   public FlatToNestedMappingDiff(QueryStructure var1, QueryStructure var2) {
      assert var1.isFlat() && var2.isNested();
      this.oldQuery = var1;
      this.newQuery = var2;
   }

   @Override
   public List<Mapping> apply(List<Mapping> var1) {
      this.resultingMappings = Lists.newArrayList();
      this.processRootObject(var1);
      this.processChildObjects();
      return this.resultingMappings;
   }

   private void processRootObject(List<Mapping> var1) {
      ObjectLevelMappingDiff var2 = new ObjectLevelMappingDiff(this.oldQuery, this.newQuery);
      List var3 = var2.apply(var1);
      this.resultingMappings.addAll(var3);
   }

   private void processChildObjects() {
      this.resultingMappings.addAll(this.newQuery.getChildObjectMappingsToComplexTextTarget());
   }
}
