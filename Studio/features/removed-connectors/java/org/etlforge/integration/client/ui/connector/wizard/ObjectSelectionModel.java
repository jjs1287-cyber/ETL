package org.etlforge.integration.client.ui.connector.wizard;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import com.google.common.collect.ImmutableList.Builder;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;

public final class ObjectSelectionModel extends AbstractPropertyBasedModel {
   public static final String FILTER = "filter";
   public static final String OBJECTS = "objects";
   public static final String SELECTED_OBJECT = "selected_object";
   public static final String NAME_OPTION = "name_option";
   public static final String TYPE_FILTER = "type_filter";
   private final Property<String> filterText = this.newProperty("filter", "");
   private final ListProperty<ConnectorObject> filteredAndSortedObjects = this.newListProperty("objects");
   private final Property<ConnectorObject> selectedObject = this.newProperty("selected_object");
   private final ImmutableList<NameOption> nameOptions;
   private ImmutableList<ConnectorObject> allObjects = ImmutableList.of();
   private final Property<NameOption> nameOption = this.newProperty("name_option");
   private final Property<ObjectTypeFilter> typeFilter = this.newProperty("type_filter", ObjectTypeFilter.ALL);
   private ImmutableList<ObjectTypeFilter> typeFilters = ImmutableList.of();
   private ImmutableList<ConnectorObject> selectionPath = ImmutableList.of();
   private ConnectorEndpoint endpoint;

   public ObjectSelectionModel() {
      this(defaultNameOption(), Collections.emptyList());
   }

   private static ImmutableList<NameOption> defaultNameOption() {
      DefaultNameOption var0 = new DefaultNameOption("Name");
      return ImmutableList.of(var0);
   }

   public ObjectSelectionModel(Collection<? extends ObjectTypeFilter> var1) {
      this(defaultNameOption(), var1);
   }

   public ObjectSelectionModel(Collection<? extends NameOption> var1, Collection<? extends ObjectTypeFilter> var2) {
      this.nameOptions = copyNameOptions(var1);
      assert !this.nameOptions.isEmpty() : "copyNameOptions is supposed to guarantee at least one option";
      this.typeFilters = copyTypeFilters(var2);
      this.filteredAndSortedObjects.set(new ArrayList<>());
      this.filteredAndSortedObjects.setAllowsNull(false);
      this.nameOption.set(this.nameOptions.get(0));
      this.nameOption.setAllowsNull(false);
      this.typeFilter.setAllowsNull(false);
      this.installFilterHandler();
   }

   private static ImmutableList<NameOption> copyNameOptions(Iterable<? extends NameOption> var0) {
      ImmutableList var1 = ImmutableList.copyOf(var0);
      return var1.isEmpty() ? defaultNameOption() : var1;
   }

   private static ImmutableList<ObjectTypeFilter> copyTypeFilters(Collection<? extends ObjectTypeFilter> var0) {
      if (var0.isEmpty()) {
         return ImmutableList.of();
      }

      Builder var1 = ImmutableList.builder();
      var1.add(ObjectTypeFilter.ALL);
      var1.addAll(Iterables.filter(var0, Predicates.not(Predicates.equalTo(ObjectTypeFilter.ALL))));
      return var1.build();
   }

   private void installFilterHandler() {
      PropertyChangeListener var1 = new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            ObjectSelectionModel.this.updateFilteredListOfObjects();
         }
      };
      this.nameOption.addListener(var1);
      this.typeFilter.addListener(var1);
      this.filterText.addListener(var1);
   }

   public ConnectorEndpoint getEndpoint() {
      return this.endpoint;
   }

   public void setEndpoint(ConnectorEndpoint var1) {
      if (var1 != this.endpoint) {
         this.endpoint = var1;
         this.clear();
      }
   }

   public ImmutableList<NameOption> getNameOptions() {
      return this.nameOptions;
   }

   public NameOption getNameOption() {
      return this.nameOption.get();
   }

   public void setNameOption(NameOption var1) {
      this.nameOption.set(var1);
   }

   public ImmutableList<ObjectTypeFilter> getTypeFilters() {
      return this.typeFilters;
   }

   public ObjectTypeFilter getSelectedType() {
      return this.typeFilter.get();
   }

   protected final void clear() {
      this.filterText.set("");
      this.filteredAndSortedObjects.set(new ArrayList<>());
      this.selectedObject.set(null);
   }

   public void setFilterText(String var1) {
      this.filterText.set(var1);
   }

   public ListProperty<ConnectorObject> getObjectsProperty() {
      return this.filteredAndSortedObjects;
   }

   public void setObjects(List<ConnectorObject> var1) {
      this.allObjects = ImmutableList.copyOf(var1);
      this.updateFilteredListOfObjects();
   }

   public Property<ConnectorObject> getSelectedObjectProperty() {
      return this.selectedObject;
   }

   public ConnectorObject getSelectedObject() {
      return this.selectedObject.get();
   }

   public ImmutableList<ConnectorObject> getSelectionPath() {
      return this.selectionPath;
   }

   public void setSelectionPath(Iterable<? extends ConnectorObject> var1) {
      this.clear();
      this.selectionPath = ImmutableList.copyOf(var1);
   }

   private void updateFilteredListOfObjects() {
      List var1 = this.createFilteredAndOrderedList();
      this.updateSelectedObject(var1);
   }

   private List<ConnectorObject> createFilteredAndOrderedList() {
      Predicate var1 = this.createFilter();
      Iterable var2 = Iterables.filter(this.allObjects, var1);
      List var3 = Ordering.from(String.CASE_INSENSITIVE_ORDER).onResultOf(this.nameOption.get()).sortedCopy(var2);
      this.filteredAndSortedObjects.set(var3);
      return var3;
   }

   private Predicate<ConnectorObject> createFilter() {
      Predicate var1 = this.typeFilter.get();
      String var2 = this.filterText.get().trim();
      if (var2.isEmpty()) {
         return var1;
      }

      ObjectSelectionModel.Filter var3 = new ObjectSelectionModel.Filter(var2, this.nameOption.get());
      return Predicates.and(var1, var3);
   }

   private void updateSelectedObject(List<ConnectorObject> var1) {
      ConnectorObject var2 = this.selectedObject.get();
      if (!var1.contains(var2)) {
         this.selectedObject.set(null);
      }
   }

   private static class Filter implements Predicate<ConnectorObject> {
      private final String filterText;
      private final NameOption nameOption;

      public Filter(String var1, NameOption var2) {
         assert !var1.isEmpty();
         this.filterText = var1.toLowerCase();
         this.nameOption = var2;
      }

      public boolean apply(ConnectorObject var1) {
         String var2 = this.nameOption.apply(var1).toLowerCase();
         return var2.contains(this.filterText);
      }
   }
}
