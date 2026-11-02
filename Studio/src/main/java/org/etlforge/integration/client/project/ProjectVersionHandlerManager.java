package org.etlforge.integration.client.project;

import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.etlforge.application.config.FatalConfigurationError;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.util.version.Version;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;

final class ProjectVersionHandlerManager {
   private static final String CONFIGURATION_RESOURCE = "Project.VersionHandlers.xml";
   private boolean initialized;
   private final ProjectVersionHandlerManager.Factories factories = new ProjectVersionHandlerManager.Factories();
   private final Object lock = new Object();

   public ProjectVersionHandlerManager() {
   }

   public void registerHandlerFactory(ProjectVersionHandlerFactory var1) {
      synchronized (this.lock) {
         this.factories.add(var1);
      }
   }

   public ProjectVersionHandlerManager.Job getJob(String var1, File var2) {
      return new ProjectVersionHandlerManager.JobImpl(var1, var2);
   }

   private static final class Factories {
      private List<ProjectVersionHandlerFactory> factoryList = new ArrayList<>();
      private final Comparator<ProjectVersionHandlerFactory> sorter = new ProjectVersionHandlerManager.Factories.Sorter();

      public Factories() {
      }

      public void add(ProjectVersionHandlerFactory var1) {
         this.factoryList.add(var1);
         Collections.sort(this.factoryList, this.sorter);

         for (int var2 = this.factoryList.size() - 1; var2 >= 0; var2--) {
            if (this.factoryList.get(var2).handlesAllEarlierVersions()) {
               this.factoryList = this.factoryList.subList(var2, this.factoryList.size());
               break;
            }
         }
      }

      public List<ProjectVersionHandler> getHandlers(Version var1) {
         ArrayList var2 = new ArrayList();

         for (ProjectVersionHandlerFactory var4 : this.factoryList) {
            if (!var1.isSameAsOrEarlierThan(var4.getLastVersion())) {
               break;
            }

            var2.add(var4.getHandler());
         }

         return var2;
      }

      private static final class Sorter implements Comparator<ProjectVersionHandlerFactory>, Serializable {
         private Sorter() {
         }

         public int compare(ProjectVersionHandlerFactory var1, ProjectVersionHandlerFactory var2) {
            return -var1.getLastVersion().compareTo(var2.getLastVersion());
         }
      }
   }

   private final class Initializer {
      private Initializer() {
      }

      public void run() {
         try {
            KongaSaxParser var1 = new KongaSaxParser(this);
            var1.parseResource("Project.VersionHandlers.xml");
         } catch (Exception var2) {
            this.fatalErrorOccurred(var2);
         }
      }

      @EndElementHandler("Factory")
      public void loadFactory(String var1) throws Exception {
         Class var2 = Class.forName(var1);
         ProjectVersionHandlerFactory var3 = (ProjectVersionHandlerFactory)var2.newInstance();
         ProjectVersionHandlerManager.this.registerHandlerFactory(var3);
      }

      private void fatalErrorOccurred(Exception var1) {
         FatalConfigurationError.throwError(9, this, var1, "Failed to initialize the Project Version Handler Manager.");
      }
   }

   public interface Job {
      boolean preLoading(UpgradeStrategy var1);

      void postLoading(ManagedProject var1);
   }

   private final class JobImpl implements ProjectVersionHandlerManager.Job {
      private final String projectName;
      private ProjectFile projectFile;
      private final List<ProjectVersionHandler> handlers;

      public JobImpl(String var2, File var3) {
         this.projectName = var2;
         synchronized (ProjectVersionHandlerManager.this.lock) {
            if (!ProjectVersionHandlerManager.this.initialized) {
               this.initialize();
            }

            this.handlers = this.getApplicableHandlers(var3);
         }
      }

      private void initialize() {
         ProjectVersionHandlerManager.this.new Initializer().run();
         ProjectVersionHandlerManager.this.initialized = true;
      }

      private List<ProjectVersionHandler> getApplicableHandlers(File var1) {
         try {
            this.projectFile = ProjectFile.createForExistingProject(this.projectName, var1, false);
            Version var2 = this.projectFile.getVersionSavedWith();
            return ProjectVersionHandlerManager.this.factories.getHandlers(var2);
         } catch (IOException var3) {
            ErrorLog.log(ProjectVersionHandlerManager.class, "Failed to retrieve the version the project was saved in: " + var3.getMessage(), var3);
            return ImmutableList.of();
         }
      }

      @Override
      public boolean preLoading(UpgradeStrategy var1) {
         for (ProjectVersionHandler var3 : this.handlers) {
            ProjectVersionHandler.PreLoadingResult var4 = var3.preLoading(this.projectName, this.projectFile, var1);
            switch (var4) {
               case DENIED:
                  return false;
               case ALLOWED:
                  var1 = UpgradeStrategy.AUTOMATIC;
               case AUTOMATIC:
               case NOT_APPLICABLE:
            }
         }

         return true;
      }

      @Override
      public void postLoading(ManagedProject var1) {
         if (!this.handlers.isEmpty()) {
            boolean var2 = this.processProjects(var1.getProject(), var1.getProjectFile(), this.handlers);
            if (var2) {
               this.saveProject(var1);
            }
         }
      }

      private boolean processProjects(IntegrationProject var1, ProjectFile var2, List<ProjectVersionHandler> var3) {
         assert var3 != null && !var3.isEmpty();
         boolean var4 = false;

         for (ProjectVersionHandler var6 : var3) {
            if (var6.postLoading(var1, var2)) {
               var4 = true;
            }
         }

         return var4;
      }

      private void saveProject(ManagedProject var1) {
         try {
            var1.saveToDisk();
         } catch (InterchangeSavingException var3) {
            var3.printStackTrace();
         }
      }
   }
}
