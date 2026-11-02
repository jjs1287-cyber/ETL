package org.etlforge.integration.client.ui.interchange.cli;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.cli.CommandException;
import org.etlforge.collections.Iterables2;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.RecentProjects;
import org.etlforge.integration.client.ui.entity.EntityPageDisplayer;
import org.etlforge.integration.client.ui.interchange.component.launcher.ProjectLauncherViewer;
import org.etlforge.integration.client.ui.project.loading.ProjectLoader;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.project.IntegrationProject;

public final class OpenEntityExecutor extends AbstractProjectPathCommandExecutor {
   private final ApplicationWindow appWin;
   private final ProjectManager projectManager;
   private final EntityPageDisplayer pageDisplayer;
   private final ProjectLauncherViewer projectLauncher;
   private final boolean openProjectSupported;

   public OpenEntityExecutor(
      ApplicationWindow var1, CurrentProjectService var2, ProjectManager var3, EntityPageDisplayer var4, ProjectLauncherViewer var5, boolean var6
   ) {
      super(var2);
      this.appWin = var1;
      this.projectManager = var3;
      this.pageDisplayer = var4;
      this.projectLauncher = var5;
      this.openProjectSupported = var6;
   }

   @Override
   protected void processMatchingItems(Set<IntegrationEntity> var1) {
      for (IntegrationEntity var3 : var1) {
         this.pageDisplayer.openPage(var3);
      }
   }

   @Override
   protected void noMatchingItems(String var1) throws CommandException {
      if (var1.startsWith("project")) {
         this.executeOpenProject(var1);
      } else {
         this.openEntity(var1.split(" "));
      }
   }

   private void executeOpenProject(String var1) throws CommandException {
      if (this.openProjectSupported) {
         String var2 = StringUtils.substringAfter(var1, "project ");
         if (StringUtils.isBlank(var2)) {
            this.projectLauncher.open();
         } else {
            this.openExistingProject(var2);
         }
      }
   }

   private void openExistingProject(String var1) throws CommandException {
      File var2 = new File(this.projectManager.getProjectDirectory().getLocation(), var1);
      if (var2.exists() && var2.isDirectory()) {
         String var6 = var2.getName();
         ProjectLocation var7 = new ProjectLocation(var6, var2);
         this.loadProjectFromDisk(var7);
      } else {
         RecentProjects var3 = this.projectManager.getRecentProjects();

         for (ProjectLocation var5 : var3.getExistingLocations()) {
            if (var5.getProjectName().equalsIgnoreCase(var1)) {
               this.loadProjectFromDisk(var5);
               return;
            }
         }

         throw new CommandException("I cannot find the project \"" + var1 + "\".");
      }
   }

   private void loadProjectFromDisk(ProjectLocation var1) {
      ProjectLoader var2 = new ProjectLoader(this.projectManager);
      var2.setWaitService(this.appWin);
      var2.loadAndDisplay(var1);
   }

   private void openEntity(String[] var1) throws CommandException {
      IntegrationProject var2 = this.getProject();
      if (var2 == null) {
         throw new CommandException("No project open.");
      }

      IntegrationEntity var3 = this.findEntity(var2, var1);
      if (var3 != null) {
         this.pageDisplayer.openPage(var3);
      } else {
         throw new CommandException("Could not find the requested item.");
      }
   }

   private IntegrationEntity findEntity(IntegrationProject var1, String[] var2) throws CommandException {
      List var3 = this.getCandidates(var1);
      String var4 = this.getName(var2, 0);
      IntegrationEntity var5 = new OpenEntityExecutor.Searcher(var4, var3).find();
      if (var5 == null) {
         var5 = this.doTypeSearch(var3, var2);
      }

      return var5;
   }

   private List<IntegrationEntity> getCandidates(IntegrationProject var1) {
      List var2 = this.getCandidates(var1, null);
      Iterables.removeIf(var2, Predicates.instanceOf(Folder.class));
      return var2;
   }

   private String getName(String[] var1, int var2) {
      String[] var3;
      if (var2 == 0) {
         var3 = var1;
      } else {
         var3 = new String[var1.length - 1];
         System.arraycopy(var1, 1, var3, 0, var3.length);
      }

      return StringUtils.join(var3, " ");
   }

   private IntegrationEntity doTypeSearch(List<IntegrationEntity> var1, String[] var2) {
      EntityType var3 = this.getType(var2);
      if (var3 != null) {
         String var4 = this.getName(var2, 1);
         Iterable var5 = Iterables.filter(var1, EntityFilters.allOfType(var3));
         OpenEntityExecutor.Searcher var6 = new OpenEntityExecutor.Searcher(var4, var5);
         return var6.find();
      } else {
         return null;
      }
   }

   private EntityType getType(String[] var1) {
      for (EntityType var5 : EntityType.values()) {
         if (var5.name().toLowerCase().startsWith(var1[0].toLowerCase())) {
            return var5;
         }
      }

      return null;
   }

   private List<IntegrationEntity> getCandidates(IntegrationProject var1, EntityType var2) {
      List var3 = var1.getAllEntities();
      if (var2 != null) {
         Iterables2.removeIfNot(var3, EntityFilters.allOfType(var2));
      }

      return var3;
   }

   private static final class Searcher {
      private final String name;
      private final Iterable<IntegrationEntity> candidates;

      public Searcher(String var1, Iterable<IntegrationEntity> var2) {
         this.name = var1;
         this.candidates = var2;
      }

      public IntegrationEntity find() {
         for (IntegrationEntity var2 : this.candidates) {
            if (var2.getName().equalsIgnoreCase(this.name)) {
               return var2;
            }
         }

         String var7 = this.name.toLowerCase();

         for (IntegrationEntity var3 : this.candidates) {
            if (var3.getName().toLowerCase().startsWith(var7)) {
               return var3;
            }
         }

         for (IntegrationEntity var13 : this.candidates) {
            if (var13.getName().toLowerCase().contains(var7)) {
               return var13;
            }
         }

         try {
            Pattern var10 = Pattern.compile(var7);

            for (IntegrationEntity var4 : this.candidates) {
               Matcher var5 = var10.matcher(var4.getName().toLowerCase());
               if (var5.find()) {
                  return var4;
               }
            }
         } catch (PatternSyntaxException var6) {
         }

         for (IntegrationEntity var15 : this.candidates) {
            if (var15.getID().toString().equalsIgnoreCase(this.name)) {
               return var15;
            }
         }

         for (IntegrationEntity var16 : this.candidates) {
            if (var16.getID().toString().toLowerCase().startsWith(var7)) {
               return var16;
            }
         }

         return null;
      }
   }
}
