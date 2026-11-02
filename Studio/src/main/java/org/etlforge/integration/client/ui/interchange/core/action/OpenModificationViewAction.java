package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.WindowSectionContentProvider;
import org.etlforge.integration.client.project.CurrentProjectEvent;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.entity.ModificationMonitorWindowContent;
import org.etlforge.integration.client.ui.interchange.CurrentProjectListenerResource;
import org.etlforge.integration.data.project.IntegrationProject;

public final class OpenModificationViewAction extends AbstractProjectBasedAction {
   public static final String ID = "action.ic.ModificationView";
   private final ApplicationWindow appWin;

   public OpenModificationViewAction(ApplicationWindow var1, CurrentProjectService var2) {
      super(var2, "action.ic.ModificationView", InterchangeActionDictionary.DICTIONARY);
      this.appWin = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            OpenModificationViewAction.this.open();
         }
      });
   }

   private void open() {
      OpenModificationViewAction.ContentProviderImpl var1 = new OpenModificationViewAction.ContentProviderImpl(
         this.getCurrentProjectService(), this.getProject()
      );
      this.appWin.displayContent(var1, ApplicationWindow.SectionPosition.SOUTH, null, null);
   }

   private static class ContentProviderImpl implements WindowSectionContentProvider {
      private final CurrentProjectService currentProjectService;
      private final IntegrationProject project;

      public ContentProviderImpl(CurrentProjectService var1, IntegrationProject var2) {
         this.currentProjectService = var1;
         this.project = var2;
      }

      @Override
      public String getContentId() {
         return ModificationMonitorWindowContent.ID;
      }

      @Override
      public WindowSectionContent getContent(WindowSection var1) {
         ModificationMonitorWindowContent var2 = new ModificationMonitorWindowContent(var1);
         var2.setProject(this.project);
         OpenModificationViewAction.ContentProviderImpl.ProjectListener var3 = new OpenModificationViewAction.ContentProviderImpl.ProjectListener(
            this.currentProjectService, var2
         );
         var2.addResource(var3);
         return var2;
      }

      private static class ProjectListener extends CurrentProjectListenerResource {
         private final ModificationMonitorWindowContent view;

         public ProjectListener(CurrentProjectService var1, ModificationMonitorWindowContent var2) {
            super(var1);
            this.view = var2;
         }

         @Override
         public void projectOpened(CurrentProjectEvent var1) {
            this.view.setProject(var1.getProject());
         }

         @Override
         public void projectClosed(CurrentProjectEvent var1) {
            this.view.setProject(null);
         }
      }
   }
}
