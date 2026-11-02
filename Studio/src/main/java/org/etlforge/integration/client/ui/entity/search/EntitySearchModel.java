package org.etlforge.integration.client.ui.entity.search;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.util.LinkedHashSet;
import java.util.List;
import org.etlforge.application.ui.search.string.StringSearchModel;
import org.etlforge.application.ui.search.string.StringSearchModelListener;
import org.etlforge.application.ui.search.string.StringSearchRule;
import org.etlforge.collections.Iterables2;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.string.StringMatcher;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;
import org.etlforge.util.name.NameSorter;

public final class EntitySearchModel extends AbstractPropertyBasedModel {
   public static final String MATCHING_ENTITIES = "matches";
   public static final String SEARCH_STRING = "search_string";
   public static final String STRING_MATCHER = "string_matcher";
   public static final String INCLUDED_TYPES = "included_types";
   public static final String SEARCH_TYPE = "search_type";
   private ImmutableList<IntegrationEntity> entities;
   private final ListProperty<IntegrationEntity> matches = this.newListProperty("matches");
   private final Property<String> searchString = this.newProperty("search_string", "");
   private final Property<StringMatcher> stringMatcher = this.newProperty("string_matcher");
   private final LinkedHashSet<EntityType> supportedEntityTypes;
   private final Property<ImmutableSet<EntityType>> selectedEntityTypes;
   private final Property<SearchType> searchType;
   private StringSearchModel stringSearchModel;
   private StringSearchModelListener stringSearchModelListener;
   private final SearchType[] searchTypes;

   public EntitySearchModel(IntegrationProject var1, Iterable<EntityType> var2, SearchType[] var3) {
      this(getSearchableProjectItems(var1), var2, var3);
   }

   private static List<IntegrationEntity> getSearchableProjectItems(IntegrationProject var0) {
      List var1 = var0.getAllEntities();
      return Iterables2.sortedCopy(Iterables.filter(var1, EntityFilters.EDITABLE), NameSorter.ignoreCase());
   }

   public EntitySearchModel(Iterable<? extends IntegrationEntity> var1, Iterable<EntityType> var2, SearchType[] var3) {
      this.entities = ImmutableList.copyOf(var1);
      this.searchTypes = (SearchType[])var3.clone();
      this.supportedEntityTypes = Sets.newLinkedHashSet();

      for (EntityType var5 : var2) {
         if (var5.canBeInstantiated()) {
            this.supportedEntityTypes.add(var5);
         }
      }

      this.selectedEntityTypes = this.newProperty("included_types", ImmutableSet.copyOf(this.supportedEntityTypes));
      this.searchType = this.newProperty("search_type", var3[0]);
   }

   public void setProject(IntegrationProject var1) {
      for (SearchType var5 : this.searchTypes) {
         var5.setProject(var1);
      }

      if (var1 == null) {
         this.clear();
      } else {
         this.setAvailableEntities(getSearchableProjectItems(var1));
      }
   }

   private void clear() {
      this.entities = ImmutableList.of();
      this.sendChange("entities", null, null);
   }

   private void setAvailableEntities(Iterable<? extends IntegrationEntity> var1) {
      this.entities = ImmutableList.copyOf(var1);
      this.sendChange("entities", null, null);
   }

   public ImmutableSet<EntityType> getSupportedEntityTypes() {
      return ImmutableSet.copyOf(this.supportedEntityTypes);
   }

   public boolean areAllEntityTypesSelected() {
      return this.selectedEntityTypes.get().size() >= this.supportedEntityTypes.size();
   }

   public SearchType[] getSearchTypes() {
      return (SearchType[])this.searchTypes.clone();
   }

   public void setStringSearchModel(StringSearchModel var1) {
      if (this.stringSearchModel != null) {
         this.stringSearchModel.removeModelListener(this.stringSearchModelListener);
      }

      if (var1 != null) {
         this.stringSearchModelListener = new EntitySearchModel.StringSearchModelListenerImpl();
         var1.addModelListener(this.stringSearchModelListener);
         this.searchString.set(var1.getSearchString());
         StringSearchRule var2 = var1.getSearchRule();
         this.stringMatcher.set(var2 != null ? var2.getMatcher() : null);
      } else {
         this.searchString.set("");
         this.stringMatcher.set(null);
      }

      this.stringSearchModel = var1;
   }

   public Iterable<IntegrationEntity> getEntities() {
      SearchType var1 = this.getSearchType();
      EntityFilter var2 = var1.getFilter();
      return Iterables.filter(this.entities, var2);
   }

   public void setMatchingEntites(List<IntegrationEntity> var1) {
      if (var1.isEmpty() && this.matches.isEmpty()) {
         this.sendChange("matches", null, null);
      } else {
         this.matches.set(var1);
      }
   }

   public IntegrationEntity getSingleMatch() {
      return this.matches.size() == 1 ? this.matches.get().get(0) : null;
   }

   public void setSearchString(String var1) {
      this.searchString.set(var1);
   }

   public String getSearchString() {
      return this.searchString.get();
   }

   public boolean hasSearchString() {
      return !this.getSearchString().isEmpty();
   }

   public void setStringMatcher(StringMatcher var1) {
      this.stringMatcher.set(var1);
   }

   public StringMatcher getStringMatcher() {
      return this.stringMatcher.get();
   }

   public void setSelectedEntityTypes(ImmutableSet<EntityType> var1) {
      this.selectedEntityTypes.set(var1);
   }

   public ImmutableSet<EntityType> getSelectedEntityTypes() {
      return this.selectedEntityTypes.get();
   }

   public void setSearchType(SearchType var1) {
      this.searchType.set(var1);
   }

   public SearchType getSearchType() {
      return this.searchType.get();
   }

   public boolean isMatch(String var1) {
      return this.getStringMatcher().matches(this.getSearchString(), var1);
   }

   public boolean isEntityTypeSelected(EntityType var1) {
      return this.getSelectedEntityTypes().contains(var1);
   }

   private class StringSearchModelListenerImpl implements StringSearchModelListener {
      private StringSearchModelListenerImpl() {
      }

      @Override
      public void searchRuleChanged(StringSearchModel var1, StringSearchRule var2) {
         EntitySearchModel.this.setStringMatcher(var2.getMatcher());
      }

      @Override
      public void searchStringChanged(StringSearchModel var1, String var2) {
         EntitySearchModel.this.setSearchString(var2);
      }
   }
}
