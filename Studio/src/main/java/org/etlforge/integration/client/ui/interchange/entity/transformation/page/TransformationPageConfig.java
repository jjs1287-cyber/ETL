package org.etlforge.integration.client.ui.interchange.entity.transformation.page;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.editor.DefaultEditorController;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorServiceAdapter;
import org.etlforge.application.ui.window.editor.EditorServiceEvent;
import org.etlforge.application.ui.window.editor.EditorServiceListener;
import org.etlforge.cli.Command;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.component.menu.InterchangeMenuDictionary;
import org.etlforge.integration.client.ui.interchange.component.page.entity.EntityPageControllerBuilder;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.deploy.DeployFacade;
import org.etlforge.integration.client.ui.interchange.entity.DefaultPageConfig;
import org.etlforge.integration.client.ui.interchange.entity.TransformationToOperationFactory;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.ClearTestDataAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.CreateOperationAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.EditTargetUpdateKeysAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.EditTransformationSourceStructureAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.EditTransformationTargetStructureAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.EditTransformationWhereClauseAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.ExportMappingsAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.ExportMultipleFoldersAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.ImportMappingsAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.ImportMultipleFoldersAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.LaunchTreeMapperToTextStructureWizardAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.MigrateTransformationAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.PrintMappingsDataAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.RefreshTransformationAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.ShowMappedSourceNodesAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.ShowMappedTargetNodesAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.ShowSourceDataNodesAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.ShowTargetDataNodesAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.TreeMapperSearchAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.ViewInputJtrAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.ViewLinkFileAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.ViewOutputJtrAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.ViewSourceFileAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.ViewTargetFileAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.MappingTreeDataService;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.MappingTreeDataServiceFactory;
import org.etlforge.integration.client.ui.interchange.entity.transformation.menu.NodeExpansionMenu;
import org.etlforge.integration.client.ui.interchange.entity.transformation.menu.TransformationToolBarMenu;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.AutoMapperAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaDisplayer;
import org.etlforge.integration.client.ui.structure.editor.StructureEditorFactories;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.debug.serverapi.DataFileProvider;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.PopupMenuButton;

public final class TransformationPageConfig extends DefaultPageConfig {
   private final UiEntityFactory entityFactory;
   private final MappingStructureFactory mappingStructureFactory;
   private final MappingTreeDataServiceFactory dataServiceFactory;
   private final FormulaDisplayerFactory formulaDisplayerFactory;
   private final DataFileProvider dataFileProvider;
   private final TestActionsFactory testActionsFactory;
   private final StructureEditorFactories structureEditorFactories;
   private final TransformationToOperationFactory operationFactory;
   private ApplicationAction refreshAction;
   private ApplicationAction searchAction;
   private boolean useToolKitMenu;
   private boolean includeNameField;

   public TransformationPageConfig(
      ApplicationWindow var1,
      ProjectPersistor var2,
      UiEntityFactory var3,
      MappingStructureFactory var4,
      MappingTreeDataServiceFactory var5,
      DataFileProvider var6,
      TestActionsFactory var7,
      FormulaDisplayerFactory var8,
      StructureEditorFactories var9,
      TransformationToOperationFactory var10
   ) {
      super(var1, var2);
      this.entityFactory = var3;
      this.mappingStructureFactory = var4;
      this.dataServiceFactory = var5;
      this.dataFileProvider = var6;
      this.testActionsFactory = var7;
      this.formulaDisplayerFactory = var8;
      this.structureEditorFactories = var9;
      this.operationFactory = var10;
   }

   public void setUseToolKitMenu(boolean var1) {
      this.useToolKitMenu = var1;
   }

   public void setIncludeNameField(boolean var1) {
      this.includeNameField = var1;
   }

   @Override
   protected void preConfiguration(IntegrationEntityPage var1) {
      TransformationPage var2 = (TransformationPage)var1;
      var2.setIncludeNameField(this.includeNameField);
      var2.setMappingStructureFactory(this.mappingStructureFactory);
      var2.setStructureEditorFactories(this.structureEditorFactories);
      this.refreshAction = new RefreshTransformationAction(var2);
      this.searchAction = new TreeMapperSearchAction(var2);
      TestActions var3 = new TestActions(var2, this.testActionsFactory);
      this.installPageActions(var2, var3);
      this.setAdditionalPopupMenuActions(var2);
      this.addAdditionalToolbarButtons(var2, var3);
      this.installController(var2);
      this.installFormulaDisplayer(var2);
      this.installTreeDataService(var2);
      this.installPageResources(var2);
      this.installCommands(var2);
   }

   private void installPageActions(TransformationPage var1, TestActions var2) {
      var2.addPageActions(var1);
      var1.addPageActions(
         new ClearTestDataAction(var1),
         new CreateOperationAction(var1.getObject(), this.operationFactory),
         new ShowSourceDataNodesAction(var1),
         new ShowTargetDataNodesAction(var1),
         new ViewSourceFileAction(var1),
         new ViewTargetFileAction(var1),
         new ViewLinkFileAction(var1),
         new ExportMappingsAction(var1),
         new ExportMultipleFoldersAction(var1),
         new ImportMappingsAction(var1),
         new ImportMultipleFoldersAction(var1),
         new LaunchTreeMapperToTextStructureWizardAction(var1, this.entityFactory, this.getPersistor()),
         new MigrateTransformationAction(var1),
         new PrintMappingsDataAction(var1),
         this.refreshAction,
         this.searchAction,
         new EditTransformationSourceStructureAction(var1),
         new EditTransformationTargetStructureAction(var1),
         new EditTransformationWhereClauseAction(var1),
         new EditTargetUpdateKeysAction(var1),
         new ShowMappedSourceNodesAction(var1),
         new ShowMappedTargetNodesAction(var1)
      );
   }

   private void setAdditionalPopupMenuActions(TransformationPage var1) {
      Transformation var2 = var1.getObject();
      ApplicationWindow var3 = this.getWindow();
      this.addToolBarContextMenuAction(null);
      this.addToolBarContextMenuAction(new ViewInputJtrAction(var3, var2));
      this.addToolBarContextMenuAction(new ViewOutputJtrAction(var3, var2));
   }

   private void addAdditionalToolbarButtons(TransformationPage var1, TestActions var2) {
      ArrayList var3 = Lists.newArrayList();
      if (!var2.isEmpty()) {
         var3.add(null);

         for (ApplicationAction var5 : var2.getAll()) {
            var3.add(this.createToolbarButton(var5));
         }

         var3.add(null);
      }

      var3.add(this.createToolbarButton(new AutoMapperAction(var1)));
      var3.add(null);
      var3.add(this.createToolbarButton(this.refreshAction));
      if (this.useToolKitMenu) {
         var3.add(this.toolkitButton(var1));
      }

      var3.add(this.findButton());

      for (Object __etlItem187 : var3) {
         JButton var7 = (JButton) __etlItem187;
         this.addToolbarButton(var7);
      }
   }

   private JButton toolkitButton(TransformationPage var1) {
      JPopupMenu var2 = this.createToolkitPopupMenu(var1);
      final PopupMenuButton var3 = new PopupMenuButton("", getToolkitIcon(), var2);
      var1.addResource(new AbstractWindowElementResource() {
         @Override
         public void dispose(WindowElement var1) {
            var3.dispose();
         }
      });
      var3.setToolTipText(Strings.get("Page.AdvancedMenu.ToolTip"));
      return ButtonUtils.configureButtonForToolBar(var3);
   }

   private static Icon getToolkitIcon() {
      String var0 = IntegrationEntityPage.USE_MEDIUM_SIZED_BUTTONS_IN_TOOLBAR ? "24" : "16";
      return ClientIcons.getIcon("Toolkit." + var0);
   }

   private JPopupMenu createToolkitPopupMenu(TransformationPage var1) {
      TransformationToolBarMenu var2 = new TransformationToolBarMenu(var1);
      JPopupMenu var3 = var2.createJMenu(InterchangeMenuDictionary.DICTIONARY).getPopupMenu();
      var3.addSeparator();
      if (var1.getObject().isXmlTransformation()) {
         NodeExpansionMenu var4 = new NodeExpansionMenu(var1);
         var3.add(var4.createMenu());
      }

      TreeMapperModeMenu var5 = new TreeMapperModeMenu(var1);
      var3.add(var5.getMenu());
      return var3;
   }

   private JButton findButton() {
      return this.createToolbarButton(this.searchAction);
   }

   private void installController(IntegrationEntityPage var1) {
      DefaultEditorController var2 = EntityPageControllerBuilder.buildController(var1);
      var2.registerAction(KeyUtils.FIND, this.searchAction);
      var2.registerAction(KeyUtils.REFRESH, this.refreshAction);
      this.setController(var2);
   }

   private void installFormulaDisplayer(TransformationPage var1) {
      FormulaDisplayer var2 = this.formulaDisplayerFactory.newDisplayer(var1);
      var1.setFormulaDisplayer(var2);
   }

   private void installTreeDataService(TransformationPage var1) {
      DeployFacade var2 = this.getDeployFacade();
      MappingTreeDataService var3 = this.dataServiceFactory.newService(var1, var2, this.dataFileProvider);
      var1.setMappingTreeDataService(var3);
   }

   private void installPageResources(TransformationPage var1) {
      MappingsChangeListeningResource var2 = new MappingsChangeListeningResource();
      var2.setLogger(ApplicationLog.getMainLogger());
      var1.addResource(var2);
      var1.addResource(new TransformationPageConfig.TestResultCloser(this.getWindow(), var1));
   }

   private void installCommands(TransformationPage var1) {
      TransformationPageCommands var2 = new TransformationPageCommands(var1);

      for (Command var6 : var2.build()) {
         this.addCommand(var6);
      }
   }

   private static final class TestResultCloser extends AbstractWindowElementResource {
      private final ApplicationWindow window;
      private final TransformationPage page;
      private final EditorServiceListener listener = new EditorServiceAdapter() {
         @Override
         public void activeEditorChanged(EditorServiceEvent var1) {
            Editor var2 = var1.getEditor();
            if (var2 != TestResultCloser.this.page && TestResultCloser.this.page.getWindow() != null) {
               TestResultCloser.this.page.getTreeMapper().getTreeMapperContainer().getFormulaDisplayer().closeIfBlocking();
            }
         }
      };

      public TestResultCloser(ApplicationWindow var1, TransformationPage var2) {
         this.window = var1;
         this.page = var2;
      }

      @Override
      public void create(WindowElement var1) {
         this.window.getEditorService().addEditorServiceListener(this.listener);
      }

      @Override
      public void dispose(WindowElement var1) {
         this.window.getEditorService().removeEditorServiceListener(this.listener);
      }
   }
}
