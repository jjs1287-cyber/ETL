package com.etlforge.integration.client.ui.sfdc.query.page;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map.Entry;
import org.etlforge.integration.data.entity.sfquery.AutoMappingFactory;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.integration.structure.mapping.MappingFactory;
import org.etlforge.integration.structure.mapping.NodePath;

final class ObjectLevelMappingDiff {
   private final QueryStructure oldQuery;
   private final QueryStructure newQuery;
   private final AutoMappingFactory mappingFactory;
   private List<Mapping> resultingMappings;

   public ObjectLevelMappingDiff(QueryStructure var1, QueryStructure var2) {
      this.oldQuery = var1;
      this.newQuery = var2;
      this.mappingFactory = var2.getAutoMapperForTextTarget();
   }

   public List<Mapping> apply(List<Mapping> var1) {
      this.resultingMappings = Lists.newArrayList();
      this.resultingMappings.add(this.mappingFactory.getGeneratorMapping());
      this.addMappingsForNewFields();
      this.removeMappingsForRemovedFields();
      this.updateRemainingFieldMappings(var1);
      return this.resultingMappings;
   }

   private void addMappingsForNewFields() {
      ImmutableSet var1 = this.newQuery.getFields().inThisButNotInThat(this.oldQuery.getFields());
      if (!var1.isEmpty()) {
         for (Object __etlItem39 : var1) {
            FieldWrapper var3 = (FieldWrapper) __etlItem39;
            Mapping var4 = this.mappingFactory.getMapping(var3.getName());
            this.resultingMappings.add(var4);
         }
      }
   }

   private void removeMappingsForRemovedFields() {
   }

   private void updateRemainingFieldMappings(List<Mapping> var1) {
      ImmutableMap var2 = this.newQuery.getFields().inThisAndInThat(this.oldQuery.getFields());
      if (!var2.isEmpty()) {
         AutoMappingFactory var3 = this.oldQuery.getAutoMapperForTextTarget();
         ImmutableMap var4 = Maps.uniqueIndex(var1, new ObjectLevelMappingDiff.ToLowerCasePath());

         for (Object __etlItem55 : var2.entrySet()) {
            Entry var6 = (Entry) __etlItem55;
            FieldWrapper var7 = (FieldWrapper)var6.getKey();
            FieldWrapper var8 = (FieldWrapper)var6.getValue();
            NodePath var9 = this.getLowerCaseNodePath(var3, var8.getName());
            Mapping var10 = (Mapping)var4.get(var9);
            if (var10 != null) {
               NodePath var11 = this.mappingFactory.getTargetPath(var7.getName());
               Mapping var12 = MappingFactory.newMapping(var11, var10.getExpression());
               this.resultingMappings.add(var12);
            }
         }
      }
   }

   private NodePath getLowerCaseNodePath(AutoMappingFactory var1, String var2) {
      NodePath var3 = var1.getTargetPath(var2);
      return NodePath.of(var3.path.toLowerCase());
   }

   private static class ToLowerCasePath implements Function<Mapping, NodePath> {
      private ToLowerCasePath() {
      }

      public NodePath apply(Mapping var1) {
         return NodePath.of(var1.getTargetPath().path.toLowerCase());
      }
   }
}
