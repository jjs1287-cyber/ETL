package com.etlforge.integration.client.ui.sfdc.query.page;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map.Entry;
import org.etlforge.integration.structure.mapping.Mapping;

final class NestedToNestedMappingDiff implements FieldListChange.MappingDiff {
   private final QueryStructure oldQuery;
   private final QueryStructure newQuery;
   private List<Mapping> oldMappings;
   private List<Mapping> resultingMappings;

   public NestedToNestedMappingDiff(QueryStructure var1, QueryStructure var2) {
      assert var1.isNested() && var2.isNested();
      this.oldQuery = var1;
      this.newQuery = var2;
   }

   @Override
   public List<Mapping> apply(List<Mapping> var1) {
      this.oldMappings = var1;
      this.resultingMappings = Lists.newArrayList();
      this.processRootObject();
      this.processChildObjects();
      return this.resultingMappings;
   }

   private void processRootObject() {
      ObjectLevelMappingDiff var1 = new ObjectLevelMappingDiff(this.oldQuery, this.newQuery);
      List var2 = var1.apply(this.oldMappings);
      this.resultingMappings.addAll(var2);
   }

   private void processChildObjects() {
      this.addMappingsForNewChildObjects();
      this.removeMappingsForRemovedChildObjects();
      this.updateMappingsForRemainingChildObjects();
   }

   private void addMappingsForNewChildObjects() {
      for (QueryStructure var3 : this.newQuery.subQueriesInThisButNotInThat(this.oldQuery)) {
         List var4 = var3.getMappingsForTextTarget();
         this.resultingMappings.addAll(var4);
      }
   }

   private void removeMappingsForRemovedChildObjects() {
   }

   private void updateMappingsForRemainingChildObjects() {
      ImmutableMap var1 = this.newQuery.subQueriesInThisAndInThat(this.oldQuery);

      for (Object __etlItem55 : var1.entrySet()) {
         Entry var3 = (Entry) __etlItem55;
         ObjectLevelMappingDiff var4 = new ObjectLevelMappingDiff((QueryStructure)var3.getValue(), (QueryStructure)var3.getKey());
         List var5 = var4.apply(this.oldMappings);
         this.resultingMappings.addAll(var5);
      }
   }
}
