package com.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.WindowSectionContentProvider;
import org.etlforge.application.ui.window.WindowSectionContentUpdater;
import org.etlforge.integration.client.plugin.serverapi.PipelinePluginInfoProvider;
import org.etlforge.integration.client.plugin.ui.pipeline.view.RequiredPipelinePluginsView;
import org.etlforge.integration.client.plugin.ui.pipeline.view.RequiredPipelinePluginsViewFactory;
import org.etlforge.integration.client.project.CurrentProjectEvent;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.interchange.CurrentProjectListenerResource;
import org.etlforge.integration.client.ui.interchange.core.action.AbstractProjectBasedAction;

public final class ViewRequiredPipelinePluginsAction extends AbstractProjectBasedAction {
   public static final String ID = "action.ic.ProjectPlugins";
   private final ApplicationWindow appWin;
   private final PipelinePluginInfoProvider infoProvider;

   public ViewRequiredPipelinePluginsAction(ApplicationWindow var1, CurrentProjectService var2, PipelinePluginInfoProvider var3) {
      super(var2, "action.ic.ProjectPlugins", EnterpriseActionDictionary.DICTIONARY);
      this.appWin = var1;
      this.infoProvider = var3;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ViewRequiredPipelinePluginsAction.this.openPluginsView();
         }
      });
   }

   private void openPluginsView() {
      WindowSectionContentProvider var1 = this.createContentProvider();
      WindowSectionContentUpdater var2 = this.createContentUpdater();
      this.appWin.displayContent(var1, ApplicationWindow.SectionPosition.EAST, var2, null);
   }

   private WindowSectionContentProvider createContentProvider() {
      ManagedProject var1 = this.getManagedProject();
      RequiredPipelinePluginsViewFactory var2 = new RequiredPipelinePluginsViewFactory(this.infoProvider);
      var2.setProjectPlugins(var1.getPipelinePlugins());
      return new ViewRequiredPipelinePluginsAction.ProviderWrapper(var2, this.getCurrentProjectService());
   }

   private WindowSectionContentUpdater createContentUpdater() {
      return new WindowSectionContentUpdater() {
         @Override
         public void update(WindowSectionContent var1) {
            ((RequiredPipelinePluginsView)var1).refresh();
         }
      };
   }

   private static class ProjectListener extends CurrentProjectListenerResource {
      private final RequiredPipelinePluginsView view;

      public ProjectListener(RequiredPipelinePluginsView var1, CurrentProjectService var2) {
         super(var2);
         this.view = var1;
      }

      @Override
      public void projectOpened(CurrentProjectEvent var1) {
         ManagedProject var2 = var1.getManagedProject();
         this.view.setProjectPlugins(var2.getPipelinePlugins());
      }

      @Override
      public void projectClosed(CurrentProjectEvent var1) {
         this.view.setProjectPlugins(null);
      }
   }

   private static class ProviderWrapper implements WindowSectionContentProvider {
      private final RequiredPipelinePluginsViewFactory impl;
      private final CurrentProjectService currentProject;

      public ProviderWrapper(RequiredPipelinePluginsViewFactory var1, CurrentProjectService var2) {
         this.impl = var1;
         this.currentProject = var2;
      }

      @Override
      public String getContentId() {
         return this.impl.getContentId();
      }

      @Override
      public WindowSectionContent getContent(WindowSection var1) {
         RequiredPipelinePluginsView var2 = this.impl.getContent(var1);
         ViewRequiredPipelinePluginsAction.ProjectListener var3 = new ViewRequiredPipelinePluginsAction.ProjectListener(var2, this.currentProject);
         var2.addResource(var3);
         return var2;
      }
   }
}
