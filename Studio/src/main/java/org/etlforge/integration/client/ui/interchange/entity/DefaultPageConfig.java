package org.etlforge.integration.client.ui.interchange.entity;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.action.popup.PopupMenuManager;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.SaveEditorAction;
import org.etlforge.application.ui.window.action.WindowActionDictionary;
import org.etlforge.application.ui.window.editor.CloseEditorAction;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorController;
import org.etlforge.application.ui.window.editor.EditorHistoryPoint;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.cli.Command;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.cli.CommandFactory;
import org.etlforge.cli.CommandModule;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.interchange.component.page.entity.ActiveObjectPageAction;
import org.etlforge.integration.client.ui.interchange.component.page.entity.EntityPageControllerBuilder;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequenceFactory;
import org.etlforge.integration.client.ui.interchange.component.page.entity.dependencies.EntityDependenciesModel;
import org.etlforge.integration.client.ui.interchange.core.action.LocatePageInTreeAction;
import org.etlforge.integration.client.ui.interchange.core.action.ViewAdditionalEntityDetailsAction;
import org.etlforge.integration.client.ui.interchange.core.action.ViewEntityXmlAction;
import org.etlforge.integration.client.ui.interchange.deploy.DeployFacade;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.DeleteRequestHandler;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.widget.button.KongaButton;

public class DefaultPageConfig implements PageConfig {
   private final ApplicationWindow appWin;
   private final ProjectPersistor persistor;
   private IntegrationProject project;
   private DeployFacade deployFacade;
   private HistoryPointFactory historyPointFactory;
   private EntityExplorerSupport explorer;
   private PopupMenuManager contextMenu;
   private DeleteRequestHandler deleteRequestHandler;
   private EntityFilter publicEntityTypesFilter = EntityFilters.ACCEPTS_ALL;
   private ImmutableList<? extends ApplicationAction> defaultToolbarActions = ImmutableList.of();
   private final List<SaveConsequenceFactory> saveConsequenceFactories = Lists.newArrayList();
   private final List<Action> additionalPopupMenuActions = Lists.newArrayList();
   private final List<JButton> additionalToolbarButtons = Lists.newArrayList();
   private final CommandModule commands = new CommandModule();
   private EditorController controller;

   protected DefaultPageConfig(ApplicationWindow var1, ProjectPersistor var2) {
      this.appWin = var1;
      this.persistor = var2;
   }

   protected final ApplicationWindow getWindow() {
      return this.appWin;
   }

   protected final ProjectPersistor getPersistor() {
      return this.persistor;
   }

   public final void setProject(IntegrationProject var1) {
      this.project = var1;
   }

   protected final IntegrationProject getProject() {
      return this.project;
   }

   public final void setDeployFacade(DeployFacade var1) {
      this.deployFacade = var1;
   }

   protected final DeployFacade getDeployFacade() {
      return this.deployFacade;
   }

   public final void setHistoryPointFactory(HistoryPointFactory var1) {
      this.historyPointFactory = var1;
   }

   public final void setProjectExplorer(EntityExplorerSupport var1) {
      this.explorer = var1;
   }

   protected final EntityExplorerSupport getProjectExplorer() {
      return this.explorer;
   }

   public final void setDeleteRequestHandler(DeleteRequestHandler var1) {
      this.deleteRequestHandler = var1;
   }

   public final void setPublicEntityTypesFilter(EntityFilter var1) {
      this.publicEntityTypesFilter = var1;
   }

   public final void setDefaultToolbarActions(List<? extends ApplicationAction> var1) {
      this.defaultToolbarActions = ImmutableList.copyOf(var1);
   }

   public final void setContextMenu(PopupMenuManager var1) {
      this.contextMenu = var1;
   }

   protected final void setController(EditorController var1) {
      this.controller = var1;
   }

   protected final void addSaveConsequenceFactory(SaveConsequenceFactory var1) {
      this.saveConsequenceFactories.add(var1);
   }

   protected final void addToolBarContextMenuAction(Action var1) {
      this.additionalPopupMenuActions.add(var1);
   }

   protected final void addToolbarButton(JButton var1) {
      this.additionalToolbarButtons.add(var1);
   }

   protected final void addCommand(Command var1) {
      this.commands.addCommand(var1);
   }

   protected final ActiveObjectPageAction createActiveObjectAction(IntegrationEntityPage var1, ApplicationAction var2) {
      ActiveObjectPageAction var3 = new ActiveObjectPageAction(var1, var2);
      var1.addActiveObjectAction(var3);
      return var3;
   }

   protected final JButton createToolbarButton(Action var1) {
      KongaButton var2 = KongaButton.forToolbar(var1);
      if (IntegrationEntityPage.USE_MEDIUM_SIZED_BUTTONS_IN_TOOLBAR) {
         var2.setUseMediumActionIcon(true);
      } else {
         var2.setUseSmallActionIcon(true);
      }

      return var2;
   }

   @Override
   public final void configure(IntegrationEntityPage var1) {
      this.preConfiguration(var1);
      this.defaultConfiguration(var1);
      this.postConfiguration(var1);
   }

   protected void preConfiguration(IntegrationEntityPage var1) {
   }

   protected void postConfiguration(IntegrationEntityPage var1) {
   }

   private void defaultConfiguration(IntegrationEntityPage var1) {
      var1.setEntityPersistor(this.persistor);
      var1.setEntityExplorerSupport(this.explorer);
      var1.setContextMenuHandler(this.contextMenu);
      this.installTransientEntityDeleter(var1);
      this.installController(var1);
      this.installSaveConsequences(var1);
      this.installValidation(var1);
      this.setHistoryPoint(var1);
      this.installDependenciesModel(var1);
      this.installToolbarPopupMenuActions(var1);
      this.installDefaultToolbarButtons(var1);
      this.installAdditionalToolbarButtons(var1);
      this.installCommands(var1);
   }

   private void installTransientEntityDeleter(IntegrationEntityPage var1) {
      TransientEntityDeleter var2 = new TransientEntityDeleter(var1.getObject(), this.deleteRequestHandler);
      var1.addResource(var2);
   }

   private void installController(IntegrationEntityPage var1) {
      if (this.controller == null) {
         this.controller = EntityPageControllerBuilder.buildController(var1);
      }

      var1.setController(this.controller);
   }

   private void installSaveConsequences(IntegrationEntityPage var1) {
      for (SaveConsequenceFactory var3 : this.saveConsequenceFactories) {
         Optional var4 = var3.getSaveConsequence(var1);
         if (var4.isPresent()) {
            var1.addSaveConsequence((SaveConsequence)var4.get());
         }
      }
   }

   private void installValidation(IntegrationEntityPage var1) {
      ManagedProject var2 = ProjectUtils.getManagedProject(var1.getObject());
      if (var2 != null) {
         var1.setValidationModel(var2.getValidationModel());
      }
   }

   private void setHistoryPoint(IntegrationEntityPage var1) {
      EditorHistoryPoint var2 = this.historyPointFactory.createHistoryPoint(var1);
      var1.setEditorHistoryPoint(var2);
   }

   private void installDependenciesModel(IntegrationEntityPage var1) {
      DependenciesModelBuilder var2 = new DependenciesModelBuilder(this.publicEntityTypesFilter);
      EntityDependenciesModel var3 = var2.buildModel(var1.getObject());
      var1.getPageModel().setDependencies(var3);
   }

   private void installToolbarPopupMenuActions(IntegrationEntityPage var1) {
      List var2 = this.getToolbarPopupMenuActions(var1);
      var1.setToolbarPopupMenuActions(var2);
   }

   private List<Action> getToolbarPopupMenuActions(IntegrationEntityPage var1) {
      ArrayList var2 = Lists.newArrayList();
      var2.add(new SaveEditorAction(var1));
      var2.add(new CloseEditorAction(var1));
      var2.add(new DefaultPageConfig.CloseOthersAction(var1));
      var2.add(new LocatePageInTreeAction(this.appWin.getEditorService()));
      var2.add(null);
      var2.add(new ViewAdditionalEntityDetailsAction(var1.getObject()));
      var2.add(new ViewEntityXmlAction(this.appWin, var1.getObject()));
      var2.addAll(this.additionalPopupMenuActions);
      return var2;
   }

   private void installDefaultToolbarButtons(IntegrationEntityPage var1) {
      for (ApplicationAction var3 : this.defaultToolbarActions) {
         ActiveObjectPageAction var4 = this.createActiveObjectAction(var1, var3);
         var1.addToolbarButton(this.createToolbarButton(var4));
      }
   }

   private void installAdditionalToolbarButtons(IntegrationEntityPage var1) {
      for (JButton var3 : this.additionalToolbarButtons) {
         var1.addToolbarButton(var3);
      }
   }

   private void installCommands(final IntegrationEntityPage var1) {
      this.commands.addCommand(CommandFactory.newCommand("used by", new CommandExecutor() {
         @Override
         public void execute(String[] var1x) throws CommandException {
            var1.showDependentObjects();
         }
      }));
      this.commands.addCommand(CommandFactory.newCommand("focus", new CommandExecutor() {
         @Override
         public void execute(String[] var1x) throws CommandException {
            var1.restoreFocus(0);
         }
      }));
      var1.setCommands(this.commands);
   }

   private static class CloseOthersAction extends AbstractAction {
      private final Editor editor;

      public CloseOthersAction(Editor var1) {
         this.editor = var1;
         ActionDecorators.decorateFromDictionary(this, WindowActionDictionary.DICTIONARY, "action.CloseOthers");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ApplicationWindow var2 = this.editor.getWindow();
         if (var2 != null) {
            EditorService var3 = var2.getEditorService();
            var3.closeOtherEditors(this.editor);
         }
      }
   }
}
