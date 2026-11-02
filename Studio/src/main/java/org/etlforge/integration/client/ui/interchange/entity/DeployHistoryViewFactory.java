package org.etlforge.integration.client.ui.interchange.entity;

import org.etlforge.application.ui.window.AddContentWaitIndicator;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.WindowSectionContentProvider;
import org.etlforge.application.ui.window.WindowSectionContentUpdater;
import org.etlforge.integration.client.deploy.history.DeployHistory;
import org.etlforge.integration.client.deploy.history.NullDeployHistory;
import org.etlforge.integration.client.project.CurrentProjectEvent;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.deploy.history.DeployHistoryView;
import org.etlforge.integration.client.ui.interchange.CurrentProjectListenerResource;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class DeployHistoryViewFactory {
   private final CurrentProjectService currentProjectService;
   private final DeployHistory deployHistory;
   private IntegrationEntity entity;

   public DeployHistoryViewFactory(DeployHistory var1, CurrentProjectService var2) {
      this.deployHistory = var1;
      this.currentProjectService = var2;
   }

   public void setEntity(IntegrationEntity var1) {
      this.entity = var1;
   }

   public void openInWindow(ApplicationWindow var1) {
      WindowSectionContentProvider var2 = this.newContentProvider();
      WindowSectionContentUpdater var3 = this.newContentUpdater();
      AddContentWaitIndicator var4 = new AddContentWaitIndicator(var1.startWait());
      var1.displayContent(var2, ApplicationWindow.SOUTH, var3, var4);
   }

   public WindowSectionContentProvider newContentProvider() {
      return new DeployHistoryViewFactory.ContentProviderImpl();
   }

   public WindowSectionContentUpdater newContentUpdater() {
      IntegrationEntity var1 = this.entity != null ? this.entity : this.deployHistory.getProject();
      return new DeployHistoryViewFactory.Updater(var1);
   }

   private class ContentProviderImpl implements WindowSectionContentProvider {
      private ContentProviderImpl() {
      }

      @Override
      public String getContentId() {
         return DeployHistoryView.CONTENT_ID;
      }

      @Override
      public WindowSectionContent getContent(WindowSection var1) {
         DeployHistoryView var2 = new DeployHistoryView(DeployHistoryViewFactory.this.deployHistory, DeployHistoryViewFactory.this.entity, var1);
         var2.addResource(new DeployHistoryViewFactory.ProjectListenerImpl(DeployHistoryViewFactory.this.currentProjectService, var2));
         return var2;
      }
   }

   private static class ProjectListenerImpl extends CurrentProjectListenerResource {
      private final DeployHistoryView view;

      public ProjectListenerImpl(CurrentProjectService var1, DeployHistoryView var2) {
         super(var1);
         this.view = var2;
      }

      @Override
      public void projectOpened(CurrentProjectEvent var1) {
         ManagedProject var2 = var1.getManagedProject();
         this.view.setHistory(var2.getDeployHistory());
      }

      @Override
      public void projectClosed(CurrentProjectEvent var1) {
         this.view.setHistory(new NullDeployHistory());
      }
   }

   private static class Updater implements WindowSectionContentUpdater {
      private final IntegrationEntity entity;

      public Updater(IntegrationEntity var1) {
         this.entity = var1;
      }

      @Override
      public void update(WindowSectionContent var1) {
         ((DeployHistoryView)var1).setIntegrationEntity(this.entity);
      }
   }
}
