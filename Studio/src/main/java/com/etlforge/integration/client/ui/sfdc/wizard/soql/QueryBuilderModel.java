package com.etlforge.integration.client.ui.sfdc.wizard.soql;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.etlforge.integration.client.sfdc.data.SoqlObject;
import com.etlforge.integration.client.ui.sfdc.wizard.soql.relational.ChildRelationship;
import com.etlforge.integration.client.ui.sfdc.wizard.soql.relational.ParentRelationship;
import com.etlforge.integration.salesforce.ui.NameDisplayMode;
import com.etlforge.integration.salesforce.ui.NameDisplayModes;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.etlforge.integration.salesforce.parser.ParsedQuery;
import org.etlforge.integration.salesforce.parser.SoqlParser;
import org.etlforge.integration.salesforce.parser.SoqlParserFactory;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.DefaultProperty;
import org.etlforge.util.Property;

public final class QueryBuilderModel extends AbstractPropertyBasedModel {
   public static final String OBJECT = "object";
   public static final String FIELDS = "fields";
   public static final String CONDITIONS = "conditions";
   public static final String QUERY_ALL = "queryAll";
   public static final String SOQL = "soql";
   private final Property<SObjectSchema> sobject;
   private Map<SObjectSchema.Field, Boolean> fields;
   private final Property<Set<SObjectSchema.Field>> selectedFields = this.newProperty("fields");
   private final QueryBuilderModel.ChildToParentFields childToParentFields = new QueryBuilderModel.ChildToParentFields();
   private final Map<String, Property<Set<SObjectSchema.Field>>> nestedFields = Maps.newHashMap();
   private final Map<String, QueryBuilderModel.ChildToParentFields> nestedChildToParentFields = Maps.newHashMap();
   private final Property<LinkedHashSet<Condition>> conditions = this.newProperty("conditions", new LinkedHashSet<>());
   private final Property<Boolean> queryAll = this.newProperty("queryAll", Boolean.FALSE);
   private final Property<String> soql = this.newProperty("soql", "");
   private NameDisplayModes nameDisplayModes = NameDisplayModes.api();
   private final QueryBuilderModel.SoqlUpdater soqlUpdater = new QueryBuilderModel.SoqlUpdater();

   public QueryBuilderModel() {
      this(null);
   }

   public QueryBuilderModel(SObjectSchema var1) {
      this.sobject = this.newProperty("object", var1);
      this.removeAllFields();
      this.selectedFields.setAllowsNull(false);
      this.conditions.setAllowsNull(false);
      this.soql.setAllowsNull(false);
      this.installSoqlBuilder();
   }

   private void installSoqlBuilder() {
      QueryBuilderModel.SoqlUpdater var1 = this.soqlUpdater;
      this.selectedFields.addListener(var1);
      this.conditions.addListener(var1);
      this.sobject.addListener(var1);
   }

   public SObjectSchema getSObject() {
      return this.sobject.get();
   }

   public void setSObject(SObjectSchema var1) {
      if (var1 != this.getSObject()) {
         this.removeAllFields();
         this.removeAllConditions();
         this.childToParentFields.clear();

         for (Property var3 : this.nestedFields.values()) {
            var3.removeListener(this.soqlUpdater);
         }

         this.nestedFields.clear();

         for (QueryBuilderModel.ChildToParentFields var5 : this.nestedChildToParentFields.values()) {
            var5.clear();
         }

         this.nestedChildToParentFields.clear();
         this.sobject.set(var1);
      }
   }

   public List<SObjectSchema.Field> getAllFields() {
      SObjectSchema var1 = this.getSObject();
      return var1 == null ? Collections.emptyList() : this.nameDisplayModes.field.sortedCopy(var1.getFields());
   }

   public List<SObjectSchema.Field> getFilterableFields() {
      List var1 = this.getAllFields();
      Iterator var2 = var1.iterator();

      while (var2.hasNext()) {
         if (!((SObjectSchema.Field)var2.next()).isFilterable()) {
            var2.remove();
         }
      }

      return var1;
   }

   public void removeAllFields() {
      this.recreateFieldMap(Boolean.FALSE);
      this.selectedFields.set(new HashSet<>());
   }

   private void recreateFieldMap(Boolean var1) {
      this.fields = Maps.newHashMap();

      for (SObjectSchema.Field var3 : this.getAllFields()) {
         this.fields.put(var3, var1);
      }
   }

   public void selectAllFields() {
      this.recreateFieldMap(Boolean.TRUE);
      HashSet var1 = Sets.newHashSet(this.fields.keySet());
      this.selectedFields.set(var1);
   }

   public void setSelectedFields(Collection<SObjectSchema.Field> var1) {
      this.recreateFieldMap(Boolean.FALSE);

      for (SObjectSchema.Field var3 : var1) {
         if (this.fields.containsKey(var3)) {
            this.fields.put(var3, Boolean.TRUE);
         }
      }

      this.updateSelectedFieldsProperty();
   }

   private void updateSelectedFieldsProperty() {
      HashSet var1 = Sets.newHashSet();

      for (Entry var3 : this.fields.entrySet()) {
         if (var3.getValue() == Boolean.TRUE) {
            var1.add(var3.getKey());
         }
      }

      this.selectedFields.set(var1);
   }

   public void setFieldSelected(SObjectSchema.Field var1, Boolean var2) {
      if (this.fields.put(var1, var2) != var2) {
         this.updateSelectedFieldsProperty();
      }
   }

   public Set<SObjectSchema.Field> getSelectedFields() {
      return Sets.newHashSet(this.selectedFields.get());
   }

   public Property<Set<ChildToParentField>> getChildToParentFieldsProperty(ParentRelationship var1) {
      if (var1.belongsToRootObject()) {
         return this.childToParentFields.getPropertyFor(var1);
      }

      ChildRelationship var2 = var1.getChildRelation();
      return this.getNestedChildToParentFieldsProperty(var2.getName(), var1);
   }

   private Property<Set<ChildToParentField>> getNestedChildToParentFieldsProperty(String var1, ParentRelationship var2) {
      QueryBuilderModel.ChildToParentFields var3 = this.nestedChildToParentFields.get(var1);
      if (var3 == null) {
         var3 = new QueryBuilderModel.ChildToParentFields();
         this.nestedChildToParentFields.put(var1, var3);
      }

      return var3.getPropertyFor(var2);
   }

   public void setNestedFieldSelected(String var1, SObjectSchema.Field var2, boolean var3) {
      Property var4 = this.getNestedFieldsProperty(var1);
      updateSetProperty(var4, var2, var3);
   }

   public Property<Set<SObjectSchema.Field>> getNestedFieldsProperty(String var1) {
      Property var2 = this.nestedFields.get(var1);
      if (var2 == null) {
         var2 = this.newProperty(var1 + ".fields");
         var2.set(new HashSet());
         var2.setAllowsNull(false);
         var2.addListener(this.soqlUpdater);
         this.nestedFields.put(var1, var2);
      }

      return var2;
   }

   private static <T> void updateSetProperty(Property<Set<T>> var0, T var1, boolean var2) {
      HashSet var3 = Sets.newHashSet((Iterable)var0.get());
      boolean var4 = var2 ? var3.add(var1) : var3.remove(var1);
      if (var4) {
         var0.set(var3);
      }
   }

   public void addCondition(Condition var1) {
      Preconditions.checkNotNull(var1);
      LinkedHashSet var2 = Sets.newLinkedHashSet(this.conditions.get());
      if (var2.add(var1)) {
         this.conditions.set(var2);
      }
   }

   public void removeCondition(Condition var1) {
      Preconditions.checkNotNull(var1);
      LinkedHashSet var2 = Sets.newLinkedHashSet(this.conditions.get());
      if (var2.remove(var1)) {
         this.conditions.set(var2);
      }
   }

   public void removeAllConditions() {
      this.conditions.set(new LinkedHashSet<>());
   }

   public LinkedHashSet<Condition> getConditions() {
      return Sets.newLinkedHashSet(this.conditions.get());
   }

   public boolean isQueryAll() {
      return this.queryAll.get();
   }

   public String getQueryString() {
      return this.soql.get().trim();
   }

   public void setQueryString(String var1) {
      this.soql.set(var1);
   }

   public ParsedQuery parseQuery() {
      SoqlParser var1 = SoqlParserFactory.newParser();
      String var2 = this.getQueryString();
      return var1.parse(var2);
   }

   public SoqlObject getSoqlObject() {
      SObjectSchema var1 = this.getSObject();
      if (var1 == null) {
         return null;
      }

      String var2 = this.getQueryString();
      return var2.isEmpty() ? null : new SoqlObject(var2, var1.getExpandedName(), this.queryAll.get());
   }

   public NameDisplayMode getObjectNameDisplayMode() {
      return this.nameDisplayModes.object;
   }

   public NameDisplayMode getFieldNameDisplayMode() {
      return this.nameDisplayModes.field;
   }

   public void setNameDisplayModes(NameDisplayModes var1) {
      this.nameDisplayModes = Preconditions.checkNotNull(var1);
   }

   private class ChildToParentFields {
      private final Map<ParentRelationship, Property<Set<ChildToParentField>>> properties = Maps.newHashMap();

      private ChildToParentFields() {
      }

      public Property<Set<ChildToParentField>> getPropertyFor(ParentRelationship var1) {
         if (this.properties.containsKey(var1)) {
            return this.properties.get(var1);
         }

         DefaultProperty var2 = QueryBuilderModel.this.newProperty(var1.getPath().toString() + ".c2p");
         var2.set(new HashSet());
         var2.setAllowsNull(false);
         var2.addListener(QueryBuilderModel.this.soqlUpdater);
         this.properties.put(var1, var2);
         return var2;
      }

      public void clear() {
         for (Property var2 : this.properties.values()) {
            var2.removeListener(QueryBuilderModel.this.soqlUpdater);
         }

         this.properties.clear();
      }

      public Iterable<ChildToParentField> getFields() {
         HashSet var1 = Sets.newHashSet();

         for (Property var3 : this.properties.values()) {
            var1.addAll((Collection)var3.get());
         }

         return var1;
      }
   }

   private class SoqlUpdater implements PropertyChangeListener {
      private SoqlUpdater() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (this.isNewObject(var1)) {
            QueryBuilderModel.this.soql.set("");
         } else {
            QueryBuilderModel.this.soql.set(this.buildSoql());
         }
      }

      private boolean isNewObject(PropertyChangeEvent var1) {
         return "object".equals(var1.getPropertyName());
      }

      public String buildSoql() {
         SoqlConstructor var1 = new SoqlConstructor(
            QueryBuilderModel.this.getSObject().getName(),
            QueryBuilderModel.this.getSelectedFields(),
            QueryBuilderModel.this.childToParentFields.getFields(),
            this.createNestedQueries(),
            QueryBuilderModel.this.getConditions()
         );
         return var1.buildSoql();
      }

      private Iterable<NestedQuery> createNestedQueries() {
         ArrayList var1 = Lists.newArrayList();
         HashSet var2 = Sets.newHashSet();

         for (Entry var4 : QueryBuilderModel.this.nestedFields.entrySet()) {
            String var5 = (String)var4.getKey();
            Iterable var6 = (Iterable)((Property)var4.getValue()).get();
            Iterable var7;
            if (QueryBuilderModel.this.nestedChildToParentFields.containsKey(var5)) {
               var7 = QueryBuilderModel.this.nestedChildToParentFields.get(var5).getFields();
            } else {
               var7 = Sets.newHashSet();
            }

            var1.add(new NestedQuery(var5, var6, var7));
            var2.add(var5);
         }

         for (Entry var9 : QueryBuilderModel.this.nestedChildToParentFields.entrySet()) {
            String var10 = (String)var9.getKey();
            if (!var2.contains(var10)) {
               HashSet var11 = Sets.newHashSet();
               Iterable var12 = ((QueryBuilderModel.ChildToParentFields)var9.getValue()).getFields();
               var1.add(new NestedQuery(var10, var11, var12));
            }
         }

         return Iterables.filter(var1, new Predicate<NestedQuery>() {
            public boolean apply(NestedQuery var1) {
               return !var1.isEmpty();
            }
         });
      }
   }
}
