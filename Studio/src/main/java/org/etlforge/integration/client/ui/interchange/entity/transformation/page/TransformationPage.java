package org.etlforge.integration.client.ui.interchange.entity.transformation.page;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.ui.de.global.ExpressionSourceDisplayer;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageModel;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.client.ui.interchange.debug.TestResultDisplayer;
import org.etlforge.integration.client.ui.interchange.entity.transformation.TransformationEditor;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.MappingTreeDataService;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperMode;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaDisplayer;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.structure.editor.StructureEditorFactories;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.TransformationExpressionSource;
import org.etlforge.integration.data.entity.transformation.TransformationVariables;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.script.ExpressionSource;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.structure.mapping.NodePath;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.event.MouseActionListener;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayDecorator;

public final class TransformationPage extends IntegrationEntityPage implements ExpressionSourceDisplayer, TestResultDisplayer {
   private MappingStructureFactory mappingStructureFactory;
   private MappingTreeDataService treeDataService;
   private FormulaDisplayer formulaDisplayer;
   private boolean refreshMappingTreeAfterSave;
   private final PostLoadingService postLoadingService;
   private final MappingNodeFilterChangeReaction nodeFilterChangeHandler;
   private final Map<String, ApplicationAction> pageActions;
   private TransformationEditor structureEditor;
   private StructureEditorFactories structureEditorFactories;
   private DataWarningPresenter dataWarningsPresenter;
   private final BorderLayoutBuilder pageLayout = new BorderLayoutBuilder(0, 0).nonOpaque();

   public TransformationPage(Transformation var1) {
      super(new IntegrationEntityPageModel(var1));
      this.postLoadingService = new PostLoadingService();
      this.setHelpId("integration.transformation.mapping");
      this.nodeFilterChangeHandler = this.installNodeFilterListener();
      this.pageActions = Maps.newHashMap();
   }

   private MappingNodeFilterChangeReaction installNodeFilterListener() {
      MappingNodeFilterChangeReaction var1 = new MappingNodeFilterChangeReaction(this);
      Transformation var2 = this.getObject();
      var2.addPropertyChangeListener("InputNodeFilter", var1);
      var2.addPropertyChangeListener("OutputNodeFilter", var1);
      return var1;
   }

   public void setFormulaDisplayer(FormulaDisplayer var1) {
      this.formulaDisplayer = Preconditions.checkNotNull(var1);
   }

   public MappingStructureFactory getMappingStructureFactory() {
      return this.mappingStructureFactory;
   }

   public void setMappingStructureFactory(MappingStructureFactory var1) {
      this.mappingStructureFactory = var1;
   }

   public MappingTreeDataService getTreeDataService() {
      return this.treeDataService;
   }

   public void setMappingTreeDataService(MappingTreeDataService var1) {
      this.treeDataService = var1;
   }

   public TransformationEditor getStructureEditor() {
      if (this.structureEditor == null) {
         if (this.structureEditorFactories == null) {
            throw new RuntimeException("The page has not been configured to edit structures. This is a bug.");
         }

         this.structureEditor = new TransformationEditor(this, this.structureEditorFactories);
      }

      return this.structureEditor;
   }

   public void setStructureEditorFactories(StructureEditorFactories var1) {
      this.structureEditorFactories = var1;
   }

   public TreeMapper getTreeMapper() {
      TransformationPanel var1 = this.getDetailsPanel();
      return var1 == null ? null : var1.getTreeMapper();
   }

   public Transformation getObject() {
      return (Transformation)super.getObject();
   }

   protected TransformationPanel createDataPanel() {
      ApplicationAction var1 = this.getPageAction("action.ic.tx.LoadSource");
      TransformationPanel var2 = new TransformationPanel(this, this.mappingStructureFactory, this.formulaDisplayer, var1);
      DataFileResource var3 = new DataFileResource(var2.getTreeMapper());
      this.addResource(var3);
      return var2;
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.installKeyboardShortcuts();
   }

   private void installKeyboardShortcuts() {
      TestActionsShortcut var1 = new TestActionsShortcut(this);
      var1.install();
      EditActionsShortcut var2 = new EditActionsShortcut(this);
      var2.install();
   }

   @Override
   protected void prepareForRemoval(ApplicationWindow var1) {
      TreeMapper var2 = this.getTreeMapper();
      if (var2 != null) {
         var2.prepareForRemoval();
      }

      this.uninstallNodeFilterListener();
      this.removeUncommitedDataElements();
      this.disposePageActions();
      super.prepareForRemoval(var1);
   }

   private void uninstallNodeFilterListener() {
      Transformation var1 = this.getObject();
      var1.removePropertyChangeListener("InputNodeFilter", this.nodeFilterChangeHandler);
      var1.removePropertyChangeListener("OutputNodeFilter", this.nodeFilterChangeHandler);
   }

   private void removeUncommitedDataElements() {
      Transformation var1 = this.getObject();
      if (!var1.isDeleted()) {
         TransformationVariables var2 = this.getTreeMapper().getDataElements();
         var2.scanTransformation(var1);
      }
   }

   private void disposePageActions() {
      for (ApplicationAction var2 : this.pageActions.values()) {
         var2.dispose();
      }
   }

   void pageIsLoaded() {
      this.nodeFilterChangeHandler.activate();
      this.postLoadingService.pageIsLoaded();
   }

   public void addPostPageLoadingJob(Runnable var1) {
      Preconditions.checkNotNull(var1, "job");
      this.postLoadingService.addJob(var1);
   }

   public void waitUntilFullyLoaded() throws InterruptedException {
      this.postLoadingService.waitUntilLoaded();
   }

   @Override
   public void showTestResult(final TestResult var1) {
      this.addPostPageLoadingJob(new Runnable() {
         @Override
         public void run() {
            TransformationPage.this.getTreeMapper().showTestResult(var1);
         }
      });
   }

   public void showMapping(NodePath var1) {
      Node var2 = this.getTreeMapper().m_targetTreeComponent.findNode_expandTreeIfNotExist(var1.path);
      if (var2 != null) {
         this.getTreeMapper().showFormulaBuilder(var2);
      }
   }

   protected TransformationPanel getDetailsPanel() {
      return (TransformationPanel)super.getDetailsPanel();
   }

   @Override
   public void display(ExpressionSource var1) {
      Preconditions.checkArgument(var1 instanceof TransformationExpressionSource, "s must be a TransformationExpressionSource");
      final TransformationExpressionSource var2 = (TransformationExpressionSource)var1;
      this.addPostPageLoadingJob(new Runnable() {
         @Override
         public void run() {
            TransformationPage.this.showMapping(var2.getTargetNode());
         }
      });
   }

   public void requestPostSaveMappingTreeRefresh() {
      this.refreshMappingTreeAfterSave = true;
   }

   @Override
   protected void doAdditionalSaveWorkBeforePageIsMarkedAsClean() throws InterchangeSavingException {
      if (this.refreshMappingTreeAfterSave) {
         this.refreshMappingTreeAfterSave = false;
         this.startPostSaveRefresh();
      }
   }

   private void startPostSaveRefresh() {
      Runnable var1 = new Runnable() {
         @Override
         public void run() {
            TransformationPageRefresher var1x = new TransformationPageRefresher(TransformationPage.this);
            var1x.setSavePageWhenDone(true);
            var1x.refresh();
         }
      };
      KongaSwingWorker.runWhileWaiting(var1, this.getWaitService());
   }

   public void setDisplayMode(TreeMapperMode var1) {
      this.getTreeMapper().setMode(var1);
   }

   public void addPageActions(ApplicationAction... var1) {
      for (ApplicationAction var5 : var1) {
         this.pageActions.put(var5.getID(), var5);
      }
   }

   public ApplicationAction getPageAction(String var1) {
      return this.pageActions.get(var1);
   }

   public void saveAndRun(Runnable var1) {
      if (this.isDirty()) {
         try {
            this.save();
         } catch (SaveEditorException var3) {
            NonBlockingAlert.error(this.getWindow().getFrame(), var3.getMessage(), "Cannot Save");
            return;
         }
      }

      var1.run();
   }

   public void confirmOpeningOfLargeTestFile(final Receiver<Boolean> var1) {
      JLabel var2 = new JLabel(
         "<html>This Transformation has test data available for display. The test data is large,<br>and displaying it may affect performance.<br><br>Do you want to display the test data?</html>"
      );
      var2.setBorder(Empty.border(10));
      OverlayDecorator var3 = new OverlayDecorator(var2, "Display Test Data?") {
         @Override
         protected void onOk() {
            var1.handle(true);
         }

         @Override
         protected void onCancel() {
            var1.handle(false);
         }
      };
      var3.setTitleIcon(CommonIcons.WARNING_24);
      var3.setBlocking(true);
      var3.useOkAndCancel();
      var3.installOkAndCancelShortCuts();
      var3.getOkAction().putValue("Name", "&Yes");
      var3.getCancelAction().putValue("Name", "&No");
      var3.show(this.getOverlayContainer(), new OverlayCenterPositioner());
      this.askForAttention();
   }

   public void showDataLoadWarning(String var1, String var2) {
      if (this.dataWarningsPresenter == null) {
         this.dataWarningsPresenter = new DataWarningPresenter(this.getOverlayContainer());
      }

      this.dataWarningsPresenter.showWarning(var1, var2);
   }

   @Override
   protected void layoutPage(IntegrationEntityPanel var1) {
      this.pageLayout.center(var1.getUi());
      this.setMainPanel(this.pageLayout.container());
      this.installContextMenuTrigger(var1.getUi());
   }

   private void installContextMenuTrigger(JPanel var1) {
      var1.addMouseListener(new MouseActionListener() {
         @Override
         protected void onPopupTrigger(MouseEvent var1) {
            Point var2 = SwingUtilities.convertPoint(var1.getComponent(), var1.getPoint(), TransformationPage.this.getMainPanel());
            TransformationPage.this.showContextMenu(var2.x, var2.y);
         }
      });
   }

   public void layoutBelowMappingTree(JComponent var1) {
      this.pageLayout.south(var1);
      this.pageLayout.refreshLayout();
   }
}
