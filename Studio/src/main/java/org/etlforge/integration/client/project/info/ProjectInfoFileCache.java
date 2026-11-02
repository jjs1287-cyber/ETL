package org.etlforge.integration.client.project.info;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.appdir.CacheDirectory;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.id.FolderId;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.ScheduleId;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.io.FileUtils;
import org.etlforge.io.Folder;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.persist.XmlPersistor;
import org.xml.sax.SAXException;

public final class ProjectInfoFileCache implements ProjectInfoCache {
   private static final String OPERATIONS = "operations";
   private static final String SCHEDULES = "schedules";
   private static final String ID = "id";
   private static final String FOLDER = "folder";
   private static final String OPERATION = "operation";
   private static final String SCHEDULE = "schedule";
   private static final String SCHEDULE_ID = "schedule_id";
   private static final String SCHEDULE_ENABLED = "schedule_enabled";
   private static final String ENABLED = "enabled";
   private static final String NAME = "name";
   private final Object lock = new Object();
   private final Folder dir;
   private boolean valid;
   private final boolean dirExists;

   public ProjectInfoFileCache(CacheDirectory var1) {
      this(new Folder(new File(var1.getLocation(), ProjectInfoFileCache.class.getName())));
   }

   public ProjectInfoFileCache(CacheDirectory var1, String var2) {
      this(new Folder(new File(var1.getLocation(), var2)));
   }

   public ProjectInfoFileCache(Folder var1) {
      this.dir = var1;
      this.dirExists = var1.createOnDisk();
      this.valid = this.dirExists;
   }

   @Override
   public List<IntegrationProject> getProjects() {
      if (!this.dirExists) {
         return null;
      }

      synchronized (this.lock) {
         if (this.valid && !this.dir.isEmpty()) {
            File[] var2 = this.getProjectFiles();
            if (var2.length > 0) {
               return this.restoreProjectsFromFiles(var2);
            }
         }

         return null;
      }
   }

   private File[] getProjectFiles() {
      return this.dir.listFiles(new FileFilter() {
         @Override
         public boolean accept(File var1) {
            return var1.getName().toLowerCase().endsWith(".xml");
         }
      });
   }

   private List<IntegrationProject> restoreProjectsFromFiles(File[] var1) {
      try {
         ArrayList var2 = Lists.newArrayListWithExpectedSize(var1.length);

         for (File var6 : var1) {
            ProjectInfoFileCache.ProjectRestorer var7 = new ProjectInfoFileCache.ProjectRestorer(var6);
            var2.add(var7.restoreProject());
         }

         return var2;
      } catch (Exception var8) {
         this.clear();
         ErrorLog.internal(this.getClass(), "Failed to store a local cache of project information downloaded from a server", var8);
         return null;
      }
   }

   @Override
   public void storeProjects(List<IntegrationProject> var1) {
      if (this.dirExists) {
         synchronized (this.lock) {
            this.clear();
            if (this.valid) {
               this.writeProjectInfoToDisk(var1);
            }
         }
      }
   }

   private void writeProjectInfoToDisk(List<IntegrationProject> var1) {
      try {
         ProjectInfoFileCache.ProjectSaver var2 = new ProjectInfoFileCache.ProjectSaver(this.dir);

         for (IntegrationProject var4 : var1) {
            var2.storeProject(var4);
         }
      } catch (IOException var5) {
         this.clear();
         ErrorLog.internal(this.getClass(), "Failed to store a local cache of project information downloaded from a server", var5);
      }
   }

   @Override
   public void clear() {
      if (this.dirExists) {
         synchronized (this.lock) {
            this.dir.deleteFiles(FileUtils.ACCEPT_ALL);
            this.valid = this.dir.isEmpty();
         }
      }
   }

   private static class ProjectRestorer {
      private final File file;

      public ProjectRestorer(File var1) {
         this.file = var1;
      }

      public IntegrationProject restoreProject() throws SAXException, IOException {
         XmlPersistor var1 = XmlPersistor.load(this.file);
         IntegrationProject var2 = this.createProjectShell(var1);
         this.restoreType(var2, EntityType.Operation, var1.getFirstChild("operations"));
         this.restoreType(var2, EntityType.Schedule, var1.getFirstChild("schedules"));
         return var2;
      }

      private IntegrationProject createProjectShell(XmlPersistor var1) {
         String var2 = restoreName(var1);
         IntegrationProjectId var3 = restoreId(var1, IntegrationProjectId.class);
         return IntegrationProject.createExistingProjectShell(var2, var3, ImmutableList.of(EntityType.Operation, EntityType.Schedule));
      }

      private static String restoreName(Persistor var0) {
         return var0.getString("name");
      }

      private static <T extends IntegrationEntityId> T restoreId(Persistor var0, Class<T> var1) {
         try {
            String var2 = var0.getString("id");
            Constructor var3 = var1.getConstructor(String.class);
            return (T)var3.newInstance(var2);
         } catch (Exception var4) {
            throw Throwables.propagate(var4);
         }
      }

      private void restoreType(IntegrationProject var1, EntityType var2, Persistor var3) {
         RootFolder var4 = var1.getRootFolder(var2);
         this.populateFolder(var4, var3, var2);
      }

      private void populateFolder(org.etlforge.integration.data.entity.Folder var1, Persistor var2, EntityType var3) {
         assert var3 == EntityType.Operation || var3 == EntityType.Schedule;

         for (Persistor var5 : var2.getChildren("folder")) {
            this.restoreFolder(var1, var3, var5);
         }

         if (var3 == EntityType.Operation) {
            for (Persistor var8 : var2.getChildren("operation")) {
               this.restoreOperation(var8, var1);
            }
         } else {
            for (Persistor var9 : var2.getChildren("schedule")) {
               this.restoreSchedule(var9, var1);
            }
         }
      }

      private void restoreFolder(org.etlforge.integration.data.entity.Folder var1, EntityType var2, Persistor var3) {
         String var4 = restoreName(var3);
         FolderId var5 = restoreId(var3, FolderId.class);
         org.etlforge.integration.data.entity.Folder var6 = new org.etlforge.integration.data.entity.Folder(var5, var4, var2);
         var1.addItem(var6);
         this.populateFolder(var6, var3, var2);
      }

      private void restoreOperation(Persistor var1, org.etlforge.integration.data.entity.Folder var2) {
         String var3 = restoreName(var1);
         OperationId var4 = restoreId(var1, OperationId.class);
         Operation var5 = new Operation(var4, var3);
         String var6 = var1.getString("schedule_id");
         if (var6 != null) {
            var5.setScheduleId(new ScheduleId(var6));
            var5.setScheduleEnabled(var1.getBoolean("schedule_enabled"));
         }

         var2.addItem(var5);
      }

      private void restoreSchedule(Persistor var1, org.etlforge.integration.data.entity.Folder var2) {
         String var3 = restoreName(var1);
         ScheduleId var4 = restoreId(var1, ScheduleId.class);
         Schedule var5 = new Schedule(var4, var3);
         var5.setEnabled(var1.getBoolean("enabled"));
         var2.addItem(var5);
      }
   }

   private static class ProjectSaver {
      private final Folder dir;

      public ProjectSaver(Folder var1) {
         this.dir = var1;
      }

      public void storeProject(IntegrationProject var1) throws IOException {
         XmlPersistor var2 = this.persistProject(var1);
         var2.store(this.dir.getFile(var1.getID() + ".xml"));
      }

      private XmlPersistor persistProject(IntegrationProject var1) {
         XmlPersistor var2 = XmlPersistor.newRoot("project");
         storeNameAndId(var1, var2);
         Persistor var3 = var2.createChild("operations");
         this.addItems(var1, var3, EntityType.Operation);
         Persistor var4 = var2.createChild("schedules");
         this.addItems(var1, var4, EntityType.Schedule);
         return var2;
      }

      private static void storeNameAndId(IntegrationEntity var0, Persistor var1) {
         var1.putString("id", var0.getID().toString());
         var1.putString("name", var0.getName());
      }

      private void addItems(IntegrationProject var1, Persistor var2, EntityType var3) {
         assert var3 == EntityType.Schedule || var3 == EntityType.Operation;
         RootFolder var4 = var1.getRootFolder(var3);
         this.processFolder(var2, var4, var3);
      }

      private void processFolder(Persistor var1, org.etlforge.integration.data.entity.Folder var2, EntityType var3) {
         assert var3 == EntityType.Schedule || var3 == EntityType.Operation;

         for (IntegrationEntity var5 : var2.getAllChildren()) {
            if (var5 instanceof org.etlforge.integration.data.entity.Folder) {
               Persistor var6 = var1.createChild("folder");
               storeNameAndId(var2, var6);
               this.processFolder(var6, (org.etlforge.integration.data.entity.Folder)var5, var3);
            } else if (var3 == EntityType.Operation) {
               this.storeOperation((Operation)var5, var1.createChild("operation"));
            } else {
               this.storeSchedule((Schedule)var5, var1.createChild("schedule"));
            }
         }
      }

      private void storeOperation(Operation var1, Persistor var2) {
         storeNameAndId(var1, var2);
         ScheduleId var3 = var1.getScheduleId();
         if (var3 != null) {
            var2.putString("schedule_id", var3.toString());
            var2.putBoolean("schedule_enabled", var1.isScheduleEnabled());
         }
      }

      private void storeSchedule(Schedule var1, Persistor var2) {
         storeNameAndId(var1, var2);
         var2.putBoolean("enabled", var1.isEnabled());
      }
   }
}
