package org.etlforge.integration.client.ui.admin.component.project;

import com.google.common.base.Objects;
import java.util.List;
import org.etlforge.application.ui.util.SingleLockWaitServiceBinding;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.ActiveObjectProviderResource;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.DisposableWindowElementResource;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.WindowElementStateSpan;
import org.etlforge.application.ui.window.event.ActiveObjectProvider;
import org.etlforge.application.ui.window.event.ActiveObjectProviderListener;
import org.etlforge.integration.client.project.info.ProjectInfoCache;
import org.etlforge.integration.client.project.info.ProjectInfoProvider;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.ui.admin.core.console.AbstractConsolePage;
import org.etlforge.integration.client.ui.admin.resources.AdminIcons;
import org.etlforge.integration.client.ui.admin.resources.AdminViewResources;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleView;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.util.Property;

public final class ProjectConsolePage extends AbstractConsolePage implements ActiveObjectProvider {
   private final ProjectConsoleView consoleView;
   private final ProjectConsoleController controller;
   private final ServerConnectionService connectionService;

   public ProjectConsolePage(ProjectConsoleView var1, ProjectConsoleActions var2, ServerConnectionService var3, ProjectInfoProvider var4) {
      super("Admin.Console.Project", AdminViewResources.STRINGS.getString("ProjectConsole.Page.Title"));
      this.consoleView = var1;
      this.connectionService = var3;
      this.controller = new ProjectConsoleController(var1, var2, var3, var4);
      this.setIcon(AdminIcons.PROJECT_CONSOLE_16);
      this.setMementoPath(new String[]{"Admin", "ProjectConsole"});
      this.useDefaultCategory();
      this.addResource(new ProjectConsolePage.ProjectsViewerActivator());
   }

   public void setCache(ProjectInfoCache var1) {
      this.controller.setCache(var1);
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.consoleView.setWaitService(this.getWaitService());
      this.installWaitIndication();
      this.controller.activate();
      this.addResource(new ActiveObjectProviderResource(var1.getActiveObjectService(), this.consoleView.getProjectsViewer()));
      this.addResource(new DisposableWindowElementResource(this.consoleView, this.controller));
      this.layoutPage();
   }

   private void installWaitIndication() {
      Binding var1 = this.createWaitHandler();
      var1.syncUi();
      this.addResource(new DisposableWindowElementResource(var1));
   }

   private Binding createWaitHandler() {
      Property var1 = this.consoleView.getModel().getProperty("talkingToServer");
      WaitService var2 = this.getWaitService();
      return new SingleLockWaitServiceBinding(var1, var2);
   }

   private void layoutPage() {
      this.createTitlePanel();
      this.setToolBar(this.createToolBar());
      this.setMainPanel(BorderLayoutBuilder.fillWith(this.consoleView));
   }

   @Override
   public List<?> getActiveObjects() {
      return this.consoleView.getProjectsViewer().getActiveObjects();
   }

   @Override
   public String getProviderId() {
      return this.consoleView.getProjectsViewer().getProviderId();
   }

   @Override
   public boolean isActive() {
      return this.consoleView.getProjectsViewer().isActive();
   }

   @Override
   public void addActiveObjectListener(ActiveObjectProviderListener var1) {
      this.consoleView.getProjectsViewer().addActiveObjectListener(var1);
   }

   @Override
   public void removeActiveObjectListener(ActiveObjectProviderListener var1) {
      this.consoleView.getProjectsViewer().removeActiveObjectListener(var1);
   }

   public void refreshIfStillConnected(ServerGuid var1) {
      if (Objects.equal(this.connectionService.getCurrentServerGuid(), var1)) {
         this.controller.downloadProjectInfo(false);
      }
   }

   private class ProjectsViewerActivator extends AbstractWindowElementResource {
      public ProjectsViewerActivator() {
         super(WindowElementStateSpan.WHEN_ACTIVE);
      }

      @Override
      public void create(WindowElement var1) {
         ProjectConsolePage.this.consoleView.getProjectsViewer().setActive(true);
      }

      @Override
      public void dispose(WindowElement var1) {
         ProjectConsolePage.this.consoleView.getProjectsViewer().setActive(false);
      }
   }
}
