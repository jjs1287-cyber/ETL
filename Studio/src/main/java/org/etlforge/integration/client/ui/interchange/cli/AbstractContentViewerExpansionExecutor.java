package org.etlforge.integration.client.ui.interchange.cli;

import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewer;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewerManager;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.IntegrationProject;

abstract class AbstractContentViewerExpansionExecutor implements CommandExecutor {
   private final CurrentProjectService currentProject;
   private final ProjectContentViewerManager contentViewers;

   protected AbstractContentViewerExpansionExecutor(CurrentProjectService var1, ProjectContentViewerManager var2) {
      this.currentProject = var1;
      this.contentViewers = var2;
   }

   @Override
   public final void execute(String[] var1) throws CommandException {
      IntegrationProject var2 = this.currentProject.getProject();
      if (var2 == null) {
         throw new CommandException("No project is open.");
      }

      RootFolder var3 = null;
      if (var1.length == 1) {
         EntityType var4 = this.getType(var1[0]);
         if (var4 == null) {
            return;
         }

         var3 = var2.getRootFolder(var4);
      }

      this.execute(this.contentViewers.getViewer(), var3);
   }

   private EntityType getType(String var1) throws CommandException {
      String var2 = var1.toLowerCase();
      EntityType var3 = null;

      for (EntityType var7 : EntityType.values()) {
         if (var7.getPlural().toLowerCase().startsWith(var2)) {
            if (var3 != null) {
               return null;
            }

            var3 = var7;
         }
      }

      if (var3 != null) {
         return var3;
      } else {
         throw new CommandException("Unknown item type: " + var1);
      }
   }

   protected abstract void execute(ProjectContentViewer var1, RootFolder var2);
}
