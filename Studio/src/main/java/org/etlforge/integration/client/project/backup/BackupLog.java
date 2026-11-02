package org.etlforge.integration.client.project.backup;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.primitives.Longs;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.project.ProjectFile;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.persist.XmlPersistor;

final class BackupLog {
   private static final String FILE_NAME = "log.xml";
   private static final String ENTRY = "Entry";
   private static final String REFERENCE = "reference";
   private static final String TYPE = "type";
   private static final String TIME = "time";
   private static final String FOLDER = "folder";
   private final File logFile;
   private final EnumMap<BackupType, SortedSet<BackupLog.Entry>> backups = Maps.newEnumMap(BackupType.class);
   private final EnumMap<BackupType, BackupPruningPolicy> pruningPolicies = Maps.newEnumMap(BackupType.class);

   public BackupLog(File var1) {
      this.logFile = new File(var1, "log.xml");
      this.init();
      if (this.exists()) {
         this.load();
      }
   }

   public void setPruningPolicy(BackupType var1, BackupPruningPolicy var2) {
      Preconditions.checkNotNull(var1, "type");
      Preconditions.checkNotNull(var2, "policy");
      this.pruningPolicies.put(var1, var2);
   }

   private void init() {
      for (BackupType var4 : BackupType.values()) {
         this.backups.put(var4, new TreeSet<>());
         this.pruningPolicies.put(var4, BackupPruningPolicy.UNLIMITED);
      }
   }

   public boolean exists() {
      return this.logFile.exists();
   }

   public Date getDateOfLastBackupOfType(BackupType var1) {
      SortedSet var2 = this.backups.get(var1);
      return var2 != null && !var2.isEmpty() ? new Date(((BackupLog.Entry)var2.first()).time) : null;
   }

   public void addBackup(BackupFolder var1, BackupType var2) {
      if (this.pruningPolicies.get(var2).allowNewBackup()) {
         this.backups.get(var2).add(new BackupLog.Entry(var1.getLocation(), var2, var1.getTimestamp()));
         this.pruningPolicies.get(var2).prune(this.backups.get(var2));
         this.persist();
      }
   }

   public List<AvailableBackup> getAvailableBackups() {
      ArrayList var1 = new ArrayList();

      for (SortedSet var3 : this.backups.values()) {
         this.collectAvailableBackups(var3, var1);
      }

      return var1;
   }

   private void collectAvailableBackups(Set<BackupLog.Entry> var1, List<AvailableBackup> var2) {
      for (BackupLog.Entry var4 : var1) {
         AvailableBackup var5 = var4.asAvailableBackup();
         if (var5 != null) {
            var2.add(var5);
         }
      }
   }

   private void persist() {
      try {
         XmlPersistor var1 = XmlPersistor.newRoot("BackupLog");

         for (SortedSet var3 : this.backups.values()) {
            for (Object __etlItem96 : var3) {
               BackupLog.Entry var5 = (BackupLog.Entry) __etlItem96;
               this.writeEntry(var1, var5);
            }
         }

         var1.store(this.logFile);
      } catch (IOException var6) {
         ErrorLog.log(BackupLog.class, "Failed to update the project backup log file", var6);
      }
   }

   private void writeEntry(XmlPersistor var1, BackupLog.Entry var2) {
      Persistor var3 = var1.createChild("Entry");
      var3.putString("folder", var2.folder.getAbsolutePath());
      var3.putLong("time", var2.time);
      var3.putString("type", var2.type.name());
   }

   private void load() {
      try {
         XmlPersistor var1 = XmlPersistor.load(this.logFile);

         for (Persistor var3 : var1.getChildren("Entry")) {
            this.loadEntry(var3);
         }
      } catch (Exception var4) {
         ErrorLog.log(BackupLog.class, "Failed to load the project backup log file " + this.logFile.getAbsolutePath(), var4);
      }
   }

   private void loadEntry(Persistor var1) {
      String var2 = var1.getString("folder");
      if (var2 != null) {
         Long var3 = var1.getLong("time");
         if (var3 != null) {
            BackupType var4 = this.readType(var1);
            if (var4 != null) {
               BackupLog.Entry var5 = new BackupLog.Entry(new File(var2), var4, var3);
               this.backups.get(var4).add(var5);
            }
         }
      }
   }

   private BackupType readType(Persistor var1) {
      Boolean var2 = var1.getBoolean("reference");
      if (var2 != null) {
         return var2 ? BackupType.REFERENCE : BackupType.INTERMEDIATE;
      }

      String var3 = var1.getString("type");
      if (var3 != null) {
         try {
            return BackupType.valueOf(var3);
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

      return null;
   }

   public static final class Entry implements Comparable<BackupLog.Entry> {
      private final File folder;
      private final long time;
      private final BackupType type;

      public Entry(File var1, BackupType var2, long var3) {
         this.folder = var1;
         this.time = var3;
         this.type = var2;
      }

      public AvailableBackup asAvailableBackup() {
         String var1 = this.getProjectName();
         if (var1 != null && !var1.isEmpty()) {
            final ProjectLocation var2 = new ProjectLocation(var1, this.folder);
            return new AvailableBackup() {
               @Override
               public ProjectLocation getLocation() {
                  return var2;
               }

               @Override
               public Date getTimestamp() {
                  return new Date(Entry.this.time);
               }
            };
         } else {
            return null;
         }
      }

      public void delete() {
         try {
            File var1 = this.folder.getParentFile();
            FileUtils.deleteDirectory(this.folder);
            if (this.canDeleteParent(var1)) {
               FileUtils.deleteDirectory(var1);
            }
         } catch (IOException var2) {
            ErrorLog.log(BackupLog.class, "Failed to remove old intermediate project backup", var2);
         }
      }

      private boolean canDeleteParent(File var1) {
         if (var1 == null) {
            return false;
         }

         File[] var2 = var1.listFiles(org.etlforge.io.FileUtils.DIRECTORIES);
         return var2 != null && var2.length == 0;
      }

      public int compareTo(BackupLog.Entry var1) {
         return -1 * Longs.compare(this.time, var1.time);
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (!(var1 instanceof BackupLog.Entry)) {
            return false;
         }

         BackupLog.Entry var2 = (BackupLog.Entry)var1;
         return this.time == var2.time && this.folder.equals(var2.folder) && this.type == var2.type;
      }

      @Override
      public int hashCode() {
         int var1 = 17;
         var1 = 31 * var1 + Longs.hashCode(this.time);
         var1 = 31 * var1 + this.folder.hashCode();
         return 31 * var1 + this.type.hashCode();
      }

      private String getProjectName() {
         File[] var1 = this.folder.listFiles(ProjectFile.FILE_FILTER);
         return var1 != null && var1.length == 1 ? FilenameUtils.getBaseName(var1[0].getName()) : null;
      }
   }
}
