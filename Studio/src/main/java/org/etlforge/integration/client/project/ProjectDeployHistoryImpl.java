package org.etlforge.integration.client.project;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.deploy.history.DeployHistory;
import org.etlforge.integration.client.deploy.history.DeployHistoryListener;
import org.etlforge.integration.client.deploy.history.DeployHistoryListeners;
import org.etlforge.integration.client.deploy.history.DeployResultFilter;
import org.etlforge.integration.client.deploy.history.PersistableDeployResult;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.deploy.serverapi.DeployResultVisitor;
import org.etlforge.integration.client.project.prefs.DeployHistoryAgePreference;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.io.Folder;
import org.etlforge.util.persist.NonFunctioningPersistorStore;
import org.etlforge.util.persist.PersistableFactory;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.persist.PersistorStore;
import org.etlforge.util.persist.XmlPersistorStore;
import org.etlforge.util.time.DateUtils;

final class ProjectDeployHistoryImpl implements DeployHistory {
   private final IntegrationProject project;
   private final DateFormat dateFolderFormat;
   private final Folder root;
   private final ProjectDeployHistoryImpl.LoadFactory loadFactory;
   private final DeployHistoryListeners listeners;
   private final Object lock;

   public ProjectDeployHistoryImpl(ManagedProject var1) {
      this.project = var1.getProject();
      this.dateFolderFormat = new SimpleDateFormat("yyyy-MM-dd");
      this.root = this.createRootFolder(var1);
      this.loadFactory = new ProjectDeployHistoryImpl.LoadFactory();
      this.listeners = new DeployHistoryListeners();
      this.lock = new Object();
      new ProjectDeployHistoryImpl.FileCleanup().start();
   }

   private Folder createRootFolder(ManagedProject var1) {
      Folder var2 = ProjectFolders.getPersistFolder(var1.getLocation());
      return var2.subFolder("DeployHistory");
   }

   @Override
   public IntegrationProject getProject() {
      return this.project;
   }

   @Override
   public void addResult(DeployResult var1) {
      synchronized (this.lock) {
         this.persistResult(var1);
      }

      this.listeners.call().deployResultAdded(var1);
   }

   @Override
   public List<DeployResult> getAllResults() {
      return this.getResults(DeployResultFilter.ACCEPTS_ALL);
   }

   public List<DeployResult> getResults(DeployResultFilter var1) {
      ArrayList var2 = Lists.newArrayList();
      synchronized (this.lock) {
         this.loadPersistedResults(var2, var1);
      }

      this.sortResults(var2);
      return var2;
   }

   private void loadPersistedResults(List<DeployResult> var1, DeployResultFilter var2) {
      for (File var6 : this.getDateDirs()) {
         this.loadResultsForDate(var6, var2, var1);
      }
   }

   private void sortResults(List<DeployResult> var1) {
      Collections.sort(var1, new Comparator<DeployResult>() {
         public int compare(DeployResult var1, DeployResult var2) {
            return var2.getTimestamp().compareTo(var1.getTimestamp());
         }
      });
   }

   private void loadResultsForDate(File var1, DeployResultFilter var2, List<DeployResult> var3) {
      XmlPersistorStore var4 = new XmlPersistorStore(var1);
      var3.addAll(KongaListUtils.filter(var4.load(this.loadFactory), var2));
   }

   @Override
   public void removeAll() {
      synchronized (this.lock) {
         for (File var5 : this.getDateDirs()) {
            try {
               FileUtils.deleteDirectory(var5);
            } catch (IOException var8) {
               var8.printStackTrace();
            }
         }
      }

      this.listeners.call().historyCleared();
   }

   @Override
   public void removeResult(DeployResult var1) {
      synchronized (this.lock) {
         File var3 = this.getDirForResult(var1);
         if (!var3.exists()) {
            return;
         }

         String var4 = createFileName(var1);
         File var5 = new File(var3, var4);
         if (!var5.exists()) {
            return;
         }

         var5.delete();
      }

      this.listeners.call().deployResultRemoved(var1);
   }

   private static String createFileName(DeployResult var0) {
      ProjectDeployHistoryImpl.FileNameGenerator var1 = new ProjectDeployHistoryImpl.FileNameGenerator();
      return var1.visit(var0);
   }

   @Override
   public void addListener(DeployHistoryListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public void removeListener(DeployHistoryListener var1) {
      this.listeners.remove(var1);
   }

   private void persistResult(DeployResult var1) {
      PersistableDeployResult var2 = new PersistableDeployResult(var1);
      PersistorStore var3 = this.getStoreForResult(var2);
      var3.store(var2, var2.getPersistName());
   }

   private PersistorStore<PersistableDeployResult> getStoreForResult(PersistableDeployResult var1) {
      File var2 = this.getDirForResult(var1);
      return ensureDirExists(var2) ? new XmlPersistorStore<PersistableDeployResult>(var2) {
         protected File getFileForPersistable(File var1, PersistableDeployResult var2x) {
            return new File(var1, ProjectDeployHistoryImpl.createFileName(var2x));
         }
      } : new NonFunctioningPersistorStore<>();
   }

   private File getDirForResult(DeployResult var1) {
      return this.root.getFile(this.dateFolderFormat.format(var1.getTimestamp()));
   }

   private File[] getDateDirs() {
      return this.root.listFiles(org.etlforge.io.FileUtils.DIRECTORIES);
   }

   private static boolean ensureDirExists(File var0) {
      return var0.exists() ? true : var0.mkdirs();
   }

   private class FileCleanup {
      private final int ageLimit = DeployHistoryAgePreference.get();
      private final Date now = new Date();

      private FileCleanup() {
      }

      public void start() {
         Thread var1 = new Thread("Deploy History Cleaner") {
            @Override
            public void run() {
               FileCleanup.this.doCleanup();
            }
         };
         var1.setPriority(2);
         var1.start();
      }

      private void doCleanup() {
         synchronized (ProjectDeployHistoryImpl.this.lock) {
            for (File var3 : this.collectFoldersToDelete()) {
               try {
                  FileUtils.deleteDirectory(var3);
               } catch (IOException var6) {
                  ErrorLog.log(ProjectDeployHistoryImpl.class, "Failed to delete old deploy record", var6);
               }
            }
         }
      }

      private List<File> collectFoldersToDelete() {
         ArrayList var1 = Lists.newArrayList();

         for (File var5 : ProjectDeployHistoryImpl.this.getDateDirs()) {
            if (this.checkFolder(var5)) {
               var1.add(var5);
            }
         }

         return var1;
      }

      private boolean checkFolder(File var1) {
         try {
            Date var2 = ProjectDeployHistoryImpl.this.dateFolderFormat.parse(var1.getName());
            if (var2.before(this.now)) {
               int var3 = DateUtils.getDaysBetween(var2, this.now);
               return var3 > this.ageLimit;
            } else {
               return false;
            }
         } catch (Exception var4) {
            var4.printStackTrace();
            return true;
         }
      }
   }

   private static class FileNameGenerator extends DeployResultVisitor<String> {
      private FileNameGenerator() {
      }

      protected String onSuccess(DeployResult var1) {
         return createFileName(var1, "DeploySuccess");
      }

      protected String onWarnings(DeployResult var1) {
         return createFileName(var1, "DeployWarning");
      }

      protected String onFailure(DeployResult var1) {
         return createFileName(var1, "DeployFailure");
      }

      private static String createFileName(DeployResult var0, String var1) {
         return var1 + "_" + var0.getTimestamp().getTime() + ".xml";
      }
   }

   private static class LoadFactory implements PersistableFactory<PersistableDeployResult> {
      private LoadFactory() {
      }

      public PersistableDeployResult restore(Persistor var1) {
         PersistableDeployResult.Factory var2 = new PersistableDeployResult.Factory();
         return (PersistableDeployResult)var2.restore(var1);
      }
   }
}
