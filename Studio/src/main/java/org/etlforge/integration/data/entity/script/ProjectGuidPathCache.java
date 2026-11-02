package org.etlforge.integration.data.entity.script;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectPropertyChangeSupport;
import org.etlforge.integration.data.project.event.IntegrationProjectEvent;
import org.etlforge.integration.data.project.event.ProjectListener;
import org.etlforge.integration.data.script.GrammarUtils;
import org.etlforge.integration.data.script.ProjectItemArgDescriptor;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.system.Disposable;

public final class ProjectGuidPathCache implements GuidPathCache, Disposable {
   private final IntegrationProject project;
   private final ProjectPropertyChangeSupport changeSupport;
   private final ProjectGuidPathCache.Updater updater = new ProjectGuidPathCache.Updater();
   private final BiMap<ProjectGuidPathCache.Path, String> pathToGuid = HashBiMap.create();
   private final List<GuidPathCacheListener> listeners = Lists.newCopyOnWriteArrayList();

   public ProjectGuidPathCache(IntegrationProject var1, Iterable<ProjectItemArgDescriptor> var2) {
      this.project = var1;
      var1.addProjectListener(this.updater);
      this.changeSupport = new ProjectPropertyChangeSupport(var1, false);
      this.changeSupport.addPropertyChangeListener("Name", this.updater);

      for (ProjectItemArgDescriptor var4 : var2) {
         this.buildMap(var1, var4);
      }
   }

   private void buildMap(IntegrationProject var1, ProjectItemArgDescriptor var2) {
      String var3 = GrammarUtils.idToPrefix(var2.getIdClass());

      for (IntegrationEntity var5 : var1.getAllEntitiesOfClass(var2.getDataClass())) {
         this.pathToGuid.put(new ProjectGuidPathCache.Path(var5), var3 + var5.getID());
      }
   }

   @Override
   public void dispose() {
      this.project.removeProjectListener(this.updater);
      this.changeSupport.dispose();
   }

   @Override
   public String getGuid(String var1) {
      return this.pathToGuid.get(new ProjectGuidPathCache.Path(var1));
   }

   @Override
   public String getPathFromGuid(String var1) {
      ProjectGuidPathCache.Path var2 = this.pathToGuid.inverse().get(var1);
      return var2 == null ? null : var2.path;
   }

   @Override
   public void addGuidPathCacheListener(GuidPathCacheListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public void removeGuidPathCacheListener(GuidPathCacheListener var1) {
      this.listeners.remove(var1);
   }

   private static class Path {
      public final String path;

      public Path(String var1) {
         this.path = var1;
      }

      public Path(IntegrationEntity var1) {
         this.path = ScriptConstants.getProjectItemPath(var1);
      }

      @Override
      public int hashCode() {
         return this.path.toLowerCase().hashCode();
      }

      @Override
      public boolean equals(Object var1) {
         return var1 == this ? true : var1 instanceof ProjectGuidPathCache.Path && this.path.equalsIgnoreCase(((ProjectGuidPathCache.Path)var1).path);
      }

      @Override
      public String toString() {
         return this.path;
      }
   }

   private class Updater implements ProjectListener, PropertyChangeListener {
      private Map<IntegrationEntity, String> oldPathsForChangeNotification;

      private Updater() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         IntegrationEntity var2 = (IntegrationEntity)var1.getSource();
         this.pathsWillChange(Collections.singletonList(var2));
         this.changePaths();
         this.pathsHaveChanged();
      }

      private void pathsWillChange(List<IntegrationEntity> var1) {
         this.oldPathsForChangeNotification = Maps.newHashMap();

         for (IntegrationEntity var4 : this.collectItemsThePathsOfWhichAreAboutToChange(var1)) {
            String var5 = this.getGuid(var4);
            if (var5 != null) {
               this.oldPathsForChangeNotification.put(var4, ProjectGuidPathCache.this.getPathFromGuid(var5));
            }
         }
      }

      private Set<IntegrationEntity> collectItemsThePathsOfWhichAreAboutToChange(List<IntegrationEntity> var1) {
         HashSet var2 = Sets.newHashSet();

         for (IntegrationEntity var4 : var1) {
            if (var4 instanceof Folder) {
               Folder var5 = (Folder)var4;
               var2.addAll(var5.getAllItemsRecursively());
            } else {
               var2.add(var4);
            }
         }

         return var2;
      }

      private String getGuid(IntegrationEntity var1) {
         IntegrationEntityId var2 = var1.getID();
         String var3 = GrammarUtils.idToPrefix((Class<? extends IntegrationEntityId>)var2.getClass());
         return var3 != null ? var3 + var2 : null;
      }

      private void changePaths() {
         for (IntegrationEntity var2 : this.oldPathsForChangeNotification.keySet()) {
            String var3 = this.getGuid(var2);
            ProjectGuidPathCache.this.pathToGuid.inverse().put(var3, new ProjectGuidPathCache.Path(var2));
         }
      }

      private void pathsHaveChanged() {
         ArrayList var1 = Lists.newArrayList();

         for (Entry var3 : this.oldPathsForChangeNotification.entrySet()) {
            String var4 = this.getGuid((IntegrationEntity)var3.getKey());
            String var5 = ProjectGuidPathCache.this.getPathFromGuid(var4);
            var1.add(new GuidPathCacheListener.PathChange((IntegrationEntity)var3.getKey(), (String)var3.getValue(), var5));
         }

         this.oldPathsForChangeNotification = null;
         this.notifyListenersThatPathsHaveChanegd(var1);
      }

      private void notifyListenersThatPathsHaveChanegd(List<GuidPathCacheListener.PathChange> var1) {
         for (GuidPathCacheListener var3 : ProjectGuidPathCache.this.listeners) {
            var3.pathsHaveChanged(var1);
         }
      }

      @Override
      public void entitiesAdded(IntegrationProjectEvent var1) {
         this.handleInsertion(var1.getEntities());
      }

      private void handleInsertion(List<IntegrationEntity> var1) {
         for (IntegrationEntity var3 : var1) {
            String var4 = GrammarUtils.idToPrefix((Class<? extends IntegrationEntityId>)var3.getID().getClass());
            if (var4 != null) {
               ProjectGuidPathCache.this.pathToGuid.put(new ProjectGuidPathCache.Path(var3), var4 + var3.getID());
            }
         }
      }

      @Override
      public void entitiesRevived(IntegrationProjectEvent var1) {
         this.handleInsertion(var1.getEntities());
      }

      @Override
      public void entitiesDeleted(IntegrationProjectEvent var1) {
         for (IntegrationEntity var3 : var1.getEntities()) {
            String var4 = GrammarUtils.idToPrefix((Class<? extends IntegrationEntityId>)var3.getID().getClass());
            if (var4 != null) {
               ProjectGuidPathCache.Path var5 = new ProjectGuidPathCache.Path(var3);
               ProjectGuidPathCache.this.pathToGuid.remove(var5);
            }
         }
      }

      @Override
      public void entitiesMoved(IntegrationProjectEvent var1) {
         List var2 = var1.getEntities();
         this.pathsWillChange(var2);
         this.changePaths();
         this.pathsHaveChanged();
      }
   }
}

