package org.etlforge.integration.data.script.de;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.ProcessFilter;
import org.etlforge.integration.data.entity.ProcessFilterListener;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.script.ScriptVariables;
import org.etlforge.integration.data.entity.transformation.TransformationVariables;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectPropertyChangeSupport;
import org.etlforge.integration.data.project.event.IntegrationProjectEvent;
import org.etlforge.integration.data.project.event.ProjectListener;
import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.data.script.ExpressionSource;
import org.etlforge.integration.dataelement.GlobalDE;

public final class DefaultGlobalDataElements implements GlobalDataElements {
   private IntegrationProject project;
   private DefaultGlobalDataElements.ProjectListenerImpl projectListener;
   private ProjectPropertyChangeSupport updater;
   private final SetMultimap<ExpressionSource, DataElementReference> references = HashMultimap.create();
   private final Object lock = new Object();
   private final DefaultGlobalDataElements.Listeners listeners = new DefaultGlobalDataElements.Listeners();
   private ProcessFilter processFilter;
   private ProcessFilterListener processFilterListener;

   @Override
   public void scanProject(IntegrationProject var1) {
      synchronized (this.lock) {
         if (var1 != this.project) {
            if (this.updater != null) {
               this.project.removeProjectListener(this.projectListener);
               this.updater.dispose();
            }

            this.updater = new ProjectPropertyChangeSupport(var1, false);
            this.updater.addPropertyChangeListener("projectvariable_name", event -> this.listeners.fireRebuilt());
            this.updater.addPropertyChangeListener("Name", event -> { if (event.getSource() instanceof org.etlforge.integration.data.entity.ProjectVariable) this.listeners.fireRebuilt(); });
            this.updater.addPropertyChangeListener("default_value", event -> this.listeners.fireRebuilt());
            this.updater.addPropertyChangeListener("script", new DefaultGlobalDataElements.ScriptUpdater());
            this.updater.addPropertyChangeListener("Transformation#Mappings", new DefaultGlobalDataElements.TransformationUpdater());
            var1.addProjectListener(this.projectListener = new DefaultGlobalDataElements.ProjectListenerImpl());
            this.project = var1;
         }

         this.references.clear();
         this.listeners.setBlocked(true);

         for (Script var4 : var1.getAllEntitiesOfClass(Script.class)) {
            this.scanScript(var4);
         }

         for (Transformation var8 : var1.getAllEntitiesOfClass(Transformation.class)) {
            this.scanTransformation(var8);
         }

         this.listeners.setBlocked(false);
      }

      this.listeners.fireRebuilt();
   }

   private void scanScript(Script var1) {
      ScriptVariables var2 = new ScriptVariables(this);
      var2.scanScript(var1);
   }

   private void scanTransformation(Transformation var1) {
      TransformationVariables var2 = new TransformationVariables(this);
      var2.scanTransformation(var1);
   }

   @Override
   public void parseExpression(ExpressionSource var1, String var2) {
      ExpressionParser var3 = new ExpressionParser(var1);
      this.parseExpression(var1, var2, var3);
   }

   private void parseExpression(ExpressionSource var1, String var2, ExpressionParser var3) {
      ImmutableSet var4 = var3.extractDataElementReferences(var2);
      if (var4 != null) {
         this.updateReferences(var1, var4);
      }
   }

   @Override
   public void remove(ExpressionSource var1) {
      boolean var2 = false;
      synchronized (this.lock) {
         Set var4 = this.references.removeAll(var1);
         var2 = !var4.isEmpty();
      }

      if (var2) {
         this.listeners.fireChange(var1);
      }
   }

   @Override
   public void updateReferences(ExpressionSource var1, Set<DataElementReference> var2) {
      Preconditions.checkNotNull(var1, "source");
      Preconditions.checkNotNull(var2, "newRefs");
      synchronized (this.lock) {
         Set var4 = this.references.get(var1);
         if (Objects.equal(var2, var4)) {
            return;
         }

         this.references.replaceValues(var1, var2);
      }

      this.listeners.fireChange(var1);
   }

   @Override
   public SortedSet<String> getAllDataElements() {
      TreeSet var1 = Sets.newTreeSet(String.CASE_INSENSITIVE_ORDER);
      this.collect(new DefaultGlobalDataElements.DeCollector(), var1);
      synchronized (this.lock) {
         if (this.project != null) for (org.etlforge.integration.data.entity.ProjectVariable variable :
               this.project.getAllEntitiesOfClass(org.etlforge.integration.data.entity.ProjectVariable.class)) var1.add(variable.getName());
      }
      return var1;
   }

   @Override
   public SortedSet<String> getAllDataElementsWithPrefix() {
      TreeSet var1 = Sets.newTreeSet();

      for (String var3 : this.getAllDataElements()) {
         var1.add(GlobalDE.forScript(var3));
      }

      return var1;
   }

   public SortedSet<String> getDataElements(ExpressionSource var1) {
      TreeSet var2 = Sets.newTreeSet(String.CASE_INSENSITIVE_ORDER);
      synchronized (this.lock) {
         for (DataElementReference var6 : this.references.get(var1)) {
            var2.add(var6.getDataElement());
         }

         return var2;
      }
   }

   @Override
   public Set<DataElementReference> getReferences(String var1) {
      HashSet var2 = Sets.newHashSet();
      this.collect(new DefaultGlobalDataElements.NameQuery(var1), var2);
      return var2;
   }

   @Override
   public void addListener(GlobalDataElementsListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public void removeListener(GlobalDataElementsListener var1) {
      this.listeners.remove(var1);
   }

   @Override
   public void addListener(ExpressionSource var1, GlobalDataElementsListener var2) {
      this.listeners.add(var1, var2);
   }

   @Override
   public void removeListener(ExpressionSource var1, GlobalDataElementsListener var2) {
      this.listeners.remove(var1, var2);
   }

   private <T> void collect(DefaultGlobalDataElements.ReferenceQuery<T> var1, Set<T> var2) {
      synchronized (this.lock) {
         for (DataElementReference var5 : this.references.values()) {
            this.collectDataElementFromReference(var5, var1, var2);
         }
      }
   }

   private <T> void collectDataElementFromReference(DataElementReference var1, DefaultGlobalDataElements.ReferenceQuery<T> var2, Set<T> var3) {
      if (this.applyProcessFilter(var1)) {
         T var4 = var2.apply(var1);
         if (var4 != null) {
            var3.add(var4);
         }
      }
   }

   private boolean applyProcessFilter(DataElementReference var1) {
      return this.processFilter != null ? var1.getSource().matches(this.processFilter) : true;
   }

   @Override
   public void setProcessFilter(ProcessFilter var1) {
      if (var1 != this.processFilter) {
         if (this.processFilter != null) {
            this.processFilter.removeFilterListener(this.processFilterListener);
         }

         if (var1 != null) {
            if (this.processFilterListener == null) {
               this.processFilterListener = new DefaultGlobalDataElements.ProcessFilterListenerImpl();
            }

            var1.addFilterListener(this.processFilterListener);
         }

         this.processFilter = var1;
         this.listeners.fireRebuilt();
         this.listeners.fireNewProcessFilter(var1);
      }
   }

   @Override
   public ProcessFilter getProcessFilter() {
      return this.processFilter;
   }

   private static class DeCollector implements DefaultGlobalDataElements.ReferenceQuery<String> {
      private DeCollector() {
      }

      public String apply(DataElementReference var1) {
         return var1.getDataElement();
      }
   }

   private static class Listeners {
      private final List<GlobalDataElementsListener> global = new CopyOnWriteArrayList<>();
      private final Map<ExpressionSource, List<GlobalDataElementsListener>> individual = Maps.newHashMap();
      private boolean blocked;

      public Listeners() {
      }

      public void setBlocked(boolean var1) {
         this.blocked = var1;
      }

      public void add(GlobalDataElementsListener var1) {
         this.global.add(var1);
      }

      public void remove(GlobalDataElementsListener var1) {
         this.global.remove(var1);
      }

      public void add(ExpressionSource var1, GlobalDataElementsListener var2) {
         synchronized (this.individual) {
            List var4 = this.individual.get(var1);
            if (var4 == null) {
               var4 = new CopyOnWriteArrayList();
               this.individual.put(var1, var4);
            }

            var4.add(var2);
         }
      }

      public void remove(ExpressionSource var1, GlobalDataElementsListener var2) {
         synchronized (this.individual) {
            List var4 = this.individual.get(var1);
            if (var4 != null) {
               var4.remove(var2);
               if (var4.isEmpty()) {
                  this.individual.remove(var1);
               }
            }
         }
      }

      public void fireChange(ExpressionSource var1) {
         if (!this.blocked) {
            ArrayList var2 = Lists.newArrayList(this.global);
            synchronized (this.individual) {
               List var4 = this.individual.get(var1);
               if (var4 != null) {
                  var2.addAll(var4);
               }
            }

            for (Object __etlItem302 : var2) {
               GlobalDataElementsListener var8 = (GlobalDataElementsListener) __etlItem302;
               var8.dataElementStoreChanged();
            }
         }
      }

      public void fireRebuilt() {
         ArrayList var1 = Lists.newArrayList(this.global);
         synchronized (this.individual) {
            for (List var4 : this.individual.values()) {
               var1.addAll(var4);
            }
         }

         for (Object __etlItem316 : var1) {
            GlobalDataElementsListener var8 = (GlobalDataElementsListener) __etlItem316;
            var8.dataElementStoreRebuilt();
         }
      }

      public void fireNewProcessFilter(ProcessFilter var1) {
         for (GlobalDataElementsListener var3 : this.global) {
            var3.processFilterChanged(var1);
         }
      }
   }

   private static class NameQuery implements DefaultGlobalDataElements.ReferenceQuery<DataElementReference> {
      private final String name;

      public NameQuery(String var1) {
         this.name = var1;
      }

      public DataElementReference apply(DataElementReference var1) {
         return var1.getDataElement().equalsIgnoreCase(this.name) ? var1 : null;
      }
   }

   private class ProcessFilterListenerImpl implements ProcessFilterListener {
      private ProcessFilterListenerImpl() {
      }

      @Override
      public void filterChanged(ProcessFilter var1) {
         DefaultGlobalDataElements.this.listeners.fireRebuilt();
      }
   }

   private class ProjectListenerImpl implements ProjectListener {
      private ProjectListenerImpl() {
      }

      @Override
      public void entitiesDeleted(IntegrationProjectEvent var1) { if (var1.filter(org.etlforge.integration.data.entity.ProjectVariable.class).iterator().hasNext()) DefaultGlobalDataElements.this.listeners.fireRebuilt();
         for (IntegrationEntity var3 : var1.filter(EntityFilters.oneOfTypes(EntityType.Transformation, EntityType.Script))) {
            this.remove(var3);
         }
      }

      private void remove(final IntegrationEntity var1) {
         final HashSet var2 = Sets.newHashSet();
         synchronized (DefaultGlobalDataElements.this.lock) {
            Iterables.removeIf(DefaultGlobalDataElements.this.references.keySet(), new Predicate<ExpressionSource>() {
               public boolean apply(ExpressionSource var1x) {
                  return var1x.originatesIn(var1) && var2.add(var1x);
               }
            });
         }

         for (Object __etlItem371 : var2) {
            ExpressionSource var4 = (ExpressionSource) __etlItem371;
            DefaultGlobalDataElements.this.listeners.fireChange(var4);
         }
      }

      @Override
      public void entitiesAdded(IntegrationProjectEvent var1) {
         this.scanAll(var1);
      }

      @Override
      public void entitiesRevived(IntegrationProjectEvent var1) {
         this.scanAll(var1);
      }

      private void scanAll(IntegrationProjectEvent var1) {
         if (var1.filter(org.etlforge.integration.data.entity.ProjectVariable.class).iterator().hasNext()) DefaultGlobalDataElements.this.listeners.fireRebuilt();
         for (Transformation var3 : var1.filter(Transformation.class)) {
            DefaultGlobalDataElements.this.scanTransformation(var3);
         }

         for (Script var5 : var1.filter(Script.class)) {
            DefaultGlobalDataElements.this.scanScript(var5);
         }
      }

      @Override
      public void entitiesMoved(IntegrationProjectEvent var1) {
      }
   }

   private interface ReferenceQuery<T> extends Function<DataElementReference, T> {
   }

   private class ScriptUpdater implements PropertyChangeListener {
      private ScriptUpdater() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         DefaultGlobalDataElements.this.scanScript((Script)var1.getSource());
      }
   }

   private class TransformationUpdater implements PropertyChangeListener {
      private TransformationUpdater() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         DefaultGlobalDataElements.this.scanTransformation((Transformation)var1.getSource());
      }
   }
}
