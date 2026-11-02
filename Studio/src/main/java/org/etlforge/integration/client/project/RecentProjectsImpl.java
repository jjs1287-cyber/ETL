package org.etlforge.integration.client.project;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.project.event.RecentProjectsListener;
import org.etlforge.integration.client.project.prefs.ProjectHistorySizePreference;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.persist.XmlPersistor;
import org.etlforge.util.preference.KongaPreferences;

final class RecentProjectsImpl implements RecentProjects {
   private static final String RECENT_KEY = "RecentProject";
   private static final String NAME_KEY = ".name.";
   private static final String LOCATION_KEY = ".location.";
   private static final int HISTORY_SIZE = getHistorySize();
   private final File storage;
   private final KongaPreferences preferences = KongaPreferences.getPreferences(RecentProjects.class);
   private LinkedHashSet<ProjectLocation> recentProjects;
   private final List<RecentProjectsListener> listeners = Lists.newCopyOnWriteArrayList();
   private final Object lock = new Object();
   private final ExecutorService preferencesWriterThread = Executors.newSingleThreadExecutor();

   public RecentProjectsImpl(File var1) {
      this.storage = Preconditions.checkNotNull(var1, "storage");
      this.recentProjects = this.readRecentProjectsFromStorage();
   }

   private LinkedHashSet<ProjectLocation> readRecentProjectsFromStorage() {
      return this.storage.exists() ? this.getRecentProjectsFromFile() : this.getRecentProjectsFromPreferences();
   }

   private LinkedHashSet<ProjectLocation> getRecentProjectsFromFile() {
      LinkedHashSet var1 = new LinkedHashSet();

      try {
         XmlPersistor var2 = XmlPersistor.load(this.storage);

         for (Persistor var4 : var2.getChildren("project")) {
            String var5 = var4.getString("name");
            if (!Strings.isNullOrEmpty(var5)) {
               String var6 = var4.getString("path");
               File var7 = new File(var6, var5 + ".jip");
               if (var7.exists()) {
                  var1.add(new ProjectLocation(var5, new File(var6)));
               }
            }
         }
      } catch (Exception var8) {
         ErrorLog.log(this.getClass(), "Failed to read the list of recent projects.", var8);
      }

      return var1;
   }

   private LinkedHashSet<ProjectLocation> getRecentProjectsFromPreferences() {
      LinkedHashSet var1 = new LinkedHashSet();

      for (int var2 = 0; var2 < HISTORY_SIZE; var2++) {
         String var3 = this.preferences.getString(getRecentProjectNameKey(var2), null);
         if (var3 != null && var3.length() > 0) {
            File var4 = this.preferences.getFile(getRecentProjectLocationKey(var2), null);
            File var5 = new File(var4, var3 + ".jip");
            if (var5.exists()) {
               var1.add(new ProjectLocation(var3, var4));
            }
         }
      }

      return new LinkedHashSet<>(var1);
   }

   @Override
   public int getNumberOfRecentProjects() {
      synchronized (this.lock) {
         return this.recentProjects.size();
      }
   }

   @Override
   public void addProjectLocation(ProjectLocation var1) {
      Preconditions.checkNotNull(var1, "loc");
      synchronized (this.lock) {
         if (Objects.equal(var1, this.getFirstProject())) {
            return;
         }

         LinkedHashSet var3 = new LinkedHashSet();
         var3.add(var1);

         for (ProjectLocation var5 : this.recentProjects) {
            if (!var3.contains(var5)) {
               var3.add(var5);
            }
         }

         this.recentProjects = var3;
         this.putRecentProjectsInPreferences();
      }

      this.fireChange();
   }

   @Override
   public void removeProjectLocation(ProjectLocation var1) {
      if (var1 != null) {
         boolean var2 = false;
         synchronized (this.lock) {
            if (this.recentProjects.contains(var1)) {
               var2 = true;
               this.recentProjects.remove(var1);
               this.putRecentProjectsInPreferences();
            }
         }

         if (var2) {
            this.fireChange();
         }
      }
   }

   @Override
   public void projectWasRenamed(String var1, File var2, String var3) {
      boolean var4 = false;
      synchronized (this.lock) {
         for (int var6 = 0; var6 < HISTORY_SIZE; var6++) {
            String var7 = getRecentProjectNameKey(var6);
            String var8 = this.preferences.getString(var7, null);
            if (var1.equals(var8)) {
               String var9 = getRecentProjectLocationKey(var6);
               File var10 = this.preferences.getFile(var9, null);
               if (var2.equals(var10)) {
                  this.preferences.putString(var7, var3);
                  this.preferences.putFile(var9, var10);
               }

               var4 = true;
               break;
            }
         }
      }

      if (var4) {
         this.fireChange();
      }
   }

   @Override
   public List<ProjectLocation> getExistingLocations() {
      ArrayList var1;
      synchronized (this.lock) {
         var1 = Lists.newArrayList(this.recentProjects);
      }

      Iterables.removeIf(var1, new Predicate<ProjectLocation>() {
         public boolean apply(ProjectLocation var1) {
            return !var1.getLocation().exists();
         }
      });
      return var1;
   }

   @Override
   public void addRecentProjectsListener(RecentProjectsListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public void removeRecentProjectsListener(RecentProjectsListener var1) {
      this.listeners.remove(var1);
   }

   private ProjectLocation getFirstProject() {
      return this.recentProjects.isEmpty() ? null : this.recentProjects.iterator().next();
   }

   private void fireChange() {
      for (RecentProjectsListener var2 : this.listeners) {
         var2.recentProjectsChanged(this);
      }
   }

   private void putRecentProjectsInPreferences() {
      this.preferencesWriterThread.execute(new RecentProjectsImpl.StorageWriter(this.storage, this.recentProjects));
   }

   private static String getRecentProjectNameKey(int var0) {
      return "RecentProject.name." + var0;
   }

   private static String getRecentProjectLocationKey(int var0) {
      return "RecentProject.location." + var0;
   }

   private static int getHistorySize() {
      return ProjectHistorySizePreference.get();
   }

   private static final class StorageWriter implements Runnable {
      private final File storage;
      private final ImmutableList<ProjectLocation> recentProjects;

      public StorageWriter(File var1, Collection<ProjectLocation> var2) {
         this.storage = var1;
         this.recentProjects = ImmutableList.copyOf(var2);
      }

      @Override
      public void run() {
         XmlPersistor var1 = XmlPersistor.newRoot("recent_projects");
         int var2 = 0;

         for (ProjectLocation var4 : this.recentProjects) {
            Persistor var5 = var1.createChild("project");
            var5.putString("name", var4.getProjectName());
            var5.putString("path", var4.getLocationPath());
            if (++var2 == RecentProjectsImpl.HISTORY_SIZE) {
               break;
            }
         }

         try {
            var1.store(this.storage);
         } catch (IOException var6) {
            ErrorLog.log(RecentProjectsImpl.class, "Failed to update the list of recent projects.", var6);
         }
      }
   }
}
