package org.etlforge.integration.client.ui.interchange.component.tree;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.etlforge.integration.client.ui.interchange.content.ContentViewerCategory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.ProcessFilter;
import org.etlforge.integration.data.entity.ProcessFilterListener;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectPropertyChangeSupport;
import org.etlforge.integration.data.project.event.IntegrationProjectEvent;
import org.etlforge.integration.data.project.event.ProjectListener;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.tree.KongaTreeFilter;

final class MultiTreeFilterService {
   private final ProjectSection projectSection;
   private final ImmutableList<MultiTreeSection> categories;
   private final MultiTreeContentViewer contentViewer;
   private final MultiTreeNameFilterField filterField;
   private MultiTreeFilterService.FilterRefresher filterRefresher;
   private MultiTreeFilter currentFilter;
   private MultiTreeFilterService.ProcessFilterManager processFilterManager;
   private Set<EntityType> visibleTypes = EntityType.allTypes();

   public MultiTreeFilterService(MultiTreeContentViewer var1, ProjectSection var2, Iterable<MultiTreeSection> var3) {
      this.contentViewer = var1;
      this.projectSection = var2;
      this.categories = ImmutableList.copyOf(var3);
      this.filterField = new MultiTreeNameFilterField(new Receiver<MultiTreeNameFilter>() {
         public void handle(MultiTreeNameFilter var1) {
            MultiTreeFilterService.this.applyNewNameFilter(var1);
         }
      });
      this.filterField.addFocusListener(new FocusAdapter() {
         @Override
         public void focusGained(FocusEvent var1) {
            MultiTreeFilterService.this.contentViewer.inactivateOtherSections(null);
         }
      });
   }

   private void applyNewNameFilter(MultiTreeNameFilter var1) {
      if (this.currentFilter instanceof CompoundMultiTreeFilter) {
         CompoundMultiTreeFilter var2 = (CompoundMultiTreeFilter)this.currentFilter;
         var2.setNameFilter(var1);
         this.applyFilter(var2);
      } else {
         this.applyFilter(var1);
      }
   }

   private void applyFilter(MultiTreeFilter var1) {
      assert EventQueue.isDispatchThread();
      Set var2 = this.visibleTypes;
      this.visibleTypes = var1.getAllTypes();
      this.currentFilter = var1;
      if (!Objects.equal(var2, this.visibleTypes)) {
         this.contentViewer.rebuildLayout();
      }

      for (MultiTreeSection var4 : this.categories) {
         this.applyNewFilterToSection(var1, var4);
      }

      this.projectSection.setExpansionMode(this.getExpansionMode());
      if (var1.isEmpty()) {
         this.disposeFilterRefresher();
      } else if (this.filterRefresher == null) {
         this.filterRefresher = new MultiTreeFilterService.FilterRefresher(this.contentViewer.getProject());
      }
   }

   private void applyNewFilterToSection(MultiTreeFilter var1, MultiTreeSection var2) {
      KongaTreeFilter var3 = null;
      ContentViewerCategory var4 = var2.getCategory();
      if (var1.hasMatchingType(var4)) {
         if (!var1.isEmpty()) {
            var2.expand();
         }

         var3 = var1.getTopLevelFilter(var4);
      } else {
         var2.collapse();
      }

      var2.setTreeFilter(var3);
   }

   public void setProject(IntegrationProject var1) {
      this.filterField.setProject(var1);
   }

   public MultiTreeNameFilterField getFilterField() {
      return this.filterField;
   }

   public List<MultiTreeSection> getVisibleSections() {
      if (this.currentFilter != null && !this.currentFilter.isEmpty()) {
         ArrayList var1 = Lists.newArrayList();

         for (MultiTreeSection var3 : this.categories) {
            if (var3.isAcceptedByFilter(this.currentFilter)) {
               var1.add(var3);
            }
         }

         return var1;
      } else {
         return this.categories;
      }
   }

   ExpansionMode getExpansionMode() {
      for (MultiTreeSection var2 : this.getVisibleSections()) {
         if (var2.isExpanded()) {
            return ExpansionMode.COLLAPSE;
         }
      }

      return ExpansionMode.EXPAND;
   }

   public void disposeFilterRefresher() {
      if (this.filterRefresher != null) {
         this.filterRefresher.dispose();
         this.filterRefresher = null;
      }
   }

   public void setProcessFilter(ProcessFilter var1) {
      if (this.processFilterManager != null) {
         this.processFilterManager.dispose();
         this.processFilterManager = null;
      }

      MultiTreeFilter var2 = null;
      MultiTreeNameFilter var3 = this.filterField.getFilter();
      if (var1 == null) {
         var2 = var3;
      } else {
         ContentViewerProcessFilter var4 = new ContentViewerProcessFilter(var1);
         this.processFilterManager = new MultiTreeFilterService.ProcessFilterManager(this.contentViewer.getProject(), var4);
         var2 = new CompoundMultiTreeFilter(var4, var3);
      }

      this.applyFilter(var2);
   }

   private final class FilterRefresher implements ProjectListener, PropertyChangeListener, Disposable {
      private final IntegrationProject project;
      private final ProjectPropertyChangeSupport propChangeDispatcher;

      public FilterRefresher(IntegrationProject var2) {
         this.project = var2;
         this.propChangeDispatcher = new ProjectPropertyChangeSupport(var2, false);
         this.propChangeDispatcher.addPropertyChangeListener("Name", this);
         var2.addProjectListener(this);
      }

      @Override
      public void dispose() {
         this.project.removeProjectListener(this);
         this.propChangeDispatcher.dispose();
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         this.refreshFilter();
      }

      private void refreshFilter() {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               MultiTreeNameFilter var1 = MultiTreeFilterService.this.filterField.getFilter();
               MultiTreeFilterService.this.applyNewNameFilter(var1);
            }
         });
      }

      @Override
      public void entitiesAdded(IntegrationProjectEvent var1) {
         this.refreshFilter();
      }

      @Override
      public void entitiesRevived(IntegrationProjectEvent var1) {
         this.refreshFilter();
      }

      @Override
      public void entitiesDeleted(IntegrationProjectEvent var1) {
         this.refreshFilter();
      }

      @Override
      public void entitiesMoved(IntegrationProjectEvent var1) {
         this.refreshFilter();
      }
   }

   private final class ProcessFilterManager extends ProjectListener.Adapter implements ProcessFilterListener, Disposable {
      private final IntegrationProject project;
      private final ContentViewerProcessFilter processFilter;

      public ProcessFilterManager(IntegrationProject var2, ContentViewerProcessFilter var3) {
         this.project = var2;
         this.processFilter = var3;
         var3.addProcessFilterListener(this);
         var2.addProjectListener(this);
      }

      @Override
      public void filterChanged(ProcessFilter var1) {
         MultiTreeFilterService.this.applyFilter(MultiTreeFilterService.this.currentFilter);
      }

      @Override
      public void dispose() {
         this.project.removeProjectListener(this);
      }

      @Override
      public void entitiesAdded(IntegrationProjectEvent var1) {
         if (this.processFilter != null) {
            this.processFilter.addAdditionalItems(var1.getEntities());
            MultiTreeFilterService.this.applyFilter(MultiTreeFilterService.this.currentFilter);
         }
      }
   }
}
