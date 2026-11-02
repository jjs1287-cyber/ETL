package org.etlforge.integration.client.ui.interchange.entity.operation.page;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.DisposableWindowElementResource;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.editor.EditorDisplayer;
import org.etlforge.cli.Command;
import org.etlforge.integration.activity.serverapi.history.OperationHistoryProvider;
import org.etlforge.integration.activity.serverapi.history.SourceHistoryProvider;
import org.etlforge.integration.activity.serverapi.log.OperationLogProvider;
import org.etlforge.integration.activity.serverapi.queue.OperationQueueProvider;
import org.etlforge.integration.activity.ui.core.action.ShowHistoryAction;
import org.etlforge.integration.activity.ui.core.action.ShowLogAction;
import org.etlforge.integration.activity.ui.core.action.ShowQueueAction;
import org.etlforge.integration.activity.ui.monitor.OperationMonitor;
import org.etlforge.integration.activity.ui.monitor.OperationMonitorApis;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.project.event.UpdatedDependenciesEvent;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.interchange.component.page.entity.ActiveObjectPageAction;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequenceFactory;
import org.etlforge.integration.client.ui.interchange.entity.DefaultPageConfig;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.GraphLayoutFileStore;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraphManager;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.DefaultOperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.HttpEndpoint;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class OperationPageConfig extends DefaultPageConfig {
   private final UiEntityFactory entityFactory;
   private EditorDisplayer activityPageDisplayer;
   private OperationLogProvider logApi;
   private OperationQueueProvider queueApi;
   private OperationHistoryProvider opHistoryApi;
   private SourceHistoryProvider sourceHistoryApi;
   private OperationGraphActions graphActions;
   private OperationRunnerFactory opRunnerFactory;

   public OperationPageConfig(ApplicationWindow var1, ProjectPersistor var2, UiEntityFactory var3) {
      super(var1, var2);
      this.entityFactory = var3;
   }

   public void setSaveConsequenceFactories(List<? extends SaveConsequenceFactory> var1) {
      for (SaveConsequenceFactory var3 : var1) {
         this.addSaveConsequenceFactory(var3);
      }
   }

   public void setActivityPageDisplayer(EditorDisplayer var1) {
      this.activityPageDisplayer = var1;
   }

   public void setLogApi(OperationLogProvider var1) {
      this.logApi = var1;
   }

   public void setQueueApi(OperationQueueProvider var1) {
      this.queueApi = var1;
   }

   public void setOperationHistoryApi(OperationHistoryProvider var1) {
      this.opHistoryApi = var1;
   }

   public void setSourceHistoryApi(SourceHistoryProvider var1) {
      this.sourceHistoryApi = var1;
   }

   public void setGraphActions(OperationGraphActions var1) {
      this.graphActions = var1;
   }

   public void setOpRunnerFactory(OperationRunnerFactory var1) {
      this.opRunnerFactory = var1;
   }

   @Override
   protected void preConfiguration(IntegrationEntityPage var1) {
      this.addAdditionalToolbarButtons(var1);
      this.installCommands((OperationPage)var1);
   }

   private void addAdditionalToolbarButtons(IntegrationEntityPage var1) {
      ApplicationAction var2 = this.getRunAction();
      if (var2 != null) {
         ActiveObjectPageAction var3 = this.createActiveObjectAction(var1, var2);
         this.addToolbarButton(this.createToolbarButton(var3));
      }

      for (ApplicationAction var4 : this.createActivityActions(var1)) {
         ActiveObjectPageAction var5 = this.createActiveObjectAction(var1, var4);
         this.addToolbarButton(this.createToolbarButton(var5));
      }
   }

   private ApplicationAction getRunAction() {
      return this.graphActions == null ? null : this.graphActions.getAction("action.ic.RunOperation");
   }

   private List<? extends ApplicationAction> createActivityActions(IntegrationEntityPage var1) {
      ArrayList var2 = Lists.newArrayList();
      ApplicationWindow var3 = this.getWindow();
      if (this.logApi != null) {
         ShowLogAction var4 = new ShowLogAction(var3, this.logApi);
         var4.setPageDisplayer(this.activityPageDisplayer);
         var2.add(var4);
      }

      if (this.queueApi != null) {
         ShowQueueAction var5 = new ShowQueueAction(var3, this.queueApi);
         var5.setPageDisplayer(this.activityPageDisplayer);
         var2.add(var5);
      }

      if (this.opHistoryApi != null && this.sourceHistoryApi != null) {
         ShowHistoryAction var6 = new ShowHistoryAction(var3, this.opHistoryApi, this.sourceHistoryApi);
         var6.setPageDisplayer(this.activityPageDisplayer);
         var2.add(var6);
      }

      DisposableWindowElementResource var7 = new DisposableWindowElementResource(var2);
      var1.addResource(var7);
      return var2;
   }

   private void installCommands(OperationPage var1) {
      OperationPageCommands var2 = new OperationPageCommands(this.opRunnerFactory, this.entityFactory, var1);

      for (Command var6 : var2.build()) {
         this.addCommand(var6);
      }
   }

   @Override
   protected void postConfiguration(IntegrationEntityPage var1) {
      OperationPage var2 = (OperationPage)var1;
      Operation var3 = var2.getObject();
      this.installOperationMonitoring(var2);
      this.installGraphController(var2, var3);
      this.installGraphSaver(var2, var3);
      this.installDependenciesUpdater(var2, var3);
      this.registerWithGraphManager(var1);
   }

   private void installOperationMonitoring(OperationPage var1) {
      if (this.logApi != null && this.queueApi != null) {
         OperationMonitorApis var2 = new OperationMonitorApis(this.logApi, this.queueApi);
         OperationMonitor var3 = new OperationMonitor(var2);
         var1.installMonitoring(var3);
      }
   }

   private void installGraphController(OperationPage var1, Operation var2) {
      OperationGraphController var3 = this.createGraphController(var2);
      var1.setGraphController(var3);
   }

   private OperationGraphController createGraphController(Operation var1) {
      DefaultOperationGraphController var2 = new DefaultOperationGraphController(
         this.getWindow(), this.getProject(), this.getPersistor(), this.getDeployFacade(), this.getProjectExplorer(), this.entityFactory
      );
      if (this.graphActions != null) {
         var2.setOperationRunnerFactory(this.opRunnerFactory);
         var2.setRunOperationAction(this.graphActions.getAction("action.ic.RunOperation"));
         var2.setDeploySelectedItemsAction(this.graphActions.getAction("action.ic.DeploySelectedObjects"));
         var2.setSetPermissionsAction(this.graphActions.getAction("action.ic.Permissions"));
         var2.setAssignPluginAction(this.graphActions.getAction("action.ic.AssignPlugin"));
         var2.setTestConnectionAction(this.graphActions.getAction("action.ic.TestConnection"));
         var2.setViewDependenciesAction(this.graphActions.getAction("action.ic.ViewDependencies"));
      }

      return var2;
   }

   private void installGraphSaver(OperationPage var1, Operation var2) {
      GraphLayoutFileStore var3 = new GraphLayoutFileStore(var2);
      EdiPluginSaver var4 = this.createEdiPluginSaver(var2);
      OperationGraphSaverImpl var5 = new OperationGraphSaverImpl(var2, var3, var4);
      var1.setGraphLayoutStore(var3);
      var1.setGraphSaver(var5);
   }

   private EdiPluginSaver createEdiPluginSaver(Operation var1) {
      ManagedProject var2 = ProjectUtils.getManagedProject(var1);
      return new EdiPluginSaver(var2);
   }

   private void installDependenciesUpdater(OperationPage var1, Operation var2) {
      ManagedProject var3 = ProjectUtils.getManagedProject(var2);
      OperationPageConfig.DependenciesUpdater var4 = new OperationPageConfig.DependenciesUpdater(var3, var1);
      var1.addResource(new OperationPageConfig.DependenciesUpdaterResource(var4));
   }

   private void registerWithGraphManager(IntegrationEntityPage var1) {
      var1.addResource(new OperationPageConfig.GraphManagerResource());
   }

   private static class DependenciesUpdater {
      private final OperationPage page;
      private final ManagedProject project;

      public DependenciesUpdater(ManagedProject var1, OperationPage var2) {
         this.project = var1;
         this.page = var2;
      }

      public void activate() {
         this.project.getEventBus().register(this);
         this.update();
      }

      public void dispose() {
         this.project.getEventBus().unregister(this);
      }

      @Subscribe
      public void handle(UpdatedDependenciesEvent var1) {
         this.update();
      }

      private void update() {
         DependencyStore var1 = this.project.getDependencyStore();

         for (OperationWrapperNode var3 : this.page.getGraph().getModel().getOperationNodes()) {
            OperationWrapper var4 = var3.getDataObject();
            FluentIterable var5 = var1.getDependingObjects(var4.getActivity())
               .filter(EntityFilters.allOfType(EntityType.HttpEndpoint))
               .filter(HttpEndpoint.class);
            var4.setHttpEndpointTriggers(var5);
         }
      }
   }

   private static class DependenciesUpdaterResource extends AbstractWindowElementResource {
      private final OperationPageConfig.DependenciesUpdater updater;

      public DependenciesUpdaterResource(OperationPageConfig.DependenciesUpdater var1) {
         this.updater = var1;
      }

      @Override
      public void create(WindowElement var1) {
         this.updater.activate();
      }

      @Override
      public void dispose(WindowElement var1) {
         this.updater.dispose();
      }
   }

   private class GraphManagerResource extends AbstractWindowElementResource {
      private ApplicationWindow window;

      private GraphManagerResource() {
      }

      @Override
      public void create(WindowElement var1) {
         this.window = var1.getWindow();
         OperationGraphManager var2 = OperationGraphManager.getManager(this.window);
         var2.registerGraph((OperationPage)var1);
      }

      @Override
      public void dispose(WindowElement var1) {
         OperationGraphManager var2 = OperationGraphManager.getManager(this.window);
         var2.removeGraph((OperationPage)var1);
      }
   }
}
