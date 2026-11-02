package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula;

import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.NodeMapping;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.integration.client.ui.script.builder.ExpressionArea;
import org.etlforge.integration.data.entity.transformation.TransformationVariables;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.NodePath;
import org.etlforge.system.Disposable;

public abstract class AbstractFormulaBuilder implements Disposable {
   private final IntegrationProject project;
   private final FormulaBuilderUi ui;
   private final TreeMapper treeMapper;
   private final String originalExpression;
   private String targetDE;
   private NodeMapping mapping;
   private final Map<String, Integer> caretPositions;

   public AbstractFormulaBuilder(IntegrationProject var1, TreeMapper var2, FormulaBuilderUiFactory var3, String var4, String var5, NodeMapping var6) {
      this.project = var1;
      this.treeMapper = var2;
      this.originalExpression = var4;
      this.targetDE = var5;
      this.mapping = var6;
      this.caretPositions = Maps.newHashMap();
      this.ui = this.createUi(var1, var2, var3, var4);
   }

   private FormulaBuilderUi createUi(IntegrationProject var1, TreeMapper var2, FormulaBuilderUiFactory var3, String var4) {
      SourceTreeFactory var5 = new SourceTreeFactory(var2);
      return new FormulaBuilderUi(var1, var2, var5, var3, this.targetDE, var4);
   }

   @Override
   public final void dispose() {
      try {
         this.ui.dispose();
      } finally {
         this.disposeAdditionalResources();
      }
   }

   protected void disposeAdditionalResources() {
   }

   public final boolean isShowingFormula(Node var1) {
      return this.isVisible() && this.targetDE.equals(var1.m_deName);
   }

   public abstract boolean isVisible();

   public final void showTestResult(TestResult var1) {
      this.restoreCaretPosition();
      this.ui.showFormula();
      this.ui.showTestResult(var1);
   }

   protected final void restoreCaretPosition() {
      Integer var1 = this.caretPositions.get(this.targetDE);
      if (var1 == null) {
         var1 = -1;
      }

      this.ui.setCaretPosition(var1);
   }

   protected final void rememberCaretPosition() {
      int var1 = this.ui.getCaretPosition();
      this.caretPositions.put(this.targetDE, var1);
   }

   public abstract void showFormula();

   public abstract void open();

   public final FormulaBuilderUi getUi() {
      return this.ui;
   }

   public final IntegrationProject getProject() {
      return this.project;
   }

   public final TreeMapper getTreeMapper() {
      return this.treeMapper;
   }

   public final String getOriginalExpression() {
      return this.originalExpression;
   }

   public final NodeMapping getMapping() {
      return this.mapping;
   }

   public final void setMapping(String var1, NodeMapping var2, String var3) {
      this.mapping = var2;
      this.targetDE = var1;
      this.ui.setNodeMappingToDisplay(var1, var3);
      this.mappingChanged();
   }

   protected void mappingChanged() {
   }

   public final TreeComponent getSourceTree() {
      return this.getUi().getSourceTree();
   }

   protected final String getSuggestedTitle() {
      return "Formula Builder: " + this.targetDE.replaceFirst("/PRECONDITION/", "Condition");
   }

   public final void insertExpression(String var1) {
      ExpressionArea var2 = this.ui.getExpressionArea();
      if (org.etlforge.integration.data.script.JavaScriptFunctions.isInside(var2.getText(),var2.getCaretPosition())) {
         String path=org.etlforge.lang.KongaStringUtils.removeStartAndEnd(var1,"[","]");
         var2.insertText("ETLForge.GetSource('"+org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(path)+"')");
         var2.requestFocus();return;
      }
      String var3 = var2.hasTransTag() ? var1 : ScriptConstants.wrapInTags(var1);
      var2.insertText(var3);
      var2.requestFocus();
   }

   public final void refreshSourceTree(MappingTreeStructure var1, String var2) {
      this.ui.refreshSourceTree(var1, var2);
   }

   public final boolean apply() {
      boolean var1 = false;
      FormulaApplier var2 = this.createApplier(var1);
      return this.applyImpl(var2);
   }

   private FormulaApplier createApplier(boolean var1) {
      FormulaApplier var2 = new FormulaApplier(this.treeMapper, this.ui);
      var2.setApplySilently(var1);
      return var2;
   }

   private boolean applyImpl(FormulaApplier var1) {
      boolean var2 = var1.apply(this.mapping, this.targetDE);
      if (var2) {
         this.updateSourceTreeOnMappingChange();
         this.mapping = var1.getMapping();
      }

      return var2;
   }

   private void updateSourceTreeOnMappingChange() {
      if (this.treeMapper.m_sourceTreeComponent.isShowMappedNodesOnly()) {
         this.treeMapper.m_sourceTreeComponent.updateHiddenNodes();
      }
   }

   public final void applySilently() {
      boolean var1 = true;
      FormulaApplier var2 = this.createApplier(var1);
      this.applyImpl(var2);
   }

   public abstract void hide();

   protected final void handleCancel() {
      this.treeMapper.m_stackVector = null;
      this.removeUnsavedDataElements();
   }

   private void removeUnsavedDataElements() {
      NodePath var1 = this.mapping != null ? this.mapping.getTargetPath() : (this.targetDE != null ? new NodePath(this.targetDE) : null);
      if (var1 != null) {
         TransformationVariables var2 = this.treeMapper.getDataElements();
         var2.scanMapping(this.treeMapper.getTransformation(), var1, this.originalExpression);
      }
   }

   public final void setSimpleXmlElementDisplay(boolean var1) {
      TreeComponent var2 = this.getSourceTree();
      var2.setSimpleXmlElementDisplay(var1);
   }
}
