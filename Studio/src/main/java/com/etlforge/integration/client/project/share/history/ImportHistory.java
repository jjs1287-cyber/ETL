package com.etlforge.integration.client.project.share.history;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectFolders;
import org.etlforge.io.Folder;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.persist.XmlPersistor;

public final class ImportHistory {
   private static final String FILE_NAME = "ImportHistoryEntries.xml";
   private static final Map<String, Object> locks = Maps.newHashMap();
   private final ManagedProject project;
   private final Folder root;
   private final List<ImportHistoryEntry> entries;

   public ImportHistory(ManagedProject var1) {
      this.project = var1;
      this.root = ProjectFolders.getPersistFolder(var1.getLocation()).subFolder("ImportHistory");
      this.entries = Lists.newArrayList();
      this.read();
   }

   private void read() {
      ImportHistory.Reader var1 = new ImportHistory.Reader();
      var1.read();
   }

   public List<ImportHistoryEntry> getEntries() {
      synchronized (this.getLock()) {
         return Lists.newArrayList(this.entries);
      }
   }

   public void addEntry(ImportHistoryEntry var1) {
      Preconditions.checkNotNull(var1, "entry");
      synchronized (this.getLock()) {
         this.entries.add(var1);
         this.store();
      }
   }

   private void store() {
      try {
         XmlPersistor var1 = XmlPersistor.newRoot("ImportHistory");

         for (ImportHistoryEntry var3 : this.entries) {
            var3.writeTo(var1.createChild("Entry"));
         }

         var1.store(this.getFile(true));
      } catch (IOException var4) {
         ErrorLog.log(ImportHistory.class, "An error occurred while saving the project import history.", var4);
      }
   }

   private Object getLock() {
      synchronized (locks) {
         Object var2 = locks.get(this.project.getLocation().getAbsolutePath());
         if (var2 == null) {
            var2 = new Object();
            locks.put(this.project.getLocation().getAbsolutePath(), var2);
         }

         return var2;
      }
   }

   private File getFile(boolean var1) {
      if (var1) {
         this.root.createOnDisk();
      }

      return this.root.getFile("ImportHistoryEntries.xml");
   }

   private class Reader {
      private Reader() {
      }

      public void read() {
         synchronized (ImportHistory.this.getLock()) {
            File var2 = ImportHistory.this.getFile(false);
            if (var2.exists()) {
               this.loadPersistor(var2);
            }
         }
      }

      private void loadPersistor(File var1) {
         try {
            XmlPersistor var2 = XmlPersistor.load(var1);

            for (Persistor var4 : var2.getChildren("Entry")) {
               ImportHistoryEntry var5 = ImportHistoryEntry.restore(var4);
               if (var5 != null) {
                  ImportHistory.this.entries.add(var5);
               }
            }
         } catch (Exception var6) {
            ErrorLog.log(ImportHistory.class, "An error occurred while restoring the project import history.", var6);
         }
      }
   }
}
