package org.etlforge.integration.client.ui.interchange.cli;

import com.google.common.collect.ImmutableSet;
import java.io.File;
import javax.swing.Action;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.Application;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.integration.client.project.ProjectDirectory;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.client.ui.interchange.entity.project.loader.DialogBasedNewProjectLoader;
import org.etlforge.integration.client.ui.interchange.entity.project.loader.NewProjectCreator;
import org.etlforge.integration.data.ProjectNameValidator;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.util.name.IllegalNameException;

public final class CreateNewEntityExecutor implements CommandExecutor {
   private final ApplicationWindow appWin;
   private final EntityConfigLookup entityConfigs;
   private final ProjectManager projectManager;
   private final ImmutableSet<EntityType> supportedTypes;
   private final boolean allowNewProjects;

   public CreateNewEntityExecutor(ApplicationWindow var1, EntityConfigLookup var2, ProjectManager var3, Iterable<EntityType> var4, boolean var5) {
      this.appWin = var1;
      this.entityConfigs = var2;
      this.projectManager = var3;
      this.supportedTypes = ImmutableSet.copyOf(var4);
      this.allowNewProjects = var5;
   }

   @Override
   public void execute(String[] var1) throws CommandException {
      if (this.isNewProjectCommand(var1)) {
         this.executeNewProjectCommand(var1);
      } else {
         this.createNewEntityInProject(var1);
      }
   }

   private boolean isNewProjectCommand(String[] var1) {
      return this.allowNewProjects && var1[0].equalsIgnoreCase("project");
   }

   private void executeNewProjectCommand(String[] var1) throws CommandException {
      if (var1.length == 1) {
         this.createNewProject();
      } else {
         this.createNewNamedProject(var1);
      }
   }

   private void createNewProject() {
      DialogBasedNewProjectLoader var1 = new DialogBasedNewProjectLoader(this.projectManager);
      var1.loadNewProject(this.appWin);
   }

   private void createNewNamedProject(String[] var1) throws CommandException {
      String[] var2 = new String[var1.length - 1];
      System.arraycopy(var1, 1, var2, 0, var2.length);
      String var3 = StringUtils.join(var2, " ");
      ProjectDirectory var4 = this.projectManager.getProjectDirectory();
      File var5 = new File(var4.getLocation(), var3);
      String var6 = this.getNewProjectName(var5);
      ProjectLocation var7 = new ProjectLocation(var6, var5);
      WaitLock var8 = this.appWin.startWait();
      NewProjectCreator var9 = new NewProjectCreator(this.projectManager, var7, var8);
      Application.getWorker().submitForParallel(var9);
   }

   private String getNewProjectName(File var1) throws CommandException {
      try {
         String var2 = var1.getName();
         ProjectNameValidator var3 = new ProjectNameValidator();
         var3.validateName(var2);
         return var2;
      } catch (IllegalNameException var4) {
         throw new CommandException(var4.getMessage());
      }
   }

   private void createNewEntityInProject(String[] var1) throws CommandException {
      if (this.projectManager.getCurrentProject() == null) {
         throw new CommandException("No project open.");
      }

      EntityType var2 = this.getTypeToCreate(var1);
      if (var2 == null) {
         throw new CommandException("Unknown or unsupported type: " + var1[0]);
      }

      this.createEntityOfType(var2);
   }

   private EntityType getTypeToCreate(String[] var1) {
      String var2 = var1[0].toLowerCase();

      for (EntityType var4 : this.supportedTypes) {
         if (var4.name().toLowerCase().startsWith(var2)) {
            return var4;
         }
      }

      return null;
   }

   private void createEntityOfType(EntityType var1) {
      Action var2 = this.getActionToRun(var1);
      if (var2.isEnabled()) {
         ActionUtils.run(var2, this, "create-new");
      }
   }

   private Action getActionToRun(EntityType var1) {
      EntityConfig var2 = this.entityConfigs.getEntityConfig(var1);
      return var2.newEntityAction();
   }
}
