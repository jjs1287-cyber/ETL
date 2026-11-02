package com.etlforge.integration.client.ui.sfdc.query.page;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.entity.sfquery.AutoMappingFactory;
import org.etlforge.integration.data.entity.sfquery.ComplexTextAutoMapper;
import org.etlforge.integration.data.entity.sfquery.FlatQueryMappingFactory;
import org.etlforge.integration.data.entity.sfquery.QueryResponseTextStructureFactory;
import org.etlforge.integration.data.entity.sfquery.RelationshipQueryMappingFactories;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.salesforce.parser.ParsedQuery;
import org.etlforge.integration.structure.mapping.Mapping;

final class QueryStructure {
   private final ParsedQuery query;
   private final ParsedQuery parent;
   private final String name;
   private final FieldList fields;
   private final ImmutableMap<String, QueryStructure> children;

   public QueryStructure(ParsedQuery var1) {
      this(var1, null);
   }

   private QueryStructure(ParsedQuery var1, ParsedQuery var2) {
      this.query = var1;
      this.parent = var2;
      this.name = var1.getObjectName();
      this.fields = new FieldList(var1);
      Builder var3 = ImmutableMap.builder();

      for (ParsedQuery var5 : var1.getSubQueries()) {
         QueryStructure var6 = new QueryStructure(var5, var1);
         var3.put(var5.getObjectName(), var6);
      }

      this.children = var3.build();
   }

   public String getObjectName() {
      return this.name;
   }

   public boolean isFlat() {
      return this.children.isEmpty();
   }

   public boolean isNested() {
      return !this.isFlat();
   }

   public FieldList getFields() {
      return this.fields;
   }

   public TextStructure buildTextStructure() {
      return QueryResponseTextStructureFactory.buildStructure(this.query);
   }

   public ImmutableSet<QueryStructure> subQueriesInThisButNotInThat(QueryStructure var1) {
      com.google.common.collect.ImmutableSet.Builder var2 = ImmutableSet.builder();

      for (QueryStructure var4 : this.children.values()) {
         if (!var1.children.containsKey(var4.getObjectName())) {
            var2.add(var4);
         }
      }

      return var2.build();
   }

   public ImmutableMap<QueryStructure, QueryStructure> subQueriesInThisAndInThat(QueryStructure var1) {
      Builder var2 = ImmutableMap.builder();

      for (QueryStructure var4 : this.children.values()) {
         QueryStructure var5 = var1.children.get(var4.getObjectName());
         if (var5 != null) {
            var2.put(var4, var5);
         }
      }

      return var2.build();
   }

   public ImmutableList<Mapping> getChildObjectMappingsToComplexTextTarget() {
      ComplexTextAutoMapper var1 = new ComplexTextAutoMapper(this.query);
      List var2 = var1.getAllChildObjectMappings();
      return ImmutableList.copyOf(var2);
   }

   public AutoMappingFactory getAutoMapperForTextTarget() {
      if (this.parent == null && this.isFlat()) {
         return new FlatQueryMappingFactory(this.getObjectName());
      } else if (this.parent == null) {
         RelationshipQueryMappingFactories var2 = new RelationshipQueryMappingFactories(this.getObjectName());
         return var2.forRootObject();
      } else {
         RelationshipQueryMappingFactories var1 = new RelationshipQueryMappingFactories(this.parent.getObjectName());
         return var1.forChildObject(this.name);
      }
   }

   public List<Mapping> getMappingsForTextTarget() {
      ArrayList var1 = Lists.newArrayList();
      AutoMappingFactory var2 = this.getAutoMapperForTextTarget();

      for (String var4 : this.query.getFieldNames()) {
         Mapping var5 = var2.getMapping(var4);
         var1.add(var5);
      }

      return var1;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.name, this.fields, this.children);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof QueryStructure)) {
         return false;
      }

      QueryStructure var2 = (QueryStructure)var1;
      return this.name.equals(var2.name) && this.fields.equals(var2.fields) && this.children.equals(var2.children);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this)
         .add("name", this.name)
         .add("fields", this.fields)
         .add("children", this.children.isEmpty() ? "[]" : "\n\t" + Joiner.on("\n\t").join(this.children.values()))
         .toString();
   }
}
