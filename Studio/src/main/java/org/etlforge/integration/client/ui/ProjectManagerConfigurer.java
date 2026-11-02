package org.etlforge.integration.client.ui;

import com.google.common.base.Supplier;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.ProjectLoadingTracker;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.ui.event.ApplicationMessageEvent;

final class ProjectManagerConfigurer {
   public static void configure(ProjectManager var0, ApplicationWindow var1) {
      setProjectLoadingTrackerProvider(var0, var1);
   }

   private static void setProjectLoadingTrackerProvider(ProjectManager var0, ApplicationWindow var1) {
      final ProjectManagerConfigurer.ProjectLoadingTrackerImpl var2 = new ProjectManagerConfigurer.ProjectLoadingTrackerImpl(var1);
      var0.setProjectLoadingTrackerProvider(new Supplier<ProjectLoadingTracker>() {
         public ProjectLoadingTracker get() {
            return var2;
         }
      });
   }

   private ProjectManagerConfigurer() {
   }

   private static class ProjectLoadingTrackerImpl implements ProjectLoadingTracker {
      private final ApplicationWindow window;

      public ProjectLoadingTrackerImpl(ApplicationWindow var1) {
         this.window = var1;
      }

      @Override
      public void beginLoadingProject() {
         this.postStatusMessage("Reading the project file");
      }

      @Override
      public void doneLoadingProject() {
      }

      @Override
      public void beginLoadingEntityType(EntityType var1) {
         this.postStatusMessage("Loading " + var1.getPlural());
      }

      @Override
      public void doneLoadingEntityType(EntityType var1) {
         this.postStatusMessage(" ");
      }

      @Override
      public void beginLoadingPluginInfo() {
         this.postStatusMessage("Loading plugin information");
      }

      @Override
      public void doneLoadingPluginInfo() {
         this.postStatusMessage(" ");
      }

      @Override
      public void beginLoadingUiState() {
         this.postStatusMessage("Restoring the UI");
      }

      @Override
      public void doneLoadingUiState() {
      }

      private void postStatusMessage(String var1) {
         String var2 = StringUtils.isBlank(var1) ? var1 : var1 + "...";
         ApplicationMessageEvent var3 = new ApplicationMessageEvent(this, var2);
         this.window.postApplicationMessageEvent(var3);
      }
   }
}
