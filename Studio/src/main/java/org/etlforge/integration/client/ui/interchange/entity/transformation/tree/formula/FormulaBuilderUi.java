package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula;

import javax.swing.Action;
import javax.swing.JComponent;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.NodeVisibilityFilter;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.SourceTreeDataElementContainer;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.DataTreeListener;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.integration.client.ui.script.builder.ExpressionArea;
import org.etlforge.integration.client.ui.script.builder.ExpressionScanner;
import org.etlforge.integration.client.ui.script.builder.FunctionInserterLookup;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderModel;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderUi;
import org.etlforge.integration.data.entity.transformation.TransformationExpressionSource;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.debug.client.TargetNode;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.NodePath;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;

public final class FormulaBuilderUi implements UiProvider, Disposable {
   private final TreeMapper treeMapper;
   private final SourceTreeLayout sourceTreeLayout;
   private final ScriptBuilderUi scriptBuilder;
   private final SourceTreeFactory sourceTreeFactory;
   private final FormulaBuilderSourceTree sourceTree;
   private final ScriptBuilderModel scriptModel;
   private TransformationExpressionSource exprSource;
   private ExpressionParser expressionParser;
   private ExpressionScanner expressionScanner;
   private final FunctionInserterLookup functionInserterLookup;
   private final SourceTreeDataElementContainer sourceDEs;

   public FormulaBuilderUi(IntegrationProject var1, TreeMapper var2, SourceTreeFactory var3, FormulaBuilderUiFactory var4, String var5, String var6) {
      this.treeMapper = var2;
      this.sourceTreeFactory = var3;
      this.sourceDEs = new SourceTreeDataElementContainer(var2.m_sourceTreeComponent);
      this.sourceTree = var3.newSourceTree();
      this.sourceTreeLayout = this.layoutSourceTree();
      this.exprSource = new TransformationExpressionSource(var2.getTransformation(), new NodePath(var5));
      this.scriptModel = this.createScriptBuilderModel(var1);
      Node var7 = var2.findTargetNode(var5);
      this.scriptBuilder = this.createScriptBuilder(var4, var7, var6);
      this.expressionParser = this.createExpressionParser();
      this.expressionScanner = this.installExpressionScanner();
      this.functionInserterLookup = this.createFunctionInserterLookup();
      ExpressionArea var8 = this.scriptBuilder.getExpressionArea();
      var8.setFunctionInserterLookup(this.functionInserterLookup);
      this.sourceTree.getDataTree().addListener(new FormulaBuilderUi.SourceDataListener());
   }

   private SourceTreeLayout layoutSourceTree() {
      return new SourceTreeLayout(this.sourceTree, new Receiver<String>() {
         public void handle(String var1) {
            FormulaBuilderUi.this.treeMapper.getTreeMapperContainer().getFormulaDisplayer().insertExpression(var1);
         }
      });
   }

   private ScriptBuilderModel createScriptBuilderModel(IntegrationProject var1) {
      ScriptBuilderModel var2 = new ScriptBuilderModel(var1);
      var2.setSource(this.exprSource);
      var2.setMustBeSingleValue(this.treeMapper.m_sourceTreeComponent.m_flat);
      return var2;
   }

   private ScriptBuilderUi createScriptBuilder(FormulaBuilderUiFactory var1, Node var2, String var3) {
      ScriptBuilderUi var4 = var1.createScriptBuilderUi(
         this.treeMapper.getTransformation(), this.scriptModel, this.sourceTreeLayout, this.sourceDEs, this.createTargetNode(var2), var3
      );
      this.configureCompletionContext(var4);
      return var4;
   }

   private void configureCompletionContext(ScriptBuilderUi var1) {
      SourceTreeCompletionContext var2 = new SourceTreeCompletionContext(this.sourceTree);
      var1.addAdditionalCompletionContext(var2);
   }

   private ExpressionParser createExpressionParser() {
      ExpressionParser var1 = new ExpressionParser(this.exprSource);
      var1.setSourceDataElementContainer(this.sourceDEs);
      return var1;
   }

   private ExpressionScanner installExpressionScanner() {
      ExpressionScannerBuilder var1 = new ExpressionScannerBuilder(
         this.treeMapper.m_sourceTreeComponent, this.exprSource, this.scriptModel.getProject().getGlobalDataElements(), this.expressionParser
      );
      ExpressionScanner var2 = var1.buildScanner();
      this.scriptBuilder.installScriptScanner(var2);
      return var2;
   }

   private FormulaBuilderFunctionInserterLookup createFunctionInserterLookup() {
      ExpressionArea var1 = this.scriptBuilder.getExpressionArea();
      FunctionInserterLookup var2 = var1.getFunctionInserterLookup();
      FormulaBuilderFunctionInserterLookup var3 = new FormulaBuilderFunctionInserterLookup(var2, this.sourceTreeFactory);
      var3.setParser(this.expressionParser);
      return var3;
   }

   public TreeComponent getSourceTree() {
      return this.sourceTree;
   }

   @Override
   public void dispose() {
      this.scriptBuilder.dispose();
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.scriptBuilder.decorate(var1);
      this.sourceTreeLayout.expand();
   }

   @Override
   public void requestFocus() {
      this.scriptBuilder.requestFocus();
      this.sourceTree.repaint();
   }

   public void setPathChangeReaction(Runnable var1) {
      this.scriptBuilder.setPathChangeReaction(var1);
   }

   public Action getTestAction() {
      return this.scriptBuilder.getTestAction();
   }

   public ExpressionArea getExpressionArea() {
      return this.scriptBuilder.getExpressionArea();
   }

   public void setNodeMappingToDisplay(String var1, String var2) {
      this.exprSource = new TransformationExpressionSource(this.treeMapper.getTransformation(), new NodePath(var1));
      this.expressionParser = this.createExpressionParser();
      this.scriptModel.setSource(this.exprSource);
      TargetNode var3 = this.getTargetNode(var1);
      this.scriptBuilder.getDebugUi().getModel().setTargetNode(var3);
      this.expressionScanner.uninstall();
      this.scriptBuilder.setExpression(checkEmptyExpression(var2));
      this.expressionScanner = this.installExpressionScanner();
   }

   private TargetNode getTargetNode(String var1) {
      Node var2 = this.treeMapper.findTargetNode(var1);
      return var2 == null ? null : this.createTargetNode(var2);
   }

   private TargetNode createTargetNode(Node var1) {
      CROM var2 = var1.getCROM();
      return new TargetNode(var1.m_deName, var2.getValueType(), var2.getSqlDataType(), var2.getFormat());
   }

   private static String checkEmptyExpression(String var0) {
      return var0.isEmpty() ? "<trans>\n\n</trans>" : var0;
   }

   public String getExpression() {
      return this.scriptBuilder.getExpression();
   }

   public String getTranslatedExpression() {
      return this.scriptBuilder.getTranslatedExpression();
   }

   public void showTestResult(TestResult var1) {
      this.scriptBuilder.showTestResult(var1);
   }

   void refreshSourceTree(MappingTreeStructure var1, String var2) {
      this.sourceTree.createTreeFromCromTree(this.treeMapper.m_sourceTreeComponent.m_CROM_Tree, var2);
      this.sourceTree.checkSalesforceNode();
   }

   void showFormula() {
      this.updateSourceTree();
      if (!this.scriptBuilder.hasExpression()) {
         this.requestFocus();
      }
   }

   private void updateSourceTree() {
      this.sourceTree.setShowDataNodesOnly(this.treeMapper.m_sourceTreeComponent.isShowDataNodesOnly());
      this.updateHiddenNodes();
   }

   private void updateHiddenNodes() {
      NodeVisibilityFilter var1 = this.treeMapper.m_sourceTreeComponent.getFilterForFormulaBuilder(this.sourceTree);
      this.sourceTree.setNodeVisibilityFilter(var1);
      this.sourceTree.updateHiddenNodes();
   }

   public void setCaretPosition(int var1) {
      if (var1 >= 0) {
         this.scriptBuilder.setCaretPosition(var1);
      } else {
         this.scriptBuilder.setDefaultCaretPosition();
      }
   }

   public int getCaretPosition() {
      return this.scriptBuilder.getCaretPosition();
   }

   public void setButtonPanel(JComponent var1) {
      this.scriptBuilder.setButtonPanel(var1);
   }

   private class SourceDataListener implements DataTreeListener {
      private SourceDataListener() {
      }

      @Override
      public void newDataLoaded() {
         this.updateDataElements();
      }

      @Override
      public void dataRemoved() {
         this.updateDataElements();
      }

      @Override
      public void newDataInstanceSelected(String var1) {
         this.updateDataElements();
      }

      private void updateDataElements() {
         DataElementFactoryImpl var1 = new DataElementFactoryImpl(FormulaBuilderUi.this.treeMapper.m_sourceTreeComponent, FormulaBuilderUi.this.exprSource);
         FormulaBuilderUi.this.expressionScanner.setDataElementFactory(var1);
         FormulaBuilderUi.this.expressionScanner.doScan();
      }
   }
}
