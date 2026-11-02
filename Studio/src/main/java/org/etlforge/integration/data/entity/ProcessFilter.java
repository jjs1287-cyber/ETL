package org.etlforge.integration.data.entity;

import com.google.common.base.Objects;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.integration.data.functors.EntityCollectionFilter;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;

public final class ProcessFilter implements EntityFilter {
   private final IntegrationProject project;
   private final IntegrationEntity process;
   private EntityCollectionFilter impl;
   private final List<ProcessFilterListener> listeners;

   public ProcessFilter(IntegrationEntity var1) {
      this.process = var1;
      this.project = ProjectUtils.getProject(var1);
      this.listeners = new CopyOnWriteArrayList<>();
      this.updateImpl();
   }

   public IntegrationEntity getProcess() {
      return this.process;
   }

   private boolean updateImpl() {
      EntityCollectionFilter var1 = this.impl;
      this.impl = new EntityCollectionFilter();
      this.impl.add(this.process);
      DependencyStore var2 = this.project.getCurrentDependencies();

      try {
         RequiredObjectsCollector var3 = new RequiredObjectsCollector(var2);
         this.impl.addAll(var3.collect(this.process));
      } finally {
         var2.dispose();
      }

      return !Objects.equal(var1, this.impl);
   }

   public void update() {
      boolean var1 = this.updateImpl();
      if (var1) {
         this.notifyListeners();
      }
   }

   private void notifyListeners() {
      for (ProcessFilterListener var2 : this.listeners) {
         var2.filterChanged(this);
      }
   }

   @Override
   public boolean apply(IntegrationEntity var1) {
      return this.impl.apply(var1);
   }

   public void addFilterListener(ProcessFilterListener var1) {
      this.listeners.add(var1);
   }

   public void removeFilterListener(ProcessFilterListener var1) {
      this.listeners.remove(var1);
   }

   public boolean hasMatchingType(Iterable<EntityType> var1) {
      return this.impl.hasMatchingType(var1);
   }

   public boolean containsWizards() {
      return this.impl.containsWizards();
   }

   public Set<EntityType> getAllTypes() {
      return this.impl.getAllTypes();
   }
}
